package qx.app.freight.qxappfreight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.InternationalCargoAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.FlightLuggageBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.InternationalCargoReportContract;
import qx.app.freight.qxappfreight.contract.ScanScooterCheckUsedContract;
import qx.app.freight.qxappfreight.presenter.InternationalCargoReportPresenter;
import qx.app.freight.qxappfreight.presenter.ScanScooterCheckUsedPresenter;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

/**
 * 国际货物提交界面
 */
public class InternationalCargoListActivity extends BaseActivity implements InternationalCargoReportContract.internationalCargoReportView, ScanScooterCheckUsedContract.ScanScooterCheckView {

    @BindView(R.id.flight_id)
    TextView flightId;
    @BindView(R.id.tv_flight_type)
    TextView tvFlightType;
    @BindView(R.id.tv_flight2_1)
    TextView tvFlight21;
    @BindView(R.id.tv_flight2_2)
    TextView tvFlight22;
    @BindView(R.id.ll_flight_2)
    LinearLayout llFlight2;
    @BindView(R.id.tv_flight3_1)
    TextView tvFlight31;
    @BindView(R.id.tv_flight3_2)
    TextView tvFlight32;
    @BindView(R.id.tv_flight3_3)
    TextView tvFlight33;
    @BindView(R.id.ll_flight_3)
    LinearLayout llFlight3;
    @BindView(R.id.tv_flight4_1)
    TextView tvFlight41;
    @BindView(R.id.tv_flight4_2)
    TextView tvFlight42;
    @BindView(R.id.tv_flight4_3)
    TextView tvFlight43;
    @BindView(R.id.tv_flight4_4)
    TextView tvFlight44;
    @BindView(R.id.ll_flight_4)
    LinearLayout llFlight4;
    @BindView(R.id.tv_flight_place)
    TextView tvFlightPlace;
    @BindView(R.id.tv_arrive_time)
    TextView tvArriveTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.mfrv_receive_good)
    SlideRecyclerView mSlideRV;
    @BindView(R.id.image_scan)
    ImageView imageScan;
    @BindView(R.id.ll_add)
    LinearLayout llAdd;
    @BindView(R.id.btn_next)
    Button btnNext;

    private InternationalCargoAdapter mAdapter;
    private List<TransportTodoListBean> mList;
    private CustomToolbar toolbar;

    FlightLuggageBean flightBean;
    private int flag = 0;
    private String mScooterCode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_international_cargo_list;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        flightBean = (FlightLuggageBean) intent.getSerializableExtra("flightBean");
        setToolbarShow(View.VISIBLE);
        toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "国际货物上报");

        initView();
    }

    private void initView() {

        mSlideRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mList = new ArrayList<>();
        mAdapter = new InternationalCargoAdapter(mList);
        mAdapter.setOnDeleteClickListener(new InternationalCargoAdapter.OnDeleteClickLister() {
            @Override
            public void onDeleteClick(View view, int position) {
                mSlideRV.closeMenu();
                mList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
        });
        mSlideRV.setAdapter(mAdapter);

        //初始化ui界面
        flightId.setText(flightBean.getFlightNo());
        tvFlightType.setText(flightBean.getAircraftNo());
        tvFlightPlace.setText(flightBean.getSeat());
        tvArriveTime.setText(String.format(getString(R.string.format_arrive_info), TimeUtils.date2Tasktime3(flightBean.getScheduleTime()), TimeUtils.getDay((flightBean.getScheduleTime()))));
        //显示航线，2条 3条 4条
        if (flightBean.getFlightCourseByAndroid() !=null && flightBean.getFlightCourseByAndroid().size()>1)
        switch (flightBean.getItemType()) {
            case 2:
                llFlight2.setVisibility(View.VISIBLE);
                tvFlight21.setText(flightBean.getFlightCourseByAndroid().get(0));
                tvFlight22.setText(flightBean.getFlightCourseByAndroid().get(1));

                break;
            case 3:
                llFlight3.setVisibility(View.VISIBLE);
                tvFlight31.setText(flightBean.getFlightCourseByAndroid().get(0));
                tvFlight32.setText(flightBean.getFlightCourseByAndroid().get(1));
                tvFlight33.setText(flightBean.getFlightCourseByAndroid().get(2));
                break;
            case 4:
                llFlight4.setVisibility(View.VISIBLE);
                tvFlight41.setText(flightBean.getFlightCourseByAndroid().get(0));
                tvFlight42.setText(flightBean.getFlightCourseByAndroid().get(1));
                tvFlight43.setText(flightBean.getFlightCourseByAndroid().get(2));
                tvFlight44.setText(flightBean.getFlightCourseByAndroid().get(3));
                break;
        }
    }

    @OnClick({R.id.ll_add, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_add:
                mSlideRV.closeMenu();
                ScanManagerActivity.startActivity(this);
                break;
            case R.id.btn_next:
                mSlideRV.closeMenu();
                submitScooter();
                break;
        }
    }

    /**
     * 添加板车
     *
     * @param mScooterCode 板车号
     */
    private void isIncludeScooterCode(String mScooterCode) {
        if (mList.size() > 0) {
            for (TransportTodoListBean item : mList) {
                if (item.getTpScooterCode().equals(mScooterCode)) {
                    ToastUtil.showToast("该板已扫！");
                    return;
                }
            }
        }
        BaseFilterEntity entity = new BaseFilterEntity();
        MyAgentListBean myAgentListBean = new MyAgentListBean();
        myAgentListBean.setScooterCode(mScooterCode);
        entity.setCurrent(1);
        entity.setSize(10);
        entity.setFilter(myAgentListBean);
        mPresenter = new InternationalCargoReportPresenter(this);
        ((InternationalCargoReportPresenter) mPresenter).scooterInfoList(entity);
    }

    //提交数据
    private void submitScooter() {

        for (TransportTodoListBean item : mList) {
            item.setBaggageSubOperator(UserInfoSingle.getInstance().getUserId());
            item.setBaggageSubTerminal(UserInfoSingle.getInstance().getUsername());
            item.setBaggageSubUserName(DeviceInfoUtil.getDeviceInfo(this).get("deviceId"));
        }
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        mPresenter = new InternationalCargoReportPresenter(this);
        ((InternationalCargoReportPresenter) mPresenter).internationalCargoReport(json);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if (result.getFunctionFlag().equals("InternationalCargoListActivity")) {
            //板车号
            mScooterCode = result.getData();
            if (!"".equals(mScooterCode)) {
                checkScooterCode(mScooterCode);
            } else {
                ToastUtil.showToast("扫码数据为空请重新扫码");
            }
        }
    }

    @Override
    public void internationalCargoReportResult(String result) {
        ToastUtil.showToast("提交成功");
        finish();
    }

    @Override
    public void scooterInfoListResult(List<ScooterInfoListBean> scooterInfoListBeans) {
        if (scooterInfoListBeans != null && scooterInfoListBeans.size() > 0) {
            TransportTodoListBean bean = new TransportTodoListBean();
            bean.setTpScooterCode(scooterInfoListBeans.get(0).getScooterCode());
            bean.setTpScooterType(scooterInfoListBeans.get(0).getScooterType() + "");

            bean.setFlightId(flightBean.getFlightId());
            bean.setFlightNo(flightBean.getFlightNo());
            bean.setTpFlightLocate(flightBean.getSeat());
            bean.setTpFlightTime(flightBean.getScheduleTime());
            bean.setFlightInfoId(flightBean.getId());
            bean.setAsFlightId(flightBean.getSuccessionId());
            bean.setTpFlightType(flightBean.getTpFlightType());
            bean.setFlightIndicator("I");

            mList.add(bean);
            mAdapter.notifyDataSetChanged();

        } else {
            ToastUtil.showToast("无该板信息");
        }
    }

    @Override
    public void toastView(String error) {

    }

    @Override
    public void showNetDialog() {
        showProgessDialog("板车数据提交中……");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.SCAN_RESULT == resultCode) {
            mScooterCode = data.getStringExtra(Constants.SACN_DATA);
            checkScooterCode(mScooterCode);

        } else {
            Log.e("resultCode", "收货页面不是200");
        }
    }

    /**检测板车号是否可用
     *
     * @param scooterCode
     */
    private void checkScooterCode(String scooterCode){
        mPresenter = new ScanScooterCheckUsedPresenter(this);
        ((ScanScooterCheckUsedPresenter) mPresenter).checkScooterCode(scooterCode);
    }

    @Override
    public void checkScooterCodeResult(BaseEntity<Object> result) {
        if ("200".equals(result.getStatus())) {
            isIncludeScooterCode(mScooterCode);
        } else {
            ToastUtil.showToast("操作不合法，不能重复扫描");
        }
    }
}
