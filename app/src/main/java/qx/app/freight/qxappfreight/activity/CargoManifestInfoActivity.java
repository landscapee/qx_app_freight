package qx.app.freight.qxappfreight.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import qx.app.freight.qxappfreight.adapter.ManifestWaybillListAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.ManifestMainBean;
import qx.app.freight.qxappfreight.bean.ManifestScooterListBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightAllReportInfo;
import qx.app.freight.qxappfreight.bean.response.LastReportInfoListBean;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.contract.AuditManifestContract;
import qx.app.freight.qxappfreight.contract.GetLastReportInfoContract;
import qx.app.freight.qxappfreight.presenter.AuditManifestPresenter;
import qx.app.freight.qxappfreight.presenter.GetLastReportInfoPresenter;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.FlightInfoLayout;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/****
 * 货邮舱单详情页面
 */
public class CargoManifestInfoActivity extends BaseActivity implements MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter, GetLastReportInfoContract.getLastReportInfoView, AuditManifestContract.auditManifestView {
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
    RecyclerView mRvData;//货邮舱单信息列表
    @BindView(R.id.bt_shifang)
    Button mBtShifang;    //释放
    @BindView(R.id.btn_print)
    Button mBtPrint;    //打印

    @BindView(R.id.sr_refush)
    SwipeRefreshLayout mSrRefush;


    private LoadAndUnloadTodoBean mBaseData;
    private List<ManifestScooterListBean.WaybillListBean> mList = new ArrayList<>();

    private String mId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_cargo_manifest_info;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", v -> finish());
        toolbar.setMainTitle(Color.WHITE, "货邮舱单详情");
        mBaseData = (LoadAndUnloadTodoBean) getIntent().getSerializableExtra("data");
        mTvFlightNumber.setText(mBaseData.getFlightNo());
        mTvPlaneInfo.setText(mBaseData.getAircraftno());
        FlightInfoLayout layout = new FlightInfoLayout(this, mBaseData.getFlightInfoList());
        LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLlContainer.removeAllViews();
        mLlContainer.addView(layout, paramsMain);
        mTvSeat.setText(mBaseData.getSeat());
        mTvTakeOff.setText(StringUtil.getTimeTextByRegix(mBaseData.getEtd(), "HH:mm"));
        mTvFallDown.setText(StringUtil.getTimeTextByRegix(mBaseData.getAta(), "HH:mm"));
        mTvDate.setText(StringUtil.getTimeTextByRegix(mBaseData.getScheduleTime(), "yyyy-MM-dd"));
        mRvData.setLayoutManager(new LinearLayoutManager(this));
//        mRvData.setRefreshListener(this);
//        mRvData.setOnRetryLisenter(this);
//        mRvData.setRefreshStyle(false);
        loadData();
        //释放
        mBtShifang.setOnClickListener(v -> {
            mPresenter = new AuditManifestPresenter(this);
            BaseFilterEntity entity = new BaseFilterEntity();
            entity.setReportInfoId(mId);
            entity.setOperationUser(UserInfoSingle.getInstance().getUserId());
            entity.setAuditType("2");
            entity.setTaskId(mBaseData.getTaskId());
            entity.setReturnReason("手持端退回请求");
            ((AuditManifestPresenter) mPresenter).auditManifest(entity);

        });
        mBtPrint.setOnClickListener(v -> {
            ToastUtil.showToast("该功能正处于研发过程中……");
        });
        mSrRefush.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }

    private void loadData() {
        mPresenter = new GetLastReportInfoPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setFlightId(mBaseData.getFlightId());
        //货邮舱单
        entity.setDocumentType(1);
        entity.setSort(1);
        ((GetLastReportInfoPresenter) mPresenter).getLastReportInfo(entity);
    }

    @Override
    public void getLastReportInfoResult(List<FlightAllReportInfo> results) {
        if (results != null && results.get(0) != null) {
            FlightAllReportInfo result = results.get(0);
            mId = result.getId();
            mTvVersion.setText("版本号" + result.getVersion());
//        mRvData.finishRefresh();
            mSrRefush.setRefreshing(false);
            mList.clear();
            Gson mGson = new Gson();
            ManifestMainBean[] datas = mGson.fromJson(result.getContent(), ManifestMainBean[].class);
            for (ManifestMainBean data : datas) {
                for (ManifestMainBean.CargosBean bean : data.getCargos()) {
                    for (ManifestScooterListBean data1 : bean.getScooters()) {
                        mList.addAll(data1.getWaybillList());
                    }
                }
            }
            ManifestScooterListBean.WaybillListBean title = new ManifestScooterListBean.WaybillListBean();
            title.setWaybillCode("运单号");
            title.setWeight("重量");
            title.setNumber("件数");
            title.setCargoCn("货物名称");
            title.setVolume("体积");
            mList.add(0, title);
            ManifestWaybillListAdapter adapter = new ManifestWaybillListAdapter(mList);
            mRvData.setAdapter(adapter);
        }
    }

    @Override
    public void toastView(String error) {
        mSrRefush.setRefreshing(false);
//        mRvData.finishRefresh();
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
    public void auditManifestResult(String result) {
        ToastUtil.showToast(result);
        finish();
    }
}
