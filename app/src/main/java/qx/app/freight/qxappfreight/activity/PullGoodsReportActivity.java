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

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.PullGoodsInfoAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.GetWaybillInfoByIdDataBean;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.ExceptionReportEntity;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.GetWayBillInfoByIdDataContract;
import qx.app.freight.qxappfreight.contract.PullGoodsReportContract;
import qx.app.freight.qxappfreight.contract.ScanScooterCheckUsedContract;
import qx.app.freight.qxappfreight.contract.ScanScooterContract;
import qx.app.freight.qxappfreight.contract.ScooterInfoListContract;
import qx.app.freight.qxappfreight.presenter.GetWayBillInfoByIdDataPresenter;
import qx.app.freight.qxappfreight.presenter.PullGoodsReportPresenter;
import qx.app.freight.qxappfreight.presenter.ScanScooterCheckUsedPresenter;
import qx.app.freight.qxappfreight.presenter.ScanScooterPresenter;
import qx.app.freight.qxappfreight.presenter.ScooterInfoListPresenter;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.utils.PushDataUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

/**
 * 拉货上报页面
 */
public class PullGoodsReportActivity extends BaseActivity implements ScanScooterContract.scanScooterView, ScooterInfoListContract.scooterInfoListView, PullGoodsReportContract.pullGoodsView, ScanScooterCheckUsedContract.ScanScooterCheckView, GetWayBillInfoByIdDataContract.getWayBillInfoByIdDataView {
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
    private SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
    private int mDeletePos = -1;        //左滑删除时的下标标记值
    private boolean mIsScanBill = false;//是否是扫描运单拉下，默认是扫板
    private String mCurrentTaskId;
    private List<String> mTpScooterCodeList = new ArrayList<>();
    private TransportTodoListBean mNewBillBean;
    private String mWaybillCode;
    private Map<String, GetWaybillInfoByIdDataBean.DataMainBean> mCodeMap = new HashMap<>();
    private LoadAndUnloadTodoBean mData;
    private String mMainFlightId;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {
        PushDataUtil.handlePushInfo(result, mCurrentTaskId, this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String result) {
        if (result != null && result.equals(mData.getFlightId())) {
            finish();
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
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", v -> finish());
        toolbar.setMainTitle(Color.WHITE, "拉货上报");
        mData = (LoadAndUnloadTodoBean) getIntent().getSerializableExtra("plane_info");
        //是否是连班航班
        boolean mIsKeepOnTask = mData.getMovement() == 4;
        mMainFlightId = mIsKeepOnTask ? mData.getRelateInfoObj().getFlightId() : mData.getFlightId();
        mPresenter = new ScanScooterPresenter(this);
        String userId=UserInfoSingle.getInstance().getUserId();
        ((ScanScooterPresenter) mPresenter).scooterWithUser(userId, mMainFlightId);
        mCurrentTaskId = mIsKeepOnTask ? mData.getRelateInfoObj().getTaskId() : mData.getTaskId();
        mTvFlightInfo.setText(mIsKeepOnTask ? mData.getRelateInfoObj().getFlightNo() : mData.getFlightNo());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        mTvDate.setText(sdf.format(new Date()));
        mLlAddScan.setOnClickListener(v -> {
            ScanManagerActivity.startActivity(PullGoodsReportActivity.this, "PullGoodsReportActivity");
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
                    entity.setLoadingListId(getIntent().getStringExtra("id"));
                    entity.setFlightId(Long.valueOf(mMainFlightId));
                    entity.setFlightNum(mData.getFlightNo());
                    entity.setReType(3);
                    entity.setReOperator(UserInfoSingle.getInstance().getUserId());
                    entity.setRpDate(mSdf.format(new Date()));
                    entity.setScooters(mPullBoardList);
                    entity.setWaybillScooters(mPullBillList);
                    entity.setSeat(mData.getSeat());
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
                        mTpScooterCodeList.remove(mPullBoardList.get(position).getTpScooterCode());
                    } else {
                        //删除扫描的运单上拉数据
                        scanScooterDeleteResult("");
                        mTpScooterCodeList.remove(mPullBillList.get(position).getTpScooterCode());
                    }
                }
        );
        mGoodsAdapter.setOnItemClickListener((adapter, view, position) -> {
        });
    }

    private String mNowScooterCode;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if ("PullGoodsReportActivity".equals(result.getFunctionFlag())) {
            //根据扫一扫获取的板车信息查找板车内容
            if (!mTpScooterCodeList.contains(result.getData())) {
                if (mIsScanBill) {
                    mNowScooterCode = result.getData();
                    mPresenter = new ScanScooterCheckUsedPresenter(this);
                    ((ScanScooterCheckUsedPresenter) mPresenter).checkScooterCode(mNowScooterCode);
                } else {
                    mNowScooterCode = result.getData();
                    addScooterInfo(result.getData());
                }
            } else {
                ToastUtil.showToast("操作不合法，不能重复扫描");
            }
        } else if ("PullGoodsReportActivity_scan_waybill".equals(result.getFunctionFlag())) {
            mPresenter = new GetWayBillInfoByIdDataPresenter(this);
            mWaybillCode = result.getData();
            ((GetWayBillInfoByIdDataPresenter) mPresenter).getWayBillInfoByCode(result.getData());
        }
    }

