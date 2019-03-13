package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.ExceptionReportEntity;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.contract.PullGoodsReportContract;
import qx.app.freight.qxappfreight.contract.ScanScooterContract;
import qx.app.freight.qxappfreight.presenter.PullGoodsReportPresenter;
import qx.app.freight.qxappfreight.presenter.ScanScooterPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

/**
 * 拉货上报页面
 */
public class PullGoodsReportActivity extends BaseActivity implements ScanScooterContract.scanScooterView, PullGoodsReportContract.pullGoodsView{
    @BindView(R.id.tv_flight_info)
    TextView mTvFlightInfo;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.srv_goods_info)
    SlideRecyclerView mSrvGoods;
    @BindView(R.id.ll_add_scan)
    LinearLayout mLlAddScan;
    @BindView(R.id.btn_commit)
    Button mBtnCommit;
    private List<TransportTodoListBean> mList = new ArrayList<>();
    private PullGoodsInfoAdapter mGoodsAdapter;
    private String[] mInfoList;
    private SimpleDateFormat mSdf=new SimpleDateFormat("yyyy-MM-dd",Locale.CHINESE);

    @Override
    public int getLayoutId() {
        return R.layout.activity_pull_goods_report;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", v -> finish());
        toolbar.setMainTitle(Color.WHITE, "拉货上报");
        String info=getIntent().getStringExtra("plane_info");
        mInfoList=info.split("\\*");
        Log.e("tagId","id========="+mInfoList[7]);
        mTvFlightInfo.setText(mInfoList[0]);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        mTvDate.setText(sdf.format(new Date()));
        mLlAddScan.setOnClickListener(v -> ScanManagerActivity.startActivity(PullGoodsReportActivity.this, "PullGoodsReportActivity"));
        mBtnCommit.setClickable(false);
        mSrvGoods.setLayoutManager(new LinearLayoutManager(this));
        mGoodsAdapter = new PullGoodsInfoAdapter(mList);
        mSrvGoods.setAdapter(mGoodsAdapter);
        mGoodsAdapter.setOnDeleteClickListener((view, position) -> {
                    if (mList.size() == 5) {
                        mLlAddScan.setVisibility(View.GONE);
                    }
                    mList.remove(position);
                    mSrvGoods.closeMenu();
                    mGoodsAdapter.notifyDataSetChanged();
                    upDataBtnStatus();
                }
        );
        mBtnCommit.setOnClickListener(v -> {
            if (mList.size()==0){
                ToastUtil.showToast("请扫描添加板车数据！");
            }else {
                mPresenter = new PullGoodsReportPresenter(PullGoodsReportActivity.this);
                ExceptionReportEntity entity = new ExceptionReportEntity();
                entity.setFlightId(Long.valueOf(mInfoList[7]));
                entity.setFlightNum(mInfoList[0]);
                entity.setReType(3);
                entity.setReOperator(UserInfoSingle.getInstance().getUserId());
                entity.setRpDate(mSdf.format(new Date()));
                entity.setScooters(mList);
                ((PullGoodsReportPresenter) mPresenter).pullGoodsReport(entity);
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if ("PullGoodsReportActivity".equals(result.getFunctionFlag())) {
            //根据扫一扫获取的板车信息查找板车内容
            addScooterInfo(result.getData());
        }
    }

    private void addScooterInfo(String scooterCode) {
        Log.e("scooterCode", scooterCode);
        if (!"".equals(scooterCode)) {
            mPresenter = new ScanScooterPresenter(this);
            TransportTodoListBean mainIfos = new TransportTodoListBean();
            mainIfos.setTpScooterCode(scooterCode);
            mainIfos.setTpOperator(UserInfoSingle.getInstance().getUserId());
            mainIfos.setDtoType(8);
            ((ScanScooterPresenter) mPresenter).scanScooter(mainIfos);
        } else
            ToastUtil.showToast(this, "扫描结果为空请重新扫描");
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
            mList.clear();
            mList.addAll(result);
            mGoodsAdapter.notifyDataSetChanged();
            if (mList.size() >= 5) {
                ToastUtil.showToast(this, "最多一次拉5板货");
                mLlAddScan.setVisibility(View.GONE);
            }
            upDataBtnStatus();
        } else
            ToastUtil.showToast(this, "返回数据为空");
    }

    /**
     * 开始按钮是否可以点击
     */
    private void upDataBtnStatus() {
        if (mList.size() > 0) {
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
        Log.e("tag","result======="+result);
        ToastUtil.showToast("拉货上报成功");
        finish();
    }
}
