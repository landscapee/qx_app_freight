package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.PullGoodsInfoAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.PullGoodsInfoBean;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.contract.PullGoodsReportContract;
import qx.app.freight.qxappfreight.presenter.PullGoodsReportPresenter;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.utils.PushDataUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 拉货上报页面
 */
public class PullGoodsReportActivity extends BaseActivity implements PullGoodsReportContract.pullGoodsView {
    @BindView(R.id.tv_flight_info)
    TextView mTvFlightInfo;
    @BindView(R.id.tv_seat)
    TextView mTvSeat;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.tv_fly_time)
    TextView mTvFlyTime;
    @BindView(R.id.tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.rv_info)
    RecyclerView mRvGoods;
    @BindView(R.id.btn_commit)
    Button mBtnCommit;
    private PullGoodsInfoAdapter mGoodsAdapter;
    private String mCurrentTaskId;
    private PullGoodsInfoBean mPullBean = new PullGoodsInfoBean();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {
        PushDataUtil.handlePushInfo(result, mCurrentTaskId, this);
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
        LoadAndUnloadTodoBean mData = (LoadAndUnloadTodoBean) getIntent().getSerializableExtra("plane_info");
        //是否是连班航班
        boolean mIsKeepOnTask = mData.getMovement() == 4;
        mCurrentTaskId = mIsKeepOnTask ? mData.getRelateInfoObj().getTaskId() : mData.getTaskId();
        mTvFlightInfo.setText(mIsKeepOnTask ? "航班号：" + mData.getRelateInfoObj().getFlightNo() : "航班号：" + mData.getFlightNo());
        mTvSeat.setText(mIsKeepOnTask ? "跑道：" + mData.getRelateInfoObj().getSeat() : "跑道：" + mData.getSeat());
        SimpleDateFormat sdfYmd = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        SimpleDateFormat sdfHm = new SimpleDateFormat("HH:mm", Locale.CHINESE);
        mTvDate.setText("日期：" + sdfYmd.format(mIsKeepOnTask ? mData.getRelateInfoObj().getScheduleTime() : mData.getScheduleTime()));
        mTvFlyTime.setText("计划时间：" + sdfHm.format(mIsKeepOnTask ? mData.getRelateInfoObj().getScheduleTime() : mData.getScheduleTime()));
        mPresenter = new PullGoodsReportPresenter(PullGoodsReportActivity.this);
        ((PullGoodsReportPresenter) mPresenter).getPullGoodsInfo(getIntent().getStringExtra("flight_info_id"));
        mBtnCommit.setClickable(false);
        mTabLayout.addTab(mTabLayout.newTab().setText("板车拉下"));
        mTabLayout.addTab(mTabLayout.newTab().setText("运单拉下"));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {//选中板车下拉
                    mGoodsAdapter = new PullGoodsInfoAdapter(mPullBean.getPullScooters());
                } else {//选中运单下拉
                    mGoodsAdapter = new PullGoodsInfoAdapter(mPullBean.getPullWaybills());
                }
                mRvGoods.setAdapter(mGoodsAdapter);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mBtnCommit.setOnClickListener(v -> {
            boolean infoFull = true;
            for (PullGoodsInfoBean.PullScootersBean  scootersBean:mPullBean.getPullScooters()){
                if (!scootersBean.isChecked()){
                    infoFull=false;
                    break;
                }
            }
            for (PullGoodsInfoBean.PullWaybillsBean  bill:mPullBean.getPullWaybills()){
                if (!bill.isChecked()){
                    infoFull=false;
                    break;
                }
            }
            boolean canCommit=false;
            for (PullGoodsInfoBean.PullScootersBean  scootersBean:mPullBean.getPullScooters()){
                if (scootersBean.getStatus()==0){
                    canCommit=true;
                    break;
                }
            }
            for (PullGoodsInfoBean.PullWaybillsBean  bill:mPullBean.getPullWaybills()){
                if (bill.getStatus()==0){
                    canCommit=true;
                    break;
                }
            }
            //判断所有数据都打勾
            if (canCommit) {
                if (infoFull) {
                    mPresenter = new PullGoodsReportPresenter(PullGoodsReportActivity.this);
                    ((PullGoodsReportPresenter) mPresenter).pullGoodsInfoCommit(mPullBean);
                } else {
                    ToastUtil.showToast("数据选择不完整，请检查");
                }
            }else {
                ToastUtil.showToast("所有数据都已经提交，不能重复提交");
            }
        });
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
    public void getPullGoodsInfoResult(PullGoodsInfoBean result) {
        mPullBean = result;
        mRvGoods.setLayoutManager(new LinearLayoutManager(this));
        mGoodsAdapter = new PullGoodsInfoAdapter(mPullBean.getPullScooters());
        mRvGoods.setAdapter(mGoodsAdapter);
    }

    @Override
    public void pullGoodsInfoCommitResult(String result) {
        Log.e("tag", "result=======" + result);
        ToastUtil.showToast("拉货上报提交成功");
        finish();
    }
}
