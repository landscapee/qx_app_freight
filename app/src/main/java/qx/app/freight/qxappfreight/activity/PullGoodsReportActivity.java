package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.PullGoodsInfoAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.LocalBillBean;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.ExceptionReportEntity;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.PullGoodsReportContract;
import qx.app.freight.qxappfreight.contract.ScanScooterContract;
import qx.app.freight.qxappfreight.dialog.ChooseGoodsBillDialog;
import qx.app.freight.qxappfreight.presenter.PullGoodsReportPresenter;
import qx.app.freight.qxappfreight.presenter.ScanScooterPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

/**
 * 拉货上报页面
 */
public class PullGoodsReportActivity extends BaseActivity implements ScanScooterContract.scanScooterView, PullGoodsReportContract.pullGoodsView {
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
    private boolean mIsScanBill=false;//是否是扫描运单拉下，默认是扫板
    private List<LocalBillBean> mBillList=new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_pull_goods_report;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mBillList=getIntent().getParcelableArrayListExtra("bill_list");
        mPresenter = new ScanScooterPresenter(this);
        ((ScanScooterPresenter) mPresenter).scooterWithUser(UserInfoSingle.getInstance().getUserId());
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", v -> finish());
        toolbar.setMainTitle(Color.WHITE, "拉货上报");
        String info = getIntent().getStringExtra("plane_info");
        mInfoList = info.split("\\*");
        mTvFlightInfo.setText(mInfoList[0]);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        mTvDate.setText(sdf.format(new Date()));
        mLlAddScan.setOnClickListener(v -> ScanManagerActivity.startActivity(PullGoodsReportActivity.this, "PullGoodsReportActivity"));
        mBtnCommit.setClickable(false);
        mSrvGoods.setLayoutManager(new LinearLayoutManager(this));
        mGoodsAdapter = new PullGoodsInfoAdapter(mPullBoardList);
        mSrvGoods.setAdapter(mGoodsAdapter);
        mTabLayout.addTab(mTabLayout.newTab().setText("板车拉下"));
        mTabLayout.addTab(mTabLayout.newTab().setText("运单拉下"));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==0){//选中板车下拉
                    mIsScanBill=false;
                    mGoodsAdapter=new PullGoodsInfoAdapter(mPullBoardList);
                }else {//选中运单下拉
                    mIsScanBill=true;
                    mGoodsAdapter=new PullGoodsInfoAdapter(mPullBillList);
                }
                mSrvGoods.setAdapter(mGoodsAdapter);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mGoodsAdapter.setOnDeleteClickListener((view, position) -> {
                    mDeletePos = position;
                    if (!mIsScanBill) {
                        TransportEndEntity transportEndEntity = new TransportEndEntity();
                        transportEndEntity.setId(mPullBoardList.get(position).getId());
                        ((PullGoodsReportPresenter) mPresenter).scanScooterDelete(transportEndEntity);
                    }else {
                        //删除扫描的运单上拉数据
                        scanScooterDeleteResult("");
                    }
                }
        );
        mGoodsAdapter.setOnItemClickListener((adapter, view, position) -> {
        });
        mBtnCommit.setOnClickListener(v -> {
            if (mPullBoardList.size() == 0 || mPullBillList.size() == 0) {
                ToastUtil.showToast("请扫描添加板车数据！");
            } else {
                mPresenter = new PullGoodsReportPresenter(PullGoodsReportActivity.this);
                ExceptionReportEntity entity = new ExceptionReportEntity();
                entity.setFlightId(Long.valueOf(mInfoList[7]));
                entity.setFlightNum(mInfoList[0]);
                entity.setReType(3);
                entity.setReOperator(UserInfoSingle.getInstance().getUserId());
                entity.setRpDate(mSdf.format(new Date()));
                entity.setScooters(mPullBoardList);
                entity.setSeat(mInfoList[5]);
                ((PullGoodsReportPresenter) mPresenter).pullGoodsReport(entity);
            }
        });
    }
    private int mFlag=0;//单数次扫描直接显示假数据，双数次扫描单独加入一条数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if ("PullGoodsReportActivity".equals(result.getFunctionFlag())) {
            //根据扫一扫获取的板车信息查找板车内容
            if (!mIsScanBill) {
                addScooterInfo(result.getData());
            }else {
                if (mFlag%2!=0) {
                    ChooseGoodsBillDialog dialog = new ChooseGoodsBillDialog();
                    List<String> list = new ArrayList<>();
                    for (LocalBillBean billBean:mBillList) {
                        list.add(billBean.getWayBillCode());
                    }
                    dialog.setData(PullGoodsReportActivity.this, list);
                    dialog.show(getSupportFragmentManager(), "123");
                    dialog.setOnBillSelectListener(pos -> {
                        String bill=list.get(pos);
                        setBillDataList(bill);
                    });
                }else {
                    setBillDataList("");
                }
            }
        }
    }

    private void setBillDataList(String bill) {
        if (mFlag%2==0){
            mPullBillList.clear();
        }
        for (int i=0;i<6;i++){
            TransportTodoListBean bean=new TransportTodoListBean();
            bean.setBillNumber(TextUtils.isEmpty(bill)?"运单号测试==="+i:bill);
            bean.setTpScooterId("大板车"+1111+i);
            bean.setTpCargoNumber(11+i);
            bean.setTpCargoVolume(Double.valueOf(333.5+i));
            bean.setTpCargoWeight(999+i);
            bean.setTpFlightNumber("3U999"+i);
            bean.setTpFlightLocate(111+i+"");
            bean.setTpFregihtSpace(i+"H舱位");
            bean.setTpFlightTime(System.currentTimeMillis()+86400000L*i);
            bean.setInfoType(Constants.TYPE_PULL_BILL);
            mPullBillList.add(bean);
        }
        mGoodsAdapter=new PullGoodsInfoAdapter(mPullBillList);
        mSrvGoods.setAdapter(mGoodsAdapter);
        mGoodsAdapter.setOnDeleteClickListener((view, position) -> {
                    mDeletePos = position;
                    if (!mIsScanBill) {
                        TransportEndEntity transportEndEntity = new TransportEndEntity();
                        transportEndEntity.setId(mPullBoardList.get(position).getId());
                        ((PullGoodsReportPresenter) mPresenter).scanScooterDelete(transportEndEntity);
                    }else {
                        //删除扫描的运单上拉数据
                        scanScooterDeleteResult("");
                    }
                }
        );
        mGoodsAdapter.setOnItemClickListener((adapter, view, position) -> {
        });
        mFlag+=1;
    }

    private void addScooterInfo(String scooterCode) {
        if (!TextUtils.isEmpty(scooterCode)) {
            mPresenter = new ScanScooterPresenter(this);
            TransportTodoListBean mainIfos = new TransportTodoListBean();
            mainIfos.setTpScooterCode(scooterCode);
            mainIfos.setTpOperator(UserInfoSingle.getInstance().getUserId());
            mainIfos.setDtoType(8);
            mainIfos.setTpStartLocate("seat");
            ((ScanScooterPresenter) mPresenter).scanScooter(mainIfos);
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
            mPullBoardList.clear();
            mPullBoardList.addAll(result);
            mGoodsAdapter.notifyDataSetChanged();
            upDataBtnStatus();
        } else
            ToastUtil.showToast(this, "返回数据为空");
    }

    /**
     * 开始按钮是否可以点击
     */
    private void upDataBtnStatus() {
        if (mPullBoardList.size() > 0&&mPullBillList.size()>0) {
            mBtnCommit.setClickable(true);
        } else {
            mBtnCommit.setClickable(false);
        }
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
        }else {
            mPullBillList.remove(mDeletePos);
        }
        mSrvGoods.closeMenu();
        mGoodsAdapter.notifyDataSetChanged();
        upDataBtnStatus();
    }
}
