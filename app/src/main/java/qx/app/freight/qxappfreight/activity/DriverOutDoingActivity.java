package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.DriverOutTaskDoingAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.TpFlightStep;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.FlightOfScooterBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.OutFieldTaskBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.ScanScooterCheckUsedContract;
import qx.app.freight.qxappfreight.contract.ScanScooterContract;
import qx.app.freight.qxappfreight.contract.ScooterInfoListContract;
import qx.app.freight.qxappfreight.contract.TransportBeginContract;
import qx.app.freight.qxappfreight.dialog.ChoseFlightTypeDialog;
import qx.app.freight.qxappfreight.presenter.ScanScooterPresenter;
import qx.app.freight.qxappfreight.presenter.ScooterInfoListPresenter;
import qx.app.freight.qxappfreight.presenter.TransportBeginPresenter;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.MapValue;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * ??????????????????
 */
public class DriverOutDoingActivity extends BaseActivity implements TransportBeginContract.transportBeginView, ScanScooterContract.scanScooterView, ScanScooterCheckUsedContract.ScanScooterCheckView, ScooterInfoListContract.scooterInfoListView {
    @BindView(R.id.ll_add)
    LinearLayout llAdd;

    @BindView(R.id.rv_car_doing)
    RecyclerView doingRecyclerView;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_error_report)
    TextView tvErrorReport;
    @BindView(R.id.image_scan)
    ImageView imageScan;
    @BindView(R.id.cb_all)
    CheckBox cbAll;
    @BindView(R.id.ll_cb_all)
    LinearLayout llcbAll;
    @BindView(R.id.iv_error_end)
    ImageView ivErrorEnd;

    private boolean isBaggage = false; //?????????????????????????????????????????????????????????

    @BindView(R.id.tv_tp_status)
    TextView tvTpStatus;
    @BindView(R.id.tv_can_pull_scooter)
    TextView tvCanPullScooter; //????????????


    private List <FlightOfScooterBean> list;
    private List <TransportTodoListBean> listScooter;
    private DriverOutTaskDoingAdapter mDriverOutTaskDoingAdapter;

    private List <OutFieldTaskBean> flightNumList = new ArrayList <>();

    private int tpStatus = 1; // 0 ????????? 1 ????????????
    private List <OutFieldTaskBean> mAcceptTerminalTodoBean;

    private int tpNum = 0; //????????????????????????

    private String transfortType; //????????? ???????????????????????????

    private int isSure = 0;//0 ?????? 1 ???????????????

    private String nowScooterCode = "";
    private List <String> mCanScanCodes = new ArrayList <>();

    public static void startActivity(Context context, List <OutFieldTaskBean> mTasksBean, String transfortType) {
        Intent starter = new Intent(context, DriverOutDoingActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("acceptTerminalTodoBean", (Serializable) mTasksBean);
        starter.putExtra("transfortType", transfortType);
        starter.putExtras(mBundle);
        ((Activity) context).startActivityForResult(starter, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_driver_out_doing;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        setToolbarShow(View.VISIBLE);
        CustomToolbar toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "????????????");
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        listScooter = new ArrayList <>();
        //??????????????????????????????????????????
        mAcceptTerminalTodoBean = (List <OutFieldTaskBean>) getIntent().getSerializableExtra("acceptTerminalTodoBean");
        transfortType = getIntent().getStringExtra("transfortType");

        if (Constants.TP_TYPE_SINGLE.equals(mAcceptTerminalTodoBean.get(0).getCargoType()) || Constants.TP_TYPE_BAGGAAGE.equals(mAcceptTerminalTodoBean.get(0).getCargoType())) {
            isBaggage = true;
        }
        //?????????????????????????????????????????????????????????????????????????????????
        if (mAcceptTerminalTodoBean != null) {
            //???????????? ?????????????????????0  ?????????????????? ?????????????????? ??????????????????
            if (Constants.TP_TYPE_SINGLE.equals(mAcceptTerminalTodoBean.get(0).getCargoType()))
                tpNum = 1;
            else
                tpNum = 0;
            for (OutFieldTaskBean mOutFieldTaskBean : mAcceptTerminalTodoBean) {
                tpNum += mOutFieldTaskBean.getNum();
            }
        }

        doingRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list = new ArrayList <>();
        mDriverOutTaskDoingAdapter = new DriverOutTaskDoingAdapter(list);
        doingRecyclerView.setAdapter(mDriverOutTaskDoingAdapter);
        //??????????????????
        mDriverOutTaskDoingAdapter.setOnDeleteClickListener((view, parentPosition, childPosition) -> {

            deleteHandcar(list.get(parentPosition).getMTransportTodoListBeans().get(childPosition));

        });
        //??????????????????????????????????????????????????????
        mDriverOutTaskDoingAdapter.setOnItemClickListener((adapter, view, position) -> {
        });
        //??????
        llAdd.setOnClickListener(v -> {
            if (!Tools.isFastClick())
                return;
            if (Constants.TP_TYPE_SINGLE.equals(mAcceptTerminalTodoBean.get(0).getCargoType())) {
                ScanManagerActivity.startActivity(this, "DriverOutDoingActivity");
            } else {
                if (tpStatus == 0) {
                    ToastUtil.showToast("???????????????????????????????????????");
                    return;
                }
                if (listScooter.size() >= tpNum) {

                    ToastUtil.showToast("?????????????????????" + tpNum + "?????????");
                    return;
                }
                ScanManagerActivity.startActivity(this, "DriverOutDoingActivity");
            }

        });
        //??????????????? ????????????????????????????????? ??????
        mDriverOutTaskDoingAdapter.setCheckBoxListener((view, position) -> {
            if (list.get(position).isSelect())
                list.get(position).setSelect(false);
            else
                list.get(position).setSelect(true);

            //??????item???  ?????????????????????
            for (FlightOfScooterBean mFlightOfScooterBean : list) {
                if (!mFlightOfScooterBean.isSelect()) {
                    cbAll.setChecked(false);
                } else
                    cbAll.setChecked(true);
            }
            upDataBtnStatusEnd();
        });

        /**
         * ??????
         */
        cbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (FlightOfScooterBean mFlightOfScooterBean : list) {
                        mFlightOfScooterBean.setSelect(true);
                    }
                } else {
                    for (FlightOfScooterBean mFlightOfScooterBean : list) {
                        mFlightOfScooterBean.setSelect(false);
                    }
                }
                mDriverOutTaskDoingAdapter.notifyDataSetChanged();
                upDataBtnStatusEnd();
            }
        });
        upDataBtnStatus();
        getData();
        //?????????????????? ?????????????????????
        if (!Constants.TP_TYPE_SINGLE.equals(mAcceptTerminalTodoBean.get(0).getCargoType())) {
            getCanPullScooter();
        }
    }

    /**
     * ????????????
     *
     * @param remove
     */
    private void deleteHandcar(TransportTodoListBean remove) {
        mPresenter = new TransportBeginPresenter(this);
        TransportEndEntity transportEndEntity = new TransportEndEntity();
//        if (Constants.TP_TYPE_SINGLE.equals(mAcceptTerminalTodoBean.get(0).getCargoType())){
            transportEndEntity.setTpCargoType(remove.getTpCargoType());
//        }
        transportEndEntity.setId(remove.getId());
        ((TransportBeginPresenter) mPresenter).scanScooterDelete(transportEndEntity);
    }

    private void getData() {
        mPresenter = new ScanScooterPresenter(this);
        ((ScanScooterPresenter) mPresenter).scooterWithUser(UserInfoSingle.getInstance().getUserId(), mAcceptTerminalTodoBean.get(0).getFlightId(), mAcceptTerminalTodoBean.get(0).getTaskId());

    }

    private void getCanPullScooter() {
        mPresenter = new ScanScooterPresenter(this);
        ((ScanScooterPresenter) mPresenter).scooterWithUserTask(mAcceptTerminalTodoBean.get(0).getTaskId());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if (result.getData() != null && result.getData().length() > 0) {

            //???????????????
            if ("baggage_area".equals(mAcceptTerminalTodoBean.get(0).getEndAreaType()) && tpStatus == 0) {
                isSureEndLoc(result.getData());
            } else {
                if (mCanScanCodes.contains(result.getData())||Constants.TP_TYPE_SINGLE.equals(mAcceptTerminalTodoBean.get(0).getCargoType())) {
                    if (tpStatus == 0) {
                        ToastUtil.showToast("???????????????????????????????????????");
                        return;
                    }
                    if (listScooter.size() >= tpNum && !Constants.TP_TYPE_SINGLE.equals(mAcceptTerminalTodoBean.get(0).getCargoType())) {

                        ToastUtil.showToast("?????????????????????" + tpNum + "?????????");
                        return;
                    }
                    if (getClass().getSimpleName().equals(result.getFunctionFlag())) {
                        //??????????????????????????????????????????????????????
                        for (TransportTodoListBean transportTodoListBean: listScooter){
                            if (transportTodoListBean.getTpScooterCode().equals(result.getData())){
                                ToastUtil.showToast("???????????????????????????");
                                return;
                            }
                        }
                        addScooterInfo(result.getData());
                    }
                } else {
                    ToastUtil.showToast("???????????????????????????????????????????????????");
                }
            }
        } else {
            ToastUtil.showToast("????????????????????????????????????");
        }
    }

    /**
     * ???????????????????????? ????????????
     *
     * @param result
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {
        if (result.isCancelFlag() || result.isChangeWorkerUser()) {
            if (result.getTaskId() != null && result.getTaskId().equals(mAcceptTerminalTodoBean.get(0).getTaskId())) {

                ToastUtil.showToast("????????????????????????");
                finish();
            }
        }
    }

    /**
     * ???????????????????????? ????????????
     *
     * @param result
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String result) {
        if (result.equals("DriverOutDoingActivity_finish")) {
            ToastUtil.showToast("????????????????????????");
            EventBus.getDefault().post("TaskDriverOutFragment_refresh");
            finish();
        }
    }

    /**
     * ????????????????????????
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.SCAN_RESULT == resultCode) {
            String str = data.getStringExtra(Constants.SACN_DATA);
            //??????????????????????????????????????????????????????
            if (isSure == 0)
                addScooterInfo(str);
            else
                isSureEndLoc(str);

        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param str
     */
    private void isSureEndLoc(String str) {

        if (!str.equals(mAcceptTerminalTodoBean.get(0).getEndAreaId())) {
            ToastUtil.showToast("??????????????????????????????:" + mAcceptTerminalTodoBean.get(0).getEndAreaId());
            return;
        } else
            doEnd();
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param scooterCode
     */
    private void addScooterInfo(String scooterCode) {
        if (!"".equals(scooterCode)) {
            /**
             * ?????????????????? ??????????????? ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
             */
            if (Constants.TP_TYPE_SINGLE.equals(mAcceptTerminalTodoBean.get(0).getCargoType())) {

                getNumberInfo(scooterCode);
//                checkScooterCode(scooterCode);

            } else {
                mPresenter = new ScanScooterPresenter(this);
                TransportTodoListBean mainIfos = new TransportTodoListBean();
                mainIfos.setTpScooterCode(scooterCode);
//            mainIfos.setTpOperator("u6911330e59ce46c288181ed11a48ee23");
                mainIfos.setTpOperator(UserInfoSingle.getInstance().getUserId());
                mainIfos.setTpScooterType(MapValue.getHYOfZP(transfortType));
                mainIfos.setTaskId(mAcceptTerminalTodoBean.get(0).getTaskId());
                mainIfos.setTpStartLocate(mAcceptTerminalTodoBean.get(0).getBeginAreaType());
                ((ScanScooterPresenter) mPresenter).scanScooter(mainIfos);
            }

        } else
            ToastUtil.showToast("?????????????????????????????????");
    }

    /**
     * ????????????????????????????????????
     *
     * @param scooterCode
     */
    private void checkScooterCode(String scooterCode) {
//        mPresenter = new ScanScooterCheckUsedPresenter(this);
//        nowScooterCode = scooterCode;
//        ((ScanScooterCheckUsedPresenter) mPresenter).checkScooterCode(scooterCode,mAcceptTerminalTodoBean);
    }

    /**
     * ???????????????????????????
     *
     * @param transportTodoListBeans
     * @param mainIfos
     * @param scooterCode
     */
    private void updataScooter(List <TransportTodoListBean> transportTodoListBeans, TransportTodoListBean mainIfos, String scooterCode) {

        TransportEndEntity transportEndEntity = new TransportEndEntity();
        mPresenter = new ScanScooterPresenter(this);
        mainIfos.setTaskId(mAcceptTerminalTodoBean.get(0).getTaskId());
        mainIfos.setFlightId(mAcceptTerminalTodoBean.get(0).getFlightId());
        mainIfos.setFlightNo(mAcceptTerminalTodoBean.get(0).getFlightNo());
        mainIfos.setTpCargoType(mAcceptTerminalTodoBean.get(0).getCargoType());
        mainIfos.setTpType("???");
//        mainIfos.setTpScooterCode(scooterCode);
        mainIfos.setTpOperator(UserInfoSingle.getInstance().getUserId());
//        mainIfos.setTpScooterType(scooterCode.substring(0, 1));
        mainIfos.setTpStartLocate(mAcceptTerminalTodoBean.get(0).getBeginAreaType());
        mainIfos.setBeginAreaId(mAcceptTerminalTodoBean.get(0).getBeginAreaId());
        mainIfos.setTpState(1);
        transportTodoListBeans.add(mainIfos);
        transportEndEntity.setTaskType("1");
        transportEndEntity.setScooters(transportTodoListBeans);
        ((ScanScooterPresenter) mPresenter).scanLockScooter(transportEndEntity);
    }


    private int getMaxHandcarNum() {
        if (listScooter != null)
            return listScooter.size();
        return 0;
    }

    /**
     * ??????????????????????????????
     */
    private void upDataBtnStatus() {
        if (getMaxHandcarNum() >= tpNum) {
            tvStart.setClickable(true);
            tvStart.setBackgroundResource(R.drawable.btn_blue_press);
        } else {
            tvStart.setClickable(false);
            tvStart.setBackgroundColor(getResources().getColor(R.color.gray));
        }
    }

    /**
     * ??????????????????????????????
     */
    private void upDataBtnStatusEnd() {
        tvStart.setClickable(false);
        tvStart.setBackgroundColor(getResources().getColor(R.color.gray));
        for (FlightOfScooterBean mFlightOfScooterBean : list) {
            if (mFlightOfScooterBean.isSelect()) {
                tvStart.setClickable(true);
                tvStart.setBackgroundResource(R.drawable.btn_blue_press);
                break;
            }
        }

    }

    @OnClick({R.id.ll_add, R.id.tv_start, R.id.tv_error_report, R.id.iv_error_end})
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_add://????????????
                if (Constants.TP_TYPE_SINGLE.equals(mAcceptTerminalTodoBean.get(0).getCargoType())) {
                    ScanManagerActivity.startActivity(this, "DriverOutDoingActivity");
                } else {
                    if (tpStatus == 0) {
                        ToastUtil.showToast("???????????????????????????????????????");
                        return;
                    }
                    if (listScooter.size() >= tpNum) {

                        ToastUtil.showToast("?????????????????????" + tpNum + "?????????");
                        return;
                    }
                    ScanManagerActivity.startActivity(this, "DriverOutDoingActivity");
                }
                break;
            case R.id.tv_error_report://????????????
                flightNumList.clear();
                for (OutFieldTaskBean item : mAcceptTerminalTodoBean) {
                    if (!flightNumList.contains(item.getFlightNo())) {
                        flightNumList.add(item);
                    }
                }
                if (!flightNumList.isEmpty()) {
                    Intent intent = new Intent(this, ErrorReportActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("plane_info_list", (ArrayList <OutFieldTaskBean>) flightNumList);
                    intent.putExtras(bundle);
                    intent.putExtra("error_type", 4);
                    intent.putExtra("area_id", "");//area_id
                    intent.putExtra("step_code", Constants.TP_START);//step_code
                    intent.putExtra("task_id", flightNumList.get(0).getTaskId());//??????id
                    startActivity(intent);
                }
                break;
            case R.id.iv_error_end:

                if (tpStatus == 0) {
                    flightNumList.clear();
                    for (OutFieldTaskBean item : mAcceptTerminalTodoBean) {
                        if (!flightNumList.contains(item.getFlightNo())) {
                            flightNumList.add(item);
                        }
                    }
                    if (!flightNumList.isEmpty()) {
                        Intent intent = new Intent(this, AbnormalEndActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("plane_info_list", (ArrayList <OutFieldTaskBean>) flightNumList);
                        bundle.putSerializable("TransportEndEntity", getTransportEndEntity());
                        intent.putExtras(bundle);
                        intent.putExtra("error_type", 4);
                        startActivity(intent);
                    }
                } else
                    ToastUtil.showToast("?????????????????????????????????????????????");

                break;
            case R.id.tv_start://????????????????????????
                if (tpStatus == 1) {
                    doStart();
                } else {
                    /**
                     * ???????????? ?????? ?????? ?????????????????????????????????????????????
                     */
                    if ((Constants.TP_TYPE_BAGGAAGE.equals(mAcceptTerminalTodoBean.get(0).getCargoType()) && "baggage_area".equals(mAcceptTerminalTodoBean.get(0).getEndAreaType())) || Constants.TP_TYPE_SINGLE.equals(mAcceptTerminalTodoBean.get(0).getCargoType())) {
                        isSure = 1;
                        ScanManagerActivity.startActivity(this, "DriverOutDoingActivity");
                        return;
                    }
                    doEnd();
                }
                break;
        }
    }

    /**
     * ????????????
     */
    private void doStart() {
        mPresenter = new TransportBeginPresenter(this);
        TransportEndEntity transportEndEntity = new TransportEndEntity();
        //???????????????????????????????????????
        List <TransportTodoListBean> mListBeanBegin = new ArrayList <>();
        mListBeanBegin.addAll(listScooter);
        //
        List <TpFlightStep> steps = new ArrayList <>();
        for (OutFieldTaskBean mOutFieldTaskBean : mAcceptTerminalTodoBean) {
            //??????????????????????????????????????????????????????
            for (TransportTodoListBean tr : mListBeanBegin) {
                if (mOutFieldTaskBean.getFlightNo().equals(mOutFieldTaskBean.getFlightNo())) {
                    tr.setTaskId(mOutFieldTaskBean.getTaskId());
                    tr.setTaskPk(mOutFieldTaskBean.getId());
                    tr.setBeginAreaType(mOutFieldTaskBean.getBeginAreaType());
                    tr.setBeginAreaId(mOutFieldTaskBean.getBeginAreaId());
                    tr.setEndAreaType(mOutFieldTaskBean.getEndAreaType());
                    tr.setEndAreaId(mOutFieldTaskBean.getEndAreaId());
                    tr.setInSeat(1);
                }
            }
            //??????????????????????????????
            TpFlightStep step = new TpFlightStep();
            step.setType(0);
            step.setLoadUnloadDataId(mOutFieldTaskBean.getId());
            step.setFlightId(Long.valueOf(mOutFieldTaskBean.getFlightId()));
            step.setFlightTaskId(mOutFieldTaskBean.getTaskId());
            step.setLatitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLatitude()+"");
            step.setLongitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLongitude()+"");
            step.setOperationCode(Constants.TP_START);
            step.setUserName(UserInfoSingle.getInstance().getUsername());
            step.setTerminalId(DeviceInfoUtil.getDeviceInfo(this).get("deviceId"));
            step.setUserId(UserInfoSingle.getInstance().getUserId());
            step.setCreateTime(System.currentTimeMillis());
            step.setDeptId(UserInfoSingle.getInstance().getDepId());
            steps.add(step);
        }
        transportEndEntity.setScooters(mListBeanBegin);
        transportEndEntity.setSteps(steps);
        ((TransportBeginPresenter) mPresenter).transportBegin(transportEndEntity);
    }

    /**
     * ??????????????????????????????
     *
     * @param
     * @param
     */
    private void doEnd() {
        List <OutFieldTaskBean> listEndTemp = new ArrayList <>();
        List <TransportTodoListBean> mListBeanBegin = new ArrayList <>();
        //????????????????????? ???????????? ?????? ???????????? ???????????????????????? ???????????????????????????
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelect()) {
                for (TransportTodoListBean mTransportTodoListBean : list.get(i).getMTransportTodoListBeans()) {
                    if (list.get(i).getFlightNo().equals(mTransportTodoListBean.getFlightNo()))
                        mListBeanBegin.add(mTransportTodoListBean);
                }
                for (OutFieldTaskBean mOutFieldTaskBean : mAcceptTerminalTodoBean) {
                    if (list.get(i).getFlightNo().equals(mOutFieldTaskBean.getFlightNo()))
                        listEndTemp.add(mOutFieldTaskBean);
                }
            }
        }
        if (listEndTemp != null && mListBeanBegin.size() > 0) {
            mPresenter = new TransportBeginPresenter(this);
            TransportEndEntity transportEndEntity = new TransportEndEntity();
            transportEndEntity.setTaskType(mAcceptTerminalTodoBean.get(0).getCargoType());
            for (TransportTodoListBean tr : mListBeanBegin) {
                tr.setInSeat(1);
                for (OutFieldTaskBean mOutFieldTaskBean : listEndTemp) {
                    if (mOutFieldTaskBean.getFlightNo().equals(tr.getFlightNo())) {
                        tr.setBeginAreaType(mOutFieldTaskBean.getBeginAreaType());
                        tr.setBeginAreaId(mOutFieldTaskBean.getBeginAreaId());
                        tr.setEndAreaId(mOutFieldTaskBean.getEndAreaId());
                        tr.setEndAreaType(mOutFieldTaskBean.getEndAreaType());
                        tr.setTaskId(mOutFieldTaskBean.getTaskId());
                        tr.setTaskPk(mOutFieldTaskBean.getId());
                        tr.setAcdmDtoId(mOutFieldTaskBean.getAcdmDtoId());
                    }
                }
            }
            List <TpFlightStep> steps = new ArrayList <>();
            for (OutFieldTaskBean mOutFieldTaskBean : listEndTemp) {

                TpFlightStep step = new TpFlightStep();
                step.setType(0);
                step.setLoadUnloadDataId(mOutFieldTaskBean.getId());
                step.setFlightId(Long.valueOf(mOutFieldTaskBean.getFlightId()));
                step.setFlightTaskId(mOutFieldTaskBean.getTaskId());
                step.setLatitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLatitude()+"");
                step.setLongitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLongitude()+"");
                step.setOperationCode(Constants.TP_END);
                step.setUserName(UserInfoSingle.getInstance().getUsername());
                step.setTerminalId(DeviceInfoUtil.getDeviceInfo(this).get("deviceId"));
                step.setUserId(UserInfoSingle.getInstance().getUserId());
                step.setCreateTime(System.currentTimeMillis());
                step.setDeptId(UserInfoSingle.getInstance().getDepId());

                steps.add(step);
            }
            transportEndEntity.setScooters(mListBeanBegin);
            transportEndEntity.setSteps(steps);

            ((TransportBeginPresenter) mPresenter).transportEnd(transportEndEntity);
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @return
     */
    private TransportEndEntity getTransportEndEntity() {
        TransportEndEntity transportEndEntity = new TransportEndEntity();
        List <OutFieldTaskBean> listEndTemp = new ArrayList <>();
        List <TransportTodoListBean> mListBeanBegin = new ArrayList <>();
        //????????????????????? ???????????? ?????? ???????????? ???????????????????????? ???????????????????????????
        for (int i = 0; i < list.size(); i++) {
            for (TransportTodoListBean mTransportTodoListBean : list.get(i).getMTransportTodoListBeans()) {
                if (list.get(i).getFlightNo().equals(mTransportTodoListBean.getFlightNo()))
                    mListBeanBegin.add(mTransportTodoListBean);
            }
            for (OutFieldTaskBean mOutFieldTaskBean : mAcceptTerminalTodoBean) {
                if (list.get(i).getFlightNo().equals(mOutFieldTaskBean.getFlightNo()))
                    listEndTemp.add(mOutFieldTaskBean);
            }
        }
        if (listEndTemp != null && mListBeanBegin.size() > 0) {
            mPresenter = new TransportBeginPresenter(this);
            transportEndEntity.setTaskType(mAcceptTerminalTodoBean.get(0).getCargoType());
            for (TransportTodoListBean tr : mListBeanBegin) {
                tr.setInSeat(1);
                for (OutFieldTaskBean mOutFieldTaskBean : listEndTemp) {
                    if (mOutFieldTaskBean.getFlightNo().equals(tr.getFlightNo())) {
                        tr.setBeginAreaType(mOutFieldTaskBean.getBeginAreaType());
                        tr.setBeginAreaId(mOutFieldTaskBean.getBeginAreaId());
                        tr.setEndAreaId(mOutFieldTaskBean.getEndAreaId());
                        tr.setEndAreaType(mOutFieldTaskBean.getEndAreaType());
                        tr.setTaskId(mOutFieldTaskBean.getTaskId());
                        tr.setTaskPk(mOutFieldTaskBean.getId());
                        tr.setAcdmDtoId(mOutFieldTaskBean.getAcdmDtoId());
                    }
                }
            }
            List <TpFlightStep> steps = new ArrayList <>();
            for (OutFieldTaskBean mOutFieldTaskBean : listEndTemp) {

                TpFlightStep step = new TpFlightStep();
                step.setType(0);
                step.setLoadUnloadDataId(mOutFieldTaskBean.getId());
                step.setFlightId(Long.valueOf(mOutFieldTaskBean.getFlightId()));
                step.setFlightTaskId(mOutFieldTaskBean.getTaskId());
                step.setLatitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLatitude()+"");
                step.setLongitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLongitude()+"");
                step.setOperationCode(Constants.TP_END);
                step.setUserName(UserInfoSingle.getInstance().getUsername());
                step.setTerminalId(DeviceInfoUtil.getDeviceInfo(this).get("deviceId"));
                step.setUserId(UserInfoSingle.getInstance().getUserId());
                step.setCreateTime(System.currentTimeMillis());
                step.setDeptId(UserInfoSingle.getInstance().getDepId());

                steps.add(step);
            }
            transportEndEntity.setTaskId(mAcceptTerminalTodoBean.get(0).getTaskId());
            transportEndEntity.setScooters(mListBeanBegin);
            transportEndEntity.setSteps(steps);
        }
        return transportEndEntity;
    }

    /**
     * ????????????????????????
     */
    public void getNumberInfo(String mScooterCode) {

        mPresenter = new ScooterInfoListPresenter(this);
        BaseFilterEntity baseFilterEntity = new BaseFilterEntity();
        MyAgentListBean myAgentListBean = new MyAgentListBean();
        baseFilterEntity.setSize(10);
        baseFilterEntity.setCurrent(1);
        myAgentListBean.setScooterCode(mScooterCode);
        baseFilterEntity.setFilter(myAgentListBean);
        ((ScooterInfoListPresenter) mPresenter).ScooterInfoList(baseFilterEntity);
    }

    @Override
    public void transportBeginResult(String result) {
        if (!"".equals(result)) {
            setTpStatus(0);
            upDataBtnStatusEnd();
            EventBus.getDefault().post("TaskDriverOutFragment_refresh");
        } else
            Log.e("????????????", "????????????");
    }

    /**
     * ??????flag ????????? ?????????????????????
     *
     * @param flag 0 ????????? 1 ?????????????????????????????????
     */
    private void setTpStatus(int flag) {
        tpStatus = flag;
        if (flag == 0) {
            tvStart.setText("??????");
            llAdd.setVisibility(View.GONE);
            if (!isBaggage)
                ivErrorEnd.setVisibility(View.VISIBLE);
            mDriverOutTaskDoingAdapter.setCheckBoxEnable(true);
            mDriverOutTaskDoingAdapter.setIsmIsSlide(false);
            tvTpStatus.setVisibility(View.VISIBLE);
            tvCanPullScooter.setVisibility(View.GONE);
            llcbAll.setVisibility(View.VISIBLE);
            upDataBtnStatusEnd();
        } else {
            if (getMaxHandcarNum() == 0) {
                tvStart.setText("??????");
                llAdd.setVisibility(View.VISIBLE);
                mDriverOutTaskDoingAdapter.setCheckBoxEnable(false);
                mDriverOutTaskDoingAdapter.setIsmIsSlide(true);
                tvTpStatus.setVisibility(View.GONE);
                tvCanPullScooter.setVisibility(View.VISIBLE);
                llcbAll.setVisibility(View.GONE);
                ivErrorEnd.setVisibility(View.GONE);
            }
            llcbAll.setVisibility(View.GONE);
            upDataBtnStatus();
        }


    }

    @Override
    public void transportEndResult(String result) {
        if (!"".equals(result)) {
            getData();
        } else
            Log.e("????????????", "????????????");
    }

    @Override
    public void scanScooterDeleteResult(String result) {

        if (!"".equals(result)) {
            if (getMaxHandcarNum() == tpNum) {
                llAdd.setVisibility(View.VISIBLE);
            }
            getData();
        }

    }

    /**
     * ??????????????????????????????
     */
    @Override
    public void scooterWithUserResult(List <TransportTodoListBean> result) {
        if (result != null) {
            listScooter.clear();
            listScooter.addAll(result);
            //????????????????????????  ???????????????????????????
            if (tpStatus == 0) {
                if (listScooter.size() < 1) {
                    EventBus.getDefault().post("TaskDriverOutFragment_refresh");
                    finish();
                    return;
                }
            }
            // ???????????????????????? ????????????
            for (TransportTodoListBean mTransportTodoListBean : listScooter) {
                for (OutFieldTaskBean mOutFieldTaskBean : mAcceptTerminalTodoBean) {
                    if (mTransportTodoListBean.getFlightNo().equals(mOutFieldTaskBean.getFlightNo())) {
                        mTransportTodoListBean.setPlanePlace(mOutFieldTaskBean.getFlights().getSeat());
                        mTransportTodoListBean.setPlaneType(mOutFieldTaskBean.getFlights().getAircraftNo());
                        mTransportTodoListBean.setEtd(mOutFieldTaskBean.getFlights().getScheduleTime());
                        mTransportTodoListBean.setFlightRoute(mOutFieldTaskBean.getFlights().getRoute());
                        mTransportTodoListBean.setTpDestinationLocate(mOutFieldTaskBean.getEndAreaType());
                        mTransportTodoListBean.setNum(mOutFieldTaskBean.getNum());
                        mTransportTodoListBean.setCarType(mOutFieldTaskBean.getTransfortType());
                        mTransportTodoListBean.setTpCargoType(mOutFieldTaskBean.getCargoType());
                        mTransportTodoListBean.setEndAreaId(mOutFieldTaskBean.getEndAreaId());
                        mTransportTodoListBean.setBeginAreaId(mOutFieldTaskBean.getBeginAreaId());
                    }
                }
            }
            Map <String, FlightOfScooterBean> mapFlight = new HashMap <>();
            list.clear();
            // ??????????????? ?????? ????????? ??????????????????
            for (TransportTodoListBean mTransportTodoListBean : result) {
                if (mapFlight.get(mTransportTodoListBean.getFlightNo()) == null) {
                    FlightOfScooterBean mFlightOfScooterBean = new FlightOfScooterBean();
                    //?????????????????????????????????????????? ????????? ?????????????????????????????????
                    mFlightOfScooterBean.setFlightNo(mTransportTodoListBean.getFlightNo());
                    mFlightOfScooterBean.setPlanePlace(mTransportTodoListBean.getPlanePlace());
                    mFlightOfScooterBean.setPlaneType(mTransportTodoListBean.getPlaneType());
                    mFlightOfScooterBean.setEtd(mTransportTodoListBean.getEtd());
                    mFlightOfScooterBean.setFlightRoute(mTransportTodoListBean.getFlightRoute());
                    mFlightOfScooterBean.setCarType(mTransportTodoListBean.getCarType());
                    mFlightOfScooterBean.setNum(mTransportTodoListBean.getNum());

                    List <TransportTodoListBean> listBeans = new ArrayList <>();
                    listBeans.add(mTransportTodoListBean);
                    mFlightOfScooterBean.setMTransportTodoListBeans(listBeans);
                    mapFlight.put(mTransportTodoListBean.getFlightNo(), mFlightOfScooterBean);
                } else {
                    mapFlight.get(mTransportTodoListBean.getFlightNo()).getMTransportTodoListBeans().add(mTransportTodoListBean);
                }
            }

            list.addAll(new ArrayList <FlightOfScooterBean>(mapFlight.values()));
            mapFlight.clear();
            mDriverOutTaskDoingAdapter.notifyDataSetChanged();
//            if (result.size() >= tpNum)
            //?????? ???????????? ?????????????????? ????????? ???????????????
            if (mAcceptTerminalTodoBean.get(0).getTaskBeginTime() > 0)
                setTpStatus(0);
            else
                setTpStatus(1);
        }

    }

    /**
     * ????????????
     *
     * @param result
     */
    @Override
    public void scooterWithUserTaskResult(List <TransportTodoListBean> result) {
        String canPullStr = "????????????:";
        if (result != null && result.size() > 0) {
            mCanScanCodes.clear();
            for (TransportTodoListBean mTransportTodoListBean : result) {
                canPullStr = canPullStr + mTransportTodoListBean.getTpScooterCode() + "???";
                mCanScanCodes.add(mTransportTodoListBean.getTpScooterCode());
            }
            tvCanPullScooter.setVisibility(View.VISIBLE);
            tvCanPullScooter.setText(TextUtils.isEmpty(canPullStr) ? "" : canPullStr.substring(0, canPullStr.length() - 1));
        } else
            tvCanPullScooter.setVisibility(View.GONE);

    }

    //????????????????????????
    @Override
    public void scanScooterResult(String result) {
        if (!"".equals(result)) {
            getData();
        }

    }

    @Override
    public void scanLockScooterResult(String result) {
        if (!"".equals(result)) {
            getData();
        }
    }

    @Override
    public void toastView(String error) {
        if (error != null)
            ToastUtil.showToast(error);
    }

    @Override
    public void showNetDialog() {

        showProgessDialog("???????????????");

    }

    @Override
    public void dissMiss() {

        dismissProgessDialog();
    }

    /**
     * ????????????????????????????????????
     *
     * @param result
     */
    @Override
    public void checkScooterCodeResult(BaseEntity <Object> result) {
        if ("200".equals(result.getStatus())) {

            getNumberInfo(nowScooterCode);

        } else {
            ToastUtil.showToast("????????????????????????????????????");
        }
        //
        nowScooterCode = "";
    }

    /**
     * ??????????????????
     *
     * @param scooterInfoListBeans
     */
    @Override
    public void scooterInfoListResult(List <ScooterInfoListBean> scooterInfoListBeans) {
        //TODO
        if (scooterInfoListBeans != null && scooterInfoListBeans.size() > 0) {

            List <TransportTodoListBean> transportTodoListBeans = new ArrayList <>();
            TransportTodoListBean mainIfos = new TransportTodoListBean();
            //????????????????????????
            mainIfos.setTpScooterCode(scooterInfoListBeans.get(0).getScooterCode());
            mainIfos.setTpScooterType(scooterInfoListBeans.get(0).getScooterType());
            mainIfos.setHeadingFlag(scooterInfoListBeans.get(0).getHeadingFlag());

            if ("D".equals(mAcceptTerminalTodoBean.get(0).getFlights().getFlightIndicator()) || "I".equals(mAcceptTerminalTodoBean.get(0).getFlights().getFlightIndicator())) {
                mainIfos.setFlightIndicator(mAcceptTerminalTodoBean.get(0).getFlights().getFlightIndicator());
                updataScooter(transportTodoListBeans, mainIfos, nowScooterCode);
            } else {
                ChoseFlightTypeDialog dialog = new ChoseFlightTypeDialog();
                dialog.setData(this, "????????????????????????", "??????", "??????", isCheckRight -> {
                    if (isCheckRight) {
                        mainIfos.setFlightIndicator("D");
                    } else {
                        mainIfos.setFlightIndicator("I");
                    }
                    updataScooter(transportTodoListBeans, mainIfos, nowScooterCode);
                });
                dialog.setCancelable(false);
                dialog.show(getSupportFragmentManager(), "111");

            }
        }

    }

    @Override
    public void scooterInfoListForReceiveResult(List <ScooterInfoListBean> scooterInfoListBeans) {

    }

    @Override
    public void existResult(MyAgentListBean existBean) {

    }

    @Override
    public void addInfoResult(MyAgentListBean result) {

    }
}
