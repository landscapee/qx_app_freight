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
import qx.app.freight.qxappfreight.bean.CargoUploadBean;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.LoadingListRequestEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.CargoCabinData;
import qx.app.freight.qxappfreight.bean.response.CargoReportHisBean;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.GetFlightCargoResContract;
import qx.app.freight.qxappfreight.contract.InternationalCargoReportContract;
import qx.app.freight.qxappfreight.contract.ScanScooterCheckUsedContract;
import qx.app.freight.qxappfreight.dialog.ChoseFlightTypeDialog;
import qx.app.freight.qxappfreight.presenter.GetFlightCargoResPresenter;
import qx.app.freight.qxappfreight.presenter.InternationalCargoReportPresenter;
import qx.app.freight.qxappfreight.presenter.ScanScooterCheckUsedPresenter;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

/**
 * 货物提交界面
 */
public class CargoDoneListActivity extends BaseActivity implements InternationalCargoReportContract.internationalCargoReportView, ScanScooterCheckUsedContract.ScanScooterCheckView, GetFlightCargoResContract.getFlightCargoResView {
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
    private List<TransportTodoListBean> mList = new ArrayList<>();
    private CustomToolbar toolbar;
    private List<TransportTodoListBean> flightBean;
    private CargoReportHisBean mData;
    private String mScooterCode;
    private double mBaggageWeight;//行李重量
    private double mGoodsWeight;//货物重量
    private double mMailWeight;//邮件重量

    @Override
    public int getLayoutId() {
        return R.layout.activity_international_cargo_list;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        mData = (CargoReportHisBean) intent.getSerializableExtra("flightBean");
        setToolbarShow(View.VISIBLE);
        toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "货物上报");
        flightBean = mData.getMainInfos();
        mPresenter = new GetFlightCargoResPresenter(this);
        LoadingListRequestEntity entity = new LoadingListRequestEntity();
        entity.setDocumentType(2);
        entity.setFlightId(mData.getFlightId());
        ((GetFlightCargoResPresenter) mPresenter).getLoadingList(entity);
        initView();

    }

    private void initView() {
        mSlideRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new InternationalCargoAdapter(mList);
        mAdapter.setOnDeleteClickListener((view, position) -> {
            mSlideRV.closeMenu();
            mList.remove(position);
            mAdapter.notifyDataSetChanged();
        });
        //传过来的板车如果有数据就显示
        if (flightBean.size() > 0) {
            mList.addAll(flightBean);
//            mAdapter1 = new InternationalCargoAdapter(mList1);
//            mAdapter1.notifyDataSetChanged();
//            mSlideRV.setAdapter(mAdapter1);
        }
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
        });
        mSlideRV.setAdapter(mAdapter);
        //初始化ui界面
        flightId.setText(mData.getFlightNo());
//        tvFlightType.setText(mData.getTpFlightType()+"");
//        tvFlightPlace.setText(flightBean.gett());
        tvArriveTime.setText(String.format(getString(R.string.format_arrive_info), TimeUtils.date2Tasktime3(mData.getCreateTime()), TimeUtils.getDay((mData.getCreateTime()))));
        //显示航线，2条 3条 4条
