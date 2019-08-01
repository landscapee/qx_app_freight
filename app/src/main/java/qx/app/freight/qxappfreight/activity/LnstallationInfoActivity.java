package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ouyben.empty.EmptyLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.LnstallationListAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.ManifestScooterListBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.LastReportInfoListBean;
import qx.app.freight.qxappfreight.bean.response.LnstallationInfoBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.contract.GetLastReportInfoContract;
import qx.app.freight.qxappfreight.contract.SynchronousLoadingContract;
import qx.app.freight.qxappfreight.presenter.GetLastReportInfoPresenter;
import qx.app.freight.qxappfreight.presenter.SynchronousLoadingPresenter;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.FlightInfoLayout;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

public class LnstallationInfoActivity extends BaseActivity implements MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter, GetLastReportInfoContract.getLastReportInfoView, SynchronousLoadingContract.synchronousLoadingView {

    @BindView(R.id.tv_flight_number)
    TextView mTvFlightNumber;//航班号
    @BindView(R.id.tv_plane_info)
    TextView mTvPlaneInfo;//机型
    @BindView(R.id.ll_flight_info_container)
    LinearLayout mLlContainer;//航线数据控件
    @BindView(R.id.tv_seat)
    TextView mTvSeat;//机位
    @BindView(R.id.tv_time_take_off)
    TextView mTvTakeOff;//起飞时间
    @BindView(R.id.tv_time_fall_down)
    TextView mTvFallDown;//降落时间
    @BindView(R.id.tv_date)
    TextView mTvDate;//日期
    @BindView(R.id.tv_version)
    TextView mTvVersion;//版本号
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mRvData;//货邮舱单信息列表
    @BindView(R.id.bt_sure)
    Button mBtSure;//通知录入
    @BindView(R.id.btn_refuse)
    Button mBtRefuse;//打印


    private TransportDataBase mBaseData;
    private List<LnstallationInfoBean.ScootersBean> mList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_lnstalla_tion_info;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", v -> finish());
        toolbar.setMainTitle(Color.WHITE, "装机单详情");
        mBaseData = (TransportDataBase) getIntent().getSerializableExtra("data");
        mTvFlightNumber.setText(mBaseData.getFlightNo());
        mTvPlaneInfo.setText(mBaseData.getAircraftNo());
        FlightInfoLayout layout = new FlightInfoLayout(this, mBaseData.getFlightCourseByAndroid());
        LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLlContainer.removeAllViews();
        mLlContainer.addView(layout, paramsMain);
        mTvSeat.setText(mBaseData.getSeat());
        mTvTakeOff.setText(StringUtil.getTimeTextByRegix(mBaseData.getEtd(), "HH:mm"));
        mTvFallDown.setText(StringUtil.getTimeTextByRegix(mBaseData.getAta(), "HH:mm"));
        mTvDate.setText(StringUtil.getTimeTextByRegix(mBaseData.getFlightDate(), "yyyy-MM-dd"));
        mTvVersion.setText(mBaseData.getVersion() == null ? "版本号：- -" : "版本号：" + mBaseData.getVersion());
        mRvData.setLayoutManager(new LinearLayoutManager(this));
        mRvData.setRefreshListener(this);
        mRvData.setOnRetryLisenter(this);
        mRvData.setRefreshStyle(false);
        loadData();
        mBtSure.setOnClickListener(v -> {
            mPresenter = new SynchronousLoadingPresenter(this);
            BaseFilterEntity entity = new BaseFilterEntity();
            entity.setFlightInfoId(mBaseData.getFlightId());
            entity.setOperationUser(UserInfoSingle.getInstance().getUserId());
            ((SynchronousLoadingPresenter) mPresenter).synchronousLoading(entity);
        });
    }

    private void loadData() {
        mPresenter = new GetLastReportInfoPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setFlightInfoId(mBaseData.getFlightId());
        //装机单
        entity.setDocumentType(2);
        ((GetLastReportInfoPresenter) mPresenter).getLastReportInfo(entity);
    }

    @Override
    public void getLastReportInfoResult(LastReportInfoListBean result) {
        mRvData.finishRefresh();
        mList.clear();
        Gson mGson = new Gson();
        LnstallationInfoBean[] datas = mGson.fromJson(result.getContent(), LnstallationInfoBean[].class);
        for (LnstallationInfoBean data : datas) {
            for (LnstallationInfoBean.ScootersBean data1 :data.getScooters()){
                mList.add(data1);
            }

        }
        LnstallationInfoBean.ScootersBean title = new LnstallationInfoBean.ScootersBean();
        title.setSuggestRepository("舱位");
        title.setGoodsPosition("货位");
        title.setSerialInd("板车号");
        title.setUldCode("ULD号");
        title.setDest("目的站");
        title.setType("类型");
        title.setActWgt("重量");
        title.setRestrictedCargo("件数");
        title.setSpecialNumber("特货代码");
        mList.add(0, title);
        LnstallationListAdapter adapter = new LnstallationListAdapter(mList);
        mRvData.setAdapter(adapter);
    }

    @Override
    public void toastView(String error) {
        mRvData.finishRefresh();
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
    public void onRetry() {
        showProgessDialog("正在加载数据……");
        new Handler().postDelayed(() -> {
            loadData();
            dismissProgessDialog();
        }, 2000);
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onLoadMore() {
    }

    @Override
    public void synchronousLoadingResult(String result) {
        ToastUtil.showToast(result);
        finish();
    }
}
