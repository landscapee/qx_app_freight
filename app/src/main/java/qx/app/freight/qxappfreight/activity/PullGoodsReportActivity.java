package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.PullGoodsInfoAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.LocalBillBean;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.ExceptionReportEntity;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.PullGoodsReportContract;
import qx.app.freight.qxappfreight.contract.ScanScooterContract;
import qx.app.freight.qxappfreight.contract.ScooterInfoListContract;
import qx.app.freight.qxappfreight.dialog.ChooseGoodsBillDialog;
import qx.app.freight.qxappfreight.presenter.PullGoodsReportPresenter;
import qx.app.freight.qxappfreight.presenter.ScanScooterPresenter;
import qx.app.freight.qxappfreight.presenter.ScooterInfoListPresenter;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

/**
 * 拉货上报页面
 */
public class PullGoodsReportActivity extends BaseActivity implements ScanScooterContract.scanScooterView, ScooterInfoListContract.scooterInfoListView, PullGoodsReportContract.pullGoodsView {
    @BindView(R.id.tv_flight_info)
    TextView mTvFlightInfo;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.srv_goods_info)
    SlideRecyclerView mSrvGoods;
    @BindView(R.id.ll_add_scan)
    LinearLayout mLlAddScan;
    @BindView(R.id.btn_commit)
    Button mBtnCommit;
    private List<TransportTodoListBean> mPullBoardList = new ArrayList<>();//板车拉下数据列表
    private List<TransportTodoListBean> mPullBillList = new ArrayList<>();//运单拉下数据列表
    private PullGoodsInfoAdapter mGoodsAdapter;
    private String[] mInfoList;
    private SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
    private int mDeletePos = -1;        //左滑删除时的下标标记值
    private boolean mIsScanBill = false;//是否是扫描运单拉下，默认是扫板
    private List<LocalBillBean> mBillList = new ArrayList<>();
    private Map<String, LocalBillBean> mCodeMap = new HashMap<>();//以运单code为键，对应的对象为值的map
    private String mCurrentTaskId;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {
        if (result != null) {
            if (result.isCancelFlag()) {
                String taskId = result.getTaskId();
                if (taskId.equals(mCurrentTaskId)) {
                    ToastUtil.showToast("当前卸机任务已取消");
                    Observable.timer(2, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()) // timer 默认在新线程，所以需要切换回主线程
                            .subscribe(aLong -> finish());
                }
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pull_goods_report;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ;
        mBillList = getIntent().getParcelableArrayListExtra("bill_list");
        for (LocalBillBean bean : mBillList) {
            mCodeMap.put(bean.getWayBillCode(), bean);
        }
        mPresenter = new ScanScooterPresenter(this);
        ((ScanScooterPresenter) mPresenter).scooterWithUser(UserInfoSingle.getInstance().getUserId());
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", v -> finish());
        toolbar.setMainTitle(Color.WHITE, "拉货上报");
        String info = getIntent().getStringExtra("plane_info");
        mInfoList = info.split("\\*");
        mCurrentTaskId = mInfoList[12];
        mTvFlightInfo.setText(mInfoList[0]);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        mTvDate.setText(sdf.format(new Date()));
        mLlAddScan.setOnClickListener(v -> {
            if (mIsScanBill) {
                boolean canScan = false;
                for (LocalBillBean billBean : mBillList) {
                    if (billBean.getBillItemNumber() > 0 && billBean.getBillItemWeight() > 0) {//遍历封装的装机运单列表数据，如果某一个运单的可分装件数或质量都大于0，则可以继续去扫描二维码
                        canScan = true;
                        break;
                    }
                }
                if (canScan) {//可以扫描
                    ScanManagerActivity.startActivity(PullGoodsReportActivity.this, "PullGoodsReportActivity");
                } else {
                    ToastUtil.showToast("当前已无可以分装的运单");
                }
            } else {
                ScanManagerActivity.startActivity(PullGoodsReportActivity.this, "PullGoodsReportActivity");
            }
        });
        mBtnCommit.setClickable(false);
        mSrvGoods.setLayoutManager(new LinearLayoutManager(this));
        mGoodsAdapter = new PullGoodsInfoAdapter(mPullBoardList);
        mSrvGoods.setAdapter(mGoodsAdapter);
        mTabLayout.addTab(mTabLayout.newTab().setText("板车拉下"));
        mTabLayout.addTab(mTabLayout.newTab().setText("运单拉下"));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {//选中板车下拉
                    mIsScanBill = false;
                    mGoodsAdapter = new PullGoodsInfoAdapter(mPullBoardList);
                } else {//选中运单下拉
                    mIsScanBill = true;
                    mGoodsAdapter = new PullGoodsInfoAdapter(mPullBillList);
                }
                mSrvGoods.setAdapter(mGoodsAdapter);
                setDeleteListener();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
       setDeleteListener();
        mBtnCommit.setOnClickListener(v -> {
            if (mPullBoardList.size() == 0 && mPullBillList.size() == 0) {//板车和运单列表数据都为空
                ToastUtil.showToast("请扫描添加板车数据！");
            } else {
                boolean infoFull = true;
                if (mPullBoardList.size() != 0) {
                    for (TransportTodoListBean bean : mPullBoardList) {
                        if (bean.getPullInNumber() == 0 && bean.getPullInWeight() == 0.0) {//板车信息中有一条件数并且重量都为0，则不能提交
                            infoFull = false;
                            break;
                        }
                    }
                }
                if (mPullBillList.size() != 0) {
                    for (TransportTodoListBean bean : mPullBillList) {
                        if (bean.getPullInNumber() == 0 && bean.getPullInWeight() == 0.0) {//运单信息中有一条件数并且重量都为0，则不能提交
                            infoFull = false;
                            break;
                        }
                    }
                }
                if (infoFull) {
                    mPresenter = new PullGoodsReportPresenter(PullGoodsReportActivity.this);
                    ExceptionReportEntity entity = new ExceptionReportEntity();
                    entity.setFlightId(Long.valueOf(mInfoList[7]));
                    entity.setFlightNum(mInfoList[0]);
                    entity.setReType(3);
                    entity.setReOperator(UserInfoSingle.getInstance().getUserId());
                    entity.setRpDate(mSdf.format(new Date()));
                    entity.setScooters(mPullBoardList);
                    entity.setWaybillScooters(mPullBillList);
                    entity.setSeat(mInfoList[5]);
                    ((PullGoodsReportPresenter) mPresenter).pullGoodsReport(entity);
                } else {
                    ToastUtil.showToast("信息输入不完整，请检查");
                }
            }
        });
    }

    private void setDeleteListener() {
        mGoodsAdapter.setOnDeleteClickListener((view, position) -> {
                    mDeletePos = position;
                    if (!mIsScanBill) {
                        TransportEndEntity transportEndEntity = new TransportEndEntity();
                        transportEndEntity.setId(mPullBoardList.get(position).getId());
                        ((PullGoodsReportPresenter) mPresenter).scanScooterDelete(transportEndEntity);
                    } else {
                        //删除扫描的运单上拉数据
                        scanScooterDeleteResult("");
                    }
                }
        );
        mGoodsAdapter.setOnItemClickListener((adapter, view, position) -> {
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if ("PullGoodsReportActivity".equals(result.getFunctionFlag())) {
            //根据扫一扫获取的板车信息查找板车内容
            addScooterInfo(result.getData());
        }
    }

    private void setBillDataList(String billCode, TransportTodoListBean bean) {
        bean.setBillCode(billCode);
        bean.setInfoType(Constants.TYPE_PULL_BILL);
        mPullBillList.add(bean);
        mGoodsAdapter = new PullGoodsInfoAdapter(mPullBillList);
        mSrvGoods.setAdapter(mGoodsAdapter);
        mGoodsAdapter.setOnTextWatcher(new PullGoodsInfoAdapter.OnTextWatcher() {
            @Override
            public void onNumberTextChanged(int index, EditText etNumber) {
                if (mIsScanBill) {
                    if (!TextUtils.isEmpty(etNumber.getText().toString())) {
                        int number = Integer.parseInt(etNumber.getText().toString());
                        String billCode = mPullBillList.get(index).getBillCode();//获取当前输入框对应的item的运单code
                        LocalBillBean billBean = mCodeMap.get(billCode);
                        int leftNumber = billBean.getBillItemNumber();//根据运单code获取该运单允许分装的剩余最大件数
                        if (number > leftNumber) {
                            ToastUtil.showToast("数量输入不合法，已设置为允许的件数最大值");
                            etNumber.setText("" + leftNumber);
                            mPullBillList.get(index).setPullInNumber(leftNumber);//输入的分装件数持久化到数据中去
                        } else {
                            mPullBillList.get(index).setPullInNumber(number);//输入的分装件数持久化到数据中去
                        }
                        int usedNumber = 0;
                        for (TransportTodoListBean bean1 : mPullBillList) {
                            if (bean1.getBillCode().equals(billCode)) {
                                usedNumber += bean1.getPullInNumber();
                            }
                        }
                        billBean.setBillItemNumber(billBean.getMaxNumber() - usedNumber);
                    }
                } else {
                    if (!TextUtils.isEmpty(etNumber.getText().toString())) {
                        int number = Integer.parseInt(etNumber.getText().toString());
                        if (number > mPullBoardList.get(index).getMaxBillNumber()) {
                            ToastUtil.showToast("数量输入不合法，已设置为允许的件数最大值");
                            etNumber.setText(mPullBoardList.get(index).getMaxBillNumber() + "");
                        }
                        mPullBoardList.get(index).setPullInNumber(Integer.valueOf(etNumber.getText().toString()));
                    }
                }
            }

            @Override
            public void onWeightTextChanged(int index, EditText etWeight) {
                if (mIsScanBill) {
                    if (!TextUtils.isEmpty(etWeight.getText().toString())) {
                        double weight = Double.parseDouble(etWeight.getText().toString());
                        String billCode = mPullBillList.get(index).getBillCode();//获取当前输入框对应的item的运单code
                        LocalBillBean billBean = mCodeMap.get(billCode);
                        double leftWeight = billBean.getBillItemWeight();//根据运单code获取该运单允许分装的剩余最大质量
                        if (weight > leftWeight) {
                            ToastUtil.showToast("质量输入不合法，已设置为允许的质量最大值");
                            etWeight.setText("" + leftWeight);
                            mPullBillList.get(index).setPullInWeight(leftWeight);//输入的分装质量持久化到数据中去
                        } else {
                            mPullBillList.get(index).setPullInWeight(weight);//输入的分装质量持久化到数据中去
                        }
                        double usedWeight = 0;
                        for (TransportTodoListBean bean1 : mPullBillList) {
                            if (bean1.getBillCode().equals(billCode)) {
                                usedWeight += bean1.getPullInWeight();
                            }
                        }
                        billBean.setBillItemWeight(billBean.getMaxWeight() - usedWeight);
                    }
                } else {
                    if (!TextUtils.isEmpty(etWeight.getText().toString())) {
                        double weight = Double.parseDouble(etWeight.getText().toString());
                        if (weight > mPullBoardList.get(index).getMaxBillWeight()) {
                            ToastUtil.showToast("质量输入不合法，已设置为允许的质量最大值");
                            etWeight.setText(mPullBoardList.get(index).getMaxBillWeight() + "");
                        }
                        mPullBoardList.get(index).setPullInWeight(Double.valueOf(etWeight.getText().toString()));
                    }
                }
            }
        });
        mGoodsAdapter.setOnDeleteClickListener((view, position) -> {
                    mDeletePos = position;
                    if (!mIsScanBill) {
                        TransportEndEntity transportEndEntity = new TransportEndEntity();
                        transportEndEntity.setId(mPullBoardList.get(position).getId());
                        ((PullGoodsReportPresenter) mPresenter).scanScooterDelete(transportEndEntity);
                    } else {
                        //删除扫描的运单上拉数据
                        scanScooterDeleteResult("");
                    }
                }
        );
        mGoodsAdapter.setOnItemClickListener((adapter, view, position) -> {
        });
    }

    private void addScooterInfo(String scooterCode) {
        if (!TextUtils.isEmpty(scooterCode)) {
            if (mIsScanBill) {
                mPresenter = new ScooterInfoListPresenter(this);
                BaseFilterEntity baseFilterEntity = new BaseFilterEntity();
                MyAgentListBean myAgentListBean = new MyAgentListBean();
                baseFilterEntity.setSize(10);
                baseFilterEntity.setCurrent(1);
                myAgentListBean.setScooterCode(scooterCode);
                baseFilterEntity.setFilter(myAgentListBean);
                ((ScooterInfoListPresenter) mPresenter).ScooterInfoList(baseFilterEntity);
            } else {
                mPresenter = new ScanScooterPresenter(this);
                TransportTodoListBean mainIfos = new TransportTodoListBean();
                mainIfos.setTpScooterCode(scooterCode);
                mainIfos.setTpOperator(UserInfoSingle.getInstance().getUserId());
                mainIfos.setDtoType(8);
                mainIfos.setTpStartLocate("seat");
                ((ScanScooterPresenter) mPresenter).scanScooter(mainIfos);
            }
        } else
            ToastUtil.showToast("扫描结果为空请重新扫描");
    }

    @Override
    public void scanScooterResult(String result) {
        if (!"".equals(result)) {
            ((ScanScooterPresenter) mPresenter).scooterWithUser(UserInfoSingle.getInstance().getUserId());
        }
    }

    @Override
    public void scooterWithUserResult(List<TransportTodoListBean> result) {
        if (result != null) {
            for (TransportTodoListBean bean : result) {
                bean.setMaxBillNumber(bean.getTpCargoNumber());
                bean.setMaxBillWeight(bean.getTpCargoWeight());
            }
            mPullBoardList.clear();
            mPullBoardList.addAll(result);
            mGoodsAdapter.notifyDataSetChanged();
        } else
            ToastUtil.showToast(this, "返回数据为空");
    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void showNetDialog() {
    }

    @Override
    public void dissMiss() {
    }

    @Override
    public void pullGoodsReportResult(String result) {
        Log.e("tag", "result=======" + result);
        ToastUtil.showToast("拉货上报提交成功");
        finish();
    }

    @Override
    public void scanScooterDeleteResult(String result) {
        if (!mIsScanBill) {
            mPullBoardList.remove(mDeletePos);
        } else {
            mPullBillList.remove(mDeletePos);
        }
        mSrvGoods.closeMenu();
        mGoodsAdapter.notifyDataSetChanged();
    }

    @Override
    public void scooterInfoListResult(List<ScooterInfoListBean> scooterInfoListBeans) {
        if (scooterInfoListBeans != null && scooterInfoListBeans.size() != 0) {
            ScooterInfoListBean entity = scooterInfoListBeans.get(0);
            TransportTodoListBean bean = new TransportTodoListBean();
            bean.setTpScooterId(entity.getId());
            bean.setTpScooterCode(entity.getScooterCode());
            bean.setTpScooterType(entity.getScooterType() + "");
            bean.setTpFlightNumber(mInfoList[0]);
            bean.setTpFlightLocate(mInfoList[5]);
            bean.setTpFlightTime(Long.valueOf(mInfoList[6]));
            bean.setTpFregihtSpace(getIntent().getStringExtra("fregiht_space"));
            bean.setMaxBillWeight(entity.getScooterWeight());
            bean.setBillNumber(entity.getScooterCode());
            if (mBillList.size() != 0) {
                ChooseGoodsBillDialog dialog = new ChooseGoodsBillDialog();
                List<LocalBillBean> list = new ArrayList<>();
                //筛选没有拉完的运单
                for (LocalBillBean mLocalBillBean : mBillList) {
                    if (mLocalBillBean.getBillItemNumber() > 0 && mLocalBillBean.getBillItemWeight() > 0)
                        list.add(mLocalBillBean);
                }
                if (list.size() == 0) {
                    ToastUtil.showToast("当前航班已无运单任务可分配");
                } else {
                    dialog.setData(PullGoodsReportActivity.this, entity.getScooterCode(), list);
                    dialog.show(getSupportFragmentManager(), "123");
                    dialog.setOnBillSelectListener(pos -> {
                        String bill = list.get(pos).getWayBillCode();
                        bean.setCarType(list.get(pos).getCargoType());
                        bean.setWaybillId(list.get(pos).getWaybillId());
                        setBillDataList(bill, bean);
                    });
                }
            } else {
                ToastUtil.showToast("当前航班已无运单任务可分配");
            }
        }
    }

    @Override
    public void existResult(MyAgentListBean existBean) {

    }

    @Override
    public void addInfoResult(MyAgentListBean result) {

    }
}