//        if (flightBean.getFlightCourseByAndroid() != null && flightBean.getFlightCourseByAndroid().size() > 1)
//            switch (flightBean.getItemType()) {
//                case 2:
//                    llFlight2.setVisibility(View.VISIBLE);
//                    tvFlight21.setText(flightBean.getFlightCourseByAndroid().get(0));
//                    tvFlight22.setText(flightBean.getFlightCourseByAndroid().get(1));
//                    break;
//                case 3:
//                    llFlight3.setVisibility(View.VISIBLE);
//                    tvFlight31.setText(flightBean.getFlightCourseByAndroid().get(0));
//                    tvFlight32.setText(flightBean.getFlightCourseByAndroid().get(1));
//                    tvFlight33.setText(flightBean.getFlightCourseByAndroid().get(2));
//                    break;
//                case 4:
//                    llFlight4.setVisibility(View.VISIBLE);
//                    tvFlight41.setText(flightBean.getFlightCourseByAndroid().get(0));
//                    tvFlight42.setText(flightBean.getFlightCourseByAndroid().get(1));
//                    tvFlight43.setText(flightBean.getFlightCourseByAndroid().get(2));
//                    tvFlight44.setText(flightBean.getFlightCourseByAndroid().get(3));
//                    break;
//            }
    }

    @OnClick({R.id.ll_add, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_add:
                mSlideRV.closeMenu();
                ScanManagerActivity.startActivity(this,"CargoDoneListActivity");
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
        for (int i = 0; i < mList.size(); i++) {
            for (int j = 0; j < mData.getMainInfos().size(); j++) {
                if (mList.get(i).equals(mData.getMainInfos().get(j)))
                    mList.remove(i);
            }
        }
        if (mList.size() == 0) {
            ToastUtil.showToast("请扫描板车");
        } else {
            for (TransportTodoListBean item : mList) {
                item.setBaggageSubOperator(UserInfoSingle.getInstance().getUserId());
                item.setBaggageSubTerminal(UserInfoSingle.getInstance().getUsername());
                item.setBaggageSubUserName(DeviceInfoUtil.getDeviceInfo(this).get("deviceId"));
            }
            CargoUploadBean entity = new CargoUploadBean();
            entity.setBaggageWeight(mBaggageWeight);
            entity.setMailWeight(mMailWeight);
            entity.setCargoWeight(mGoodsWeight);
//            entity.setBaggageWeight(1.11);
//            entity.setMailWeight(2.22);
//            entity.setCargoWeight(3.33);
            entity.setData(mList);
            entity.setMovement(flightBean.get(0).getFlightIndicator());
            entity.setFlightId(Long.valueOf(flightBean.get(0).getFlightId()));
            entity.setStaffId(UserInfoSingle.getInstance().getUserId());
            mPresenter = new InternationalCargoReportPresenter(this);
            ((InternationalCargoReportPresenter) mPresenter).internationalCargoReport(entity);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if (result.getFunctionFlag().equals("CargoDoneListActivity")) {
            if (result.getData() != null && result.getData().length() == Constants.SCOOTER_NO_LENGTH) {
                //板车号
                mScooterCode = result.getData();
                if (!"".equals(mScooterCode)) {
                    checkScooterCode(mScooterCode);
                } else {
                    ToastUtil.showToast("扫码数据为空请重新扫码");
                }
            } else {
                ToastUtil.showToast("请扫描或输入正确的板车号");
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
            String flightType = flightBean.get(0).getFlightIndicator();
            if ("D".equals(flightType) || "I".equals(flightType)) {
                for (ScooterInfoListBean bean : scooterInfoListBeans) {
                    bean.setFlightType(flightType);
                }
                showBoardInfos(scooterInfoListBeans);
            } else {
                ChoseFlightTypeDialog dialog = new ChoseFlightTypeDialog();
                dialog.setData(this,"请选择国际或国内","国际","国内", isCheckRight -> {
                    if (isCheckRight) {
                        for (ScooterInfoListBean bean : scooterInfoListBeans) {
                            bean.setFlightType("D");
                        }
                    } else {
                        for (ScooterInfoListBean bean : scooterInfoListBeans) {
                            bean.setFlightType("I");
                        }
                    }
                    showBoardInfos(scooterInfoListBeans);
                });
                dialog.setCancelable(false);
                dialog.show(getSupportFragmentManager(), "111");
            }
        } else {
            ToastUtil.showToast("无该板信息");
        }
    }

    private void showBoardInfos(List<ScooterInfoListBean> scooterInfoListBeans) {
        TransportTodoListBean bean = new TransportTodoListBean();
        bean.setTpScooterCode(scooterInfoListBeans.get(0).getScooterCode());
        bean.setTpScooterType(scooterInfoListBeans.get(0).getScooterType() + "");
        bean.setFlightId(flightBean.get(0).getFlightId());
        bean.setFlightNo(flightBean.get(0).getFlightNo());
        bean.setTpFlightLocate(flightBean.get(0).getTpFlightLocate());
        bean.setTpFlightTime(flightBean.get(0).getTpFlightTime());
        bean.setFlightInfoId(flightBean.get(0).getId());
        bean.setAsFlightId(flightBean.get(0).getAsFlightId());
        bean.setTpFlightType(flightBean.get(0).getTpFlightType());
        bean.setFlightIndicator("I");
        mList.add(bean);
        mAdapter.notifyDataSetChanged();
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

    /**
     * 检测板车号是否可用
     *
     * @param scooterCode
     */
    private void checkScooterCode(String scooterCode) {
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

    @Override
    public void getLoadingListResult(LoadingListBean result) {
        mGoodsWeight = 0;
        mMailWeight = 0;
        mBaggageWeight = 0;
        if (result.getData() == null || result.getData().size() == 0) return;
        for (LoadingListBean.DataBean.ContentObjectBean dataBean : result.getData().get(0).getContentObject()) {
            for (LoadingListBean.DataBean.ContentObjectBean.ScooterBean scooterBean : dataBean.getScooters()) {
                switch (scooterBean.getType()) {
                    case "C":
                    case "CT":
                        mGoodsWeight += scooterBean.getWeight();
                        break;
                    case "M":
                        mMailWeight += scooterBean.getWeight();
                        break;
                    case "B":
                    case "T":
                    case "BY":
                    case "BT":
                        mBaggageWeight += scooterBean.getWeight();
                        break;
                }
            }
        }
        Log.e("tagTest", "" + mGoodsWeight + mMailWeight + mBaggageWeight);
    }

    @Override
    public void setFlightSpace(CargoCabinData result) {

    }

    @Override
    public void flightDoneInstallResult(String result) {

    }

    @Override
    public void overLoadResult(String result) {

    }

    @Override
    public void confirmLoadPlanResult(String result) {

    }

    @Override
    public void getPullStatusResult(BaseEntity<String> result) {

    }
}
