package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.FlightFinishAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightInfoBean;
import qx.app.freight.qxappfreight.bean.response.FlightServiceBean;
import qx.app.freight.qxappfreight.bean.response.MilepostBean;
import qx.app.freight.qxappfreight.contract.FlightInfoContract;
import qx.app.freight.qxappfreight.presenter.FlightInfoPresenter;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

public class DynamicDetailsAcitvity extends BaseActivity implements FlightInfoContract.flightInfoView, SwipeRefreshLayout.OnRefreshListener, FlightFinishAdapter.onItemClickLinstener {

    @BindView(R.id.iv_jiwei)
    ImageView ivJiwei;
    @BindView(R.id.tv_airline_company)
    TextView tvAirlinceCompany;
    @BindView(R.id.tv_originating)
    TextView tvOriginating;
    @BindView(R.id.tv_destination)
    TextView tvDestination;
    @BindView(R.id.iv_seat)
    TextView ivSeat;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_luggage)
    TextView tvLuggage;
    @BindView(R.id.iv_runway)
    TextView ivRunway;
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;
    @BindView(R.id.tv_visibility)
    TextView tvVisibility;
    @BindView(R.id.tv_windspeed)
    TextView tvWindspeed;
    @BindView(R.id.tv_windgrade)
    TextView tvWindgrade;
    @BindView(R.id.tv_releasetime)
    TextView tvReleasetime;
    @BindView(R.id.tv_flight)
    TextView tvFlight;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.progress)
    ProgressBar mProgress;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.lv_service)
    ListView mListview;
    @BindView(R.id.swipe_service)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.tv_seat_nub)
    TextView tvSeatNub;
    @BindView(R.id.tv_luggage_nub)
    TextView tvLuggageNub;
    @BindView(R.id.tv_runway_nub)
    TextView tvRunwayNub;


    private int flightId;
    private String flightNo;
    private List <MilepostBean> mList = new ArrayList <>();
    private FlightFinishAdapter mAdapter;

    public static void startActivity(Activity context, int flightId, String flightNo) {
        Intent intent = new Intent(context, DynamicDetailsAcitvity.class);
        intent.putExtra("flightId", flightId);
        intent.putExtra("flightNo", flightNo);
        context.startActivityForResult(intent, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_dynamic_details;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        flightId = getIntent().getIntExtra("flightId", 0);
        flightNo = getIntent().getStringExtra("flightNo");

        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        if (!StringUtil.isEmpty(flightNo))
            toolbar.setMainTitle(Color.WHITE, flightNo);
        else
            toolbar.setMainTitle(Color.WHITE, "???????????????");

        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());

        initView();
        initData();


    }

    private void initData() {
        tvFlight.setText(flightNo + "");
        mPresenter = new FlightInfoPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setFlightId(String.valueOf(flightId));
        ((FlightInfoPresenter) mPresenter).flightInfo(entity);

    }

    private void getMilepostData() {
        mPresenter = new FlightInfoPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setFlightId(String.valueOf(flightId));
        ((FlightInfoPresenter) mPresenter).getMilepostData(entity);
    }

    private void initView() {
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    public void flightInfoResult(FlightInfoBean flightInfoBean) {
        getMilepostData();
        if (null != flightInfoBean) {
            //????????????
            tvAirlinceCompany.setText(flightInfoBean.getAirlines());
            // ????????????
            tvOriginating.setText(flightInfoBean.getRoutes().get(0));
            //????????????
            int despost = flightInfoBean.getRoutes().size();
            if (despost > 1)
                tvDestination.setText(flightInfoBean.getRoutes().get(despost - 1));
            // ?????? A- -  ???????????? - ????????????    //??????????????? ???????????????
            if ("A".equals(flightInfoBean.getMovement())) {
                tvLuggage.setText("??????");
                //?????????
                tvLuggageNub.setText(StringUtil.isEmpty(flightInfoBean.getCarousel()) ? "- -" : flightInfoBean.getCarousel());
                tvStartTime.setText(String.format(getString(R.string.format_dynamic_info)
                        //????????????
                        , TimeUtils.date2Tasktime3(flightInfoBean.getEtd())
                        //????????????
                        , TimeUtils.date2Tasktime3(flightInfoBean.getAtd())));

                tvEndTime.setText(String.format(getString(R.string.format_dynamic_info)
                        //????????????
                        , TimeUtils.date2Tasktime3(flightInfoBean.getEta())
                        //????????????
                        , TimeUtils.date2Tasktime3(flightInfoBean.getAta())));
                //  ?????? D ???????????? -????????????      - -
            } else if ("D".equals(flightInfoBean.getMovement())) {
                tvLuggage.setText("?????????");
                //????????????
                tvLuggageNub.setText(StringUtil.isEmpty(flightInfoBean.getGate()) ? "- -" : flightInfoBean.getGate());
                tvStartTime.setText(String.format(getString(R.string.format_dynamic_info)
                        //????????????
                        , TimeUtils.date2Tasktime3(flightInfoBean.getEta())
                        //????????????
                        , TimeUtils.date2Tasktime3(flightInfoBean.getAta())));

                tvEndTime.setText(String.format(getString(R.string.format_dynamic_info)
                        //????????????
                        , TimeUtils.date2Tasktime3(flightInfoBean.getEtd())
                        //????????????
                        , TimeUtils.date2Tasktime3(flightInfoBean.getEta())));
            }
            //??????
            tvSeatNub.setText(StringUtil.isEmpty(flightInfoBean.getSeat()) ? "- -" : flightInfoBean.getSeat());
            if (flightInfoBean.getWeather() != null) {
                //??????
                tvRunwayNub.setText(StringUtil.isEmpty(flightInfoBean.getWeather().getRvr()) ? "- -" : flightInfoBean.getWeather().getRvr());
                //??????
                tvTemperature.setText(flightInfoBean.getWeather().getTemperature() + "???");
                //?????????
                tvVisibility.setText(StringUtil.isEmpty(flightInfoBean.getWeather().getVisib()) ? "- -" : flightInfoBean.getWeather().getVisib());
                //??????
                tvWindspeed.setText(StringUtil.isEmpty(flightInfoBean.getWeather().getWindSpeed()) ? "- -" : flightInfoBean.getWeather().getWindSpeed());
                //??????
                tvWindgrade.setText(StringUtil.isEmpty(flightInfoBean.getWeather().getWindPower()) ? "- -" : flightInfoBean.getWeather().getWindPower());
                //????????????
                tvReleasetime.setText("????????????:" + flightInfoBean.getWeather().getReportTime());
            }

        }

    }

    @Override
    public void getMilepostDataResult(FlightServiceBean flightServiceBean) {
        if (null != flightServiceBean) {
            if (mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(false);
            }
            if (!StringUtil.isEmpty(flightServiceBean.getCompletionDegree())) {
                //??????
                tvProgress.setText(flightServiceBean.getCompletionDegree());
                Double percent = Double.parseDouble(flightServiceBean.getCompletionDegree().substring(0, flightServiceBean.getCompletionDegree().lastIndexOf("%")));
                int per = Integer.parseInt(new java.text.DecimalFormat("0").format(percent));
                mProgress.setProgress(per);
            } else {
                mProgress.setProgress(0);
                tvProgress.setText("0%");
            }
        }
        mList.clear();
        mList.addAll(flightServiceBean.getFlightMilepostList());
        if (mAdapter == null) {
            mAdapter = new FlightFinishAdapter(this, mList, this);
            mListview.setAdapter(mAdapter);
        } else
            mAdapter.notifyDataSetChanged();
    }

    @Override
    public void toastView(String error) {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        if (0 != flightId) {
            BaseFilterEntity entity = new BaseFilterEntity();
            entity.setFlightId(String.valueOf(flightId));
            ((FlightInfoPresenter) mPresenter).getMilepostData(entity);
        }
    }

    @Override
    public void clickItem(int position) {

    }
}