    private void setBillDataList(String billCode, TransportTodoListBean bean) {
        bean.setBillCode(billCode);
        bean.setBillNumber(billCode);
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
                        GetWaybillInfoByIdDataBean.DataMainBean billBean = mCodeMap.get(billCode);
                        int leftNumber = (int) billBean.getTotalNumber();//根据运单code获取该运单允许分装的剩余最大件数
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
                        billBean.setBillItemNumber((int) (billBean.getTotalNumber() - usedNumber));
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
                        GetWaybillInfoByIdDataBean.DataMainBean billBean = mCodeMap.get(billCode);
                        double leftWeight = billBean.getTotalWeight();//根据运单code获取该运单允许分装的剩余最大质量
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
                        billBean.setBillItemWeight(billBean.getTotalWeight() - usedWeight);
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
        setDeleteListener();
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
                mainIfos.setFlightId(mMainFlightId);
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
            ((ScanScooterPresenter) mPresenter).scooterWithUser(UserInfoSingle.getInstance().getUserId(), mMainFlightId);
        }
    }

    @Override
    public void scanLockScooterResult(String result) {

    }

    @Override
    public void scooterWithUserResult(List<TransportTodoListBean> result) {
        if (result != null) {
            for (TransportTodoListBean bean : result) {
                mTpScooterCodeList.add(bean.getTpScooterCode());
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
        showProgessDialog("请求中......");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
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
            mNewBillBean = new TransportTodoListBean();
            mNewBillBean.setTpScooterId(entity.getId());
            mNewBillBean.setTpScooterCode(entity.getScooterCode());
            mTpScooterCodeList.add(entity.getScooterCode());
            mNewBillBean.setTpScooterType(entity.getScooterType() + "");
            mNewBillBean.setFlightNo(mData.getFlightNo());
            mNewBillBean.setTpFlightLocate(mData.getSeat());
            mNewBillBean.setTpFlightTime(mData.getScheduleTime());
            mNewBillBean.setTpFregihtSpace("");
            mNewBillBean.setMaxBillWeight(Double.valueOf(entity.getScooterWeight()));
            ScanManagerActivity.startActivity(PullGoodsReportActivity.this, "PullGoodsReportActivity_scan_waybill", "当前航班：" + mData.getFlightNo());
        } else {
            ToastUtil.showToast("板车扫描错误，请检查");
        }
    }

    @Override
    public void existResult(MyAgentListBean existBean) {

    }

    @Override
    public void addInfoResult(MyAgentListBean result) {

    }

    @Override
    public void checkScooterCodeResult(BaseEntity<Object> result) {
        if ("200".equals(result.getStatus())) {
            addScooterInfo(mNowScooterCode);
        } else {
            ToastUtil.showToast("操作不合法，不能重复扫描");
        }
    }

    @Override
    public void getWayBillInfoByCodeResult(GetWaybillInfoByIdDataBean addScooterBean) {
        //异常处理有问题，不能走这里的判断
        if (addScooterBean.getStatus().equals("500")) {
            ToastUtil.showToast("运单号输入错误，请检查！");
        } else {
            mCodeMap.put(mWaybillCode, addScooterBean.getData());
            if (!mMainFlightId.equals(addScooterBean.getData().getFlightId())) {
                ToastUtil.showToast("扫描的运单数据非当前航班，请重试");
            } else {
                if (addScooterBean.getData().getTotalNumber() <= 0 || addScooterBean.getData().getTotalWeight() <= 0) {
                    ToastUtil.showToast("当前运单已全部被拉下");
                } else {
                    mNewBillBean.setTpCargoNumber((int) addScooterBean.getData().getTotalNumber());
                    mNewBillBean.setTpCargoWeight(addScooterBean.getData().getTotalWeight());
                    mNewBillBean.setTpCargoVolume(Double.valueOf(addScooterBean.getData().getTotalVolume()));
                    setBillDataList(mWaybillCode, mNewBillBean);
                }
            }
        }
    }
}
