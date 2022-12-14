package qx.app.freight.qxappfreight.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.CabinAdapter;
import qx.app.freight.qxappfreight.adapter.CargoCabinAdapter;
import qx.app.freight.qxappfreight.adapter.CargoHandlingAdapter;
import qx.app.freight.qxappfreight.adapter.CargoHandlingWaybillAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.FightScooterSubmitEntity;
import qx.app.freight.qxappfreight.bean.request.GetScooterListInfoEntity;
import qx.app.freight.qxappfreight.bean.response.FlightCabinInfo;
import qx.app.freight.qxappfreight.bean.response.FtGroupScooter;
import qx.app.freight.qxappfreight.bean.response.FtRuntimeFlightScooter;
import qx.app.freight.qxappfreight.bean.response.GetScooterListInfoBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.GetScooterListInfoContract;
import qx.app.freight.qxappfreight.contract.ScooterInfoListContract;
import qx.app.freight.qxappfreight.contract.ScooterOperateContract;
import qx.app.freight.qxappfreight.presenter.GetScooterListInfoPresenter;
import qx.app.freight.qxappfreight.presenter.ScooterInfoListPresenter;
import qx.app.freight.qxappfreight.presenter.ScooterOperatePresenter;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CommonPopupWindow;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

/**
 * ????????????
 *
 * create by zy - unknow
 *
 * & update by guohao - 2019/5/7
 *      ?????????????????????????????????????????????????????????
 */
public class CargoHandlingActivity extends BaseActivity implements GetScooterListInfoContract.getScooterListInfoView, ScooterInfoListContract.scooterInfoListView, ScooterOperateContract.scooterOperateView {

    @BindView(R.id.tv_flight_number)
    TextView tvFlightNumber;
    @BindView(R.id.tv_arrive_time)
    TextView tvArriveTime;
    @BindView(R.id.tv_plane_info)
    TextView tvPlaneInfo;

    @BindView(R.id.srv_cargo_handling_facility)
    RecyclerView slideRecyclerView;
    @BindView(R.id.srv_cargo_handling_waybill)
    SlideRecyclerView waybillSlideRecyclerView;
    @BindView(R.id.rv_cargo_cabin)
    RecyclerView recyclerView;

    @BindView(R.id.my_tablayout)
    TabLayout tabLayout;

    //????????????????????????
    private List <FlightCabinInfo.AircraftNoRSBean.CargosBean> listCargoCabinInfo = new ArrayList <>();
    //??????????????????????????????
    private List <FlightCabinInfo.AircraftNoRSBean.CargosBean> listCargoCabinInfoShow = new ArrayList <>();
    private CargoCabinAdapter mCargoCabinAdapter;

    //?????????????????? -- ?????????
    private List<FtRuntimeFlightScooter> listHandcar = new ArrayList<>();
    private List<FtRuntimeFlightScooter> listHandcar_ORIGIN = new ArrayList<>();//?????????

    //???????????????
    private CargoHandlingAdapter mCargoHandlingAdapter;

    //?????? ???????????????????????? -- ?????????
    private List<FtGroupScooter> listWaybill = new ArrayList<>();
    private List<FtGroupScooter> listWaybill_ORIGIN = new ArrayList<>();//?????????
    //????????????  ?????????
    private CargoHandlingWaybillAdapter mCargoHandlingWaybillAdapter;

    //???????????????????????????????????????
    private List<FtGroupScooter> listDeleteNo = new ArrayList<>();
    //????????????????????????????????????
    private List<FtGroupScooter> listDeleteYes = new ArrayList<>();

    private int nowHandcarPosition;//????????????????????? ??????

    private int nowHandcarPositionSelect = -1;//??????????????????

    private int nowWaybillPosition;//????????????????????? ??????????????????

    //?????????
    private FlightCabinInfo flightInfo;//????????????

    private String CURRENT_FLIGHT_COURSE_EN = "";//???????????????????????????

    private String taskId = null;//????????????ID
    private String flightId = null;//????????????id
    private String  taskTypeCode = null;


    private CommonPopupWindow window;
    RecyclerView dataRc;
    ImageView ivClose;
    TextView tvTitle;
    private boolean isPopWindow = false;
    private int flag = -1;//0 ?????????1 ??????

    //????????????
    private CabinAdapter mSpProductAdapter;


    public static void startActivity(Context context, String taskId, String flightId,String taskTypeCode) {
        Intent intent = new Intent(context, CargoHandlingActivity.class);
        intent.putExtra("taskId", taskId);
        intent.putExtra("flightId", flightId);
        intent.putExtra("taskTypeCode", taskTypeCode);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_cargo_handling;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        initView();
        initData();
        initPopupWindow();
        initPopupWindowCabin();
    }


    private void initData() {

        taskId = getIntent().getStringExtra("taskId");
        flightId = getIntent().getStringExtra("flightId");
        taskTypeCode = getIntent().getStringExtra("taskTypeCode");
        mPresenter = new GetScooterListInfoPresenter(this);
        GetScooterListInfoEntity mGetScooterListInfoEntity = new GetScooterListInfoEntity();
        mGetScooterListInfoEntity.setFlightId(flightId);
        mGetScooterListInfoEntity.setTaskId(taskId);
        ((GetScooterListInfoPresenter) mPresenter).getScooterListInfo(mGetScooterListInfoEntity);

    }

    private void initView() {
        setToolbarShow(View.VISIBLE);
        CustomToolbar toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "??????");

        //??????????????????
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //????????????
                screenDataByCourseCn(tab.getTag().toString());
                //?????????????????????????????????
                CURRENT_FLIGHT_COURSE_EN = tab.getTag().toString();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setIsBack(true, () -> showBackDialog());

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_inset));
//        slideRecyclerView.addItemDecoration(itemDecoration);
        //????????????
        slideRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mCargoHandlingAdapter = new CargoHandlingAdapter(listHandcar);
        slideRecyclerView.setAdapter(mCargoHandlingAdapter);
        //????????????
        mSpProductAdapter = new CabinAdapter(listCargoCabinInfo);
        mSpProductAdapter.setOnItemClickListener((adapter, view, position) -> {

            listHandcar.get(nowHandcarPositionSelect).setSuggestRepository(listCargoCabinInfo.get(position).getPos());
            mCargoHandlingAdapter.notifyDataSetChanged();
            dismissPopWindowsCabin();

        });
        mCargoHandlingAdapter.setOnSpinnerClickLister((view, position) ->
                {
                    if (!isPopWindow)
                        showPopWindowListCabin(position);

                });
        mCargoHandlingAdapter.setOnLockClickListener((view, position) -> {
            if (listHandcar.get(position).isLock()) {

                listHandcar.get(position).setLock(false);
                nowHandcarPositionSelect = -1;
            } else {
                nowHandcarPositionSelect = position;
                listHandcar.get(position).setLock(true);
            }
            mCargoHandlingAdapter.notifyDataSetChanged();
        });
        //???????????? ??????????????? ????????? ???????????? ?????? ???????????? ???????????? ?????? ????????????
        mCargoHandlingAdapter.setOnDeleteClickListener((view, position) ->
                {
                    if (listHandcar.get(position).isLock()) {
                        ToastUtil.showToast("??????????????????????????????");
                        return;
                    }
                    List<FtGroupScooter> listRcInfo = new ArrayList<>();
                    for (FtGroupScooter mFtGroupScooter : listHandcar.get(position).getGroupScooters()) {
                        if (mFtGroupScooter.getInFlight() == 1)
                            listDeleteNo.add(mFtGroupScooter);
                        else
                            listRcInfo.add(mFtGroupScooter);
                    }
//                    slideRecyclerView.closeMenu();
                    listHandcar.remove(position);
                    listHandcar_ORIGIN.remove(position);
                    mCargoHandlingAdapter.notifyDataSetChanged();
                    comparePullDownListOfWaybillList(listRcInfo);

                }
        );
        mCargoHandlingAdapter.setOnItemClickListener((adapter, v, position) -> {
            nowHandcarPositionSelect = position;
            nowHandcarPosition = position;
            //??????popwindow??? ???????????????????????????
            if (isPopWindow) {
                if (flag == 0)
                    subpackage(position);
                else
                    allIn(position);

                dismissPopWindows();
            } else {
                HandcarDetailsActivity.startActivity(CargoHandlingActivity.this, listHandcar.get(position));
            }

        });
        //????????????
        waybillSlideRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        waybillSlideRecyclerView.addItemDecoration(itemDecoration);
        mCargoHandlingWaybillAdapter = new CargoHandlingWaybillAdapter(listWaybill);
        waybillSlideRecyclerView.setAdapter(mCargoHandlingWaybillAdapter);
        //??????
        mCargoHandlingWaybillAdapter.setOnSubpackageClickListener((view, position) -> {
            if (!CURRENT_FLIGHT_COURSE_EN.equals(listWaybill.get(position).getToCityEn())){
                ToastUtil.showToast("???????????????????????????");
                return;
            }
            flag = 0;
            showPopWindowList(position);

        });
        //??????
        mCargoHandlingWaybillAdapter.setOnAllClickListener((view, position) -> {
            if (!CURRENT_FLIGHT_COURSE_EN.equals(listWaybill.get(position).getToCityEn())){
                ToastUtil.showToast("???????????????????????????");
                return;
            }
            flag = 1;
            showPopWindowList(position);
        });
        mCargoHandlingWaybillAdapter.setOnItemClickListener((adapter, v, position) -> {
        });
        //????????????
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mCargoCabinAdapter = new CargoCabinAdapter(listCargoCabinInfoShow);
        recyclerView.setAdapter(mCargoCabinAdapter);
        recyclerView.setNestedScrollingEnabled(false);

    }

    /**
     * ?????????popWindow
     */
    private void initPopupWindow() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenHeight = metrics.heightPixels;

        window = new CommonPopupWindow(this, R.layout.popup_uld_list, ViewGroup.LayoutParams.MATCH_PARENT, (int) (screenHeight * 0.7)) {
            @Override
            protected void initView() {
                View view = getContentView();
                dataRc = view.findViewById(R.id.rc_handcar);
                tvTitle = view.findViewById(R.id.tv_title);
                ivClose = view.findViewById(R.id.iv_close);
                dataRc.setLayoutManager(new LinearLayoutManager(CargoHandlingActivity.this, LinearLayoutManager.VERTICAL, false));

                ivClose.setOnClickListener((v) -> {
                    dismissPopWindows();
                });
            }

            @Override
            protected void initEvent() {

            }

            @Override
            protected void initWindow() {
                super.initWindow();
                PopupWindow instance = getPopupWindow();
                instance.setOnDismissListener(() -> {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 1.0f;
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    getWindow().setAttributes(lp);
                    isPopWindow = false;
                });
            }
        };
    }

    private void showPopWindowList(int position) {

        tvTitle.setText("?????????????????????");
        dataRc.setAdapter(mCargoHandlingAdapter);
        nowWaybillPosition = position;
        isPopWindow = true;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        window.getPopupWindow().setAnimationStyle(R.style.animTranslate);
        window.showAtLocation(waybillSlideRecyclerView, Gravity.BOTTOM, 0, 0);
    }

    private void dismissPopWindows() {

        window.getPopupWindow().dismiss();

    }

    /**
     * ?????????popWindow
     */
    private void initPopupWindowCabin() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenHeight = metrics.heightPixels;

        window = new CommonPopupWindow(this, R.layout.popup_uld_list, ViewGroup.LayoutParams.MATCH_PARENT, (int) (screenHeight * 0.7)) {
            @Override
            protected void initView() {
                View view = getContentView();
                dataRc = view.findViewById(R.id.rc_handcar);
                tvTitle = view.findViewById(R.id.tv_title);
                ivClose = view.findViewById(R.id.iv_close);
                dataRc.setLayoutManager(new LinearLayoutManager(CargoHandlingActivity.this, LinearLayoutManager.VERTICAL, false));
                dataRc.setAdapter(mSpProductAdapter);

                ivClose.setOnClickListener((v) -> {
                    dismissPopWindows();
                });
            }

            @Override
            protected void initEvent() {

            }

            @Override
            protected void initWindow() {
                super.initWindow();
                PopupWindow instance = getPopupWindow();
                instance.setOnDismissListener(() -> {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 1.0f;
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    getWindow().setAttributes(lp);
//                    nowHandcarPositionSelect = -1;
                    isPopWindow = false;
                });
            }
        };
    }

    private void showPopWindowListCabin(int position) {
        tvTitle.setText("?????????????????????");
        isPopWindow = true;
        dataRc.setAdapter(mSpProductAdapter);
        nowHandcarPositionSelect = position;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        window.getPopupWindow().setAnimationStyle(R.style.animTranslate);
        window.showAtLocation(waybillSlideRecyclerView, Gravity.BOTTOM, 0, 0);
    }

    private void dismissPopWindowsCabin() {

        window.getPopupWindow().dismiss();

    }

    private void allIn(int position) {
        if (-1 == nowHandcarPositionSelect) {
            ToastUtil.showToast(CargoHandlingActivity.this, "????????????????????????");
            return;
        } else if (listHandcar.get(position).isLock()) {
            ToastUtil.showToast("?????????????????????????????????");
            return;
        }
        rcinfoToHandcar(listWaybill.get(nowWaybillPosition), flag);
        waybillSlideRecyclerView.closeMenu();
    }

    private void subpackage(int position) {

        if (-1 == nowHandcarPositionSelect) {
            ToastUtil.showToast("????????????????????????");
            return;
        } else if (listHandcar.get(position).isLock()) {
            ToastUtil.showToast("?????????????????????????????????");
            return;
        } else if (listWaybill.get(nowWaybillPosition).getNumber() <= 1) {
            ToastUtil.showToast("??????????????????????????????");
            return;
        }

        waybillSlideRecyclerView.closeMenu();
        SubPackageWaybillActivity.startActivity(CargoHandlingActivity.this, listWaybill.get(nowWaybillPosition));
    }


    @OnClick({R.id.tv_add, R.id.btn_sure_cargo_handing, R.id.btn_sure_cargo_return})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add:
                ScanManagerActivity.startActivity(this,"CargoHandlingActivity");
                break;
            case R.id.btn_sure_cargo_handing:
                boolean isOK = true;
                for (FtRuntimeFlightScooter ftRuntimeFlightScooter : listHandcar) {
                    if (ftRuntimeFlightScooter.getInFlight() == 1 || ftRuntimeFlightScooter.getInFlightCourse() == 0) {
                        isOK = false;
                        break;
                    }
                }
                if (isOK)
                    submitData();
                else
                    ToastUtil.showToast( "?????????????????????????????????????????????????????????????????????");
                break;
            case R.id.btn_sure_cargo_return:
                //????????????
                BaseFilterEntity returnEntiry = new BaseFilterEntity();
                returnEntiry.setFlightId(flightId);
                returnEntiry.setTaskId(taskId);
                returnEntiry.setUserId(UserInfoSingle.getInstance().getUserId());
                mPresenter = new ScooterOperatePresenter(this);
                ((ScooterOperatePresenter) mPresenter).returnToPrematching(returnEntiry);
                break;

        }
    }

    /**
     * ??????????????????
     */
    private void submitData() {

        setProgressText("?????????????????????");
        mPresenter = new GetScooterListInfoPresenter(this);
        FightScooterSubmitEntity mFightScooterSubmitEntity = new FightScooterSubmitEntity();
        mFightScooterSubmitEntity.setFlightId(flightInfo.getFlightInfo().getId());
        mFightScooterSubmitEntity.setUserId(UserInfoSingle.getInstance().getUserId());
        mFightScooterSubmitEntity.setTaskId(taskId);//??????????????????
        mFightScooterSubmitEntity.setDeleteRedRcInfos(listDeleteNo);
        mFightScooterSubmitEntity.setDeleteRcInfos(listDeleteYes);
        mFightScooterSubmitEntity.setScooters(listHandcar_ORIGIN);
        mFightScooterSubmitEntity.setWithoutScootereRcInfos(listWaybill_ORIGIN);
        mFightScooterSubmitEntity.setCurrentStep(taskTypeCode);
        ((GetScooterListInfoPresenter) mPresenter).freightInfo(mFightScooterSubmitEntity);

    }

    /**
     * ????????????
     * @param handcarId
     */
    private void addHandcar(String handcarId) {

        mPresenter = new ScooterInfoListPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setSize(Constants.PAGE_SIZE);
        entity.setCurrent(1);
        ScooterInfoListBean scooterInfoListBean = new ScooterInfoListBean();
        scooterInfoListBean.setScooterCode(handcarId);
        entity.setFilter(scooterInfoListBean);
        ((ScooterInfoListPresenter) mPresenter).ScooterInfoList(entity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (Constants.FINISH_HANDCAR_DETAILS == resultCode) {//??????????????????
            //?????????????????????????????????
            List<FtGroupScooter> listDeleteNoTemp = (List<FtGroupScooter>) data.getSerializableExtra("listDelete");
            if (listDeleteNoTemp != null) {
                listDeleteNo.addAll(listDeleteNoTemp);
            }
            //??????/?????? ?????????????????????
            List<FtGroupScooter> listPullDown = (List<FtGroupScooter>) data.getSerializableExtra("listPullDown");
            if (listPullDown != null) {
                comparePullDownListOfWaybillList(listPullDown);
            }
            listHandcar.set(nowHandcarPosition, (FtRuntimeFlightScooter) data.getSerializableExtra("FtRuntimeFlightScooter"));
            mCargoHandlingAdapter.notifyDataSetChanged();

        } else if (Constants.FINISH_SUBPACKAGE == resultCode) {//????????????

            FtGroupScooter mRcInfoEd = (FtGroupScooter) data.getSerializableExtra("mRcInfoEd");
            FtGroupScooter mRcInfo = (FtGroupScooter) data.getSerializableExtra("mRcInfo");
            listWaybill.set(nowWaybillPosition, mRcInfoEd);
            mCargoHandlingWaybillAdapter.notifyDataSetChanged();

            //????????????????????? ??????
            if (nowHandcarPositionSelect != -1) {
                rcinfoToHandcar(mRcInfo, flag);
            }
        } else if (Constants.SCAN_RESULT == resultCode) {

            addHandcar(data.getStringExtra(Constants.SACN_DATA));

        }
    }

    /**
     * ??????????????????
     *
     * @param flightInfo
     */
    private void setFlightInfo(FlightCabinInfo flightInfo) {
        tvFlightNumber.setText(flightInfo.getFlightInfo().getFlightNo());
        tvArriveTime.setText(TimeUtils.date2Tasktime3(flightInfo.getFlightInfo().getEtd()) + "(" + TimeUtils.getDay(flightInfo.getFlightInfo().getEtd()) + ")");

        //??????????????????????????????
        String flightCabinInfo = flightInfo.getAircraftTypes().getTypeName()
                + " | " + flightInfo.getFlightInfo().getAircraftNo() + " | 0-" + flightInfo.getAircraftNoRS().getFlightMaxWgt() + "KG";
        tvPlaneInfo.setText(flightCabinInfo);
        listCargoCabinInfo.clear();
        listCargoCabinInfo.addAll(flightInfo.getAircraftNoRS().getCargos());
        listCargoCabinInfoShow.clear();
        for (int i = 0; i < 5; i++) {
            FlightCabinInfo.AircraftNoRSBean.CargosBean mCargosBean = new FlightCabinInfo.AircraftNoRSBean.CargosBean();
            switch (i) {
                case 0:
                    mCargosBean.setPos("1H");
                    mCargosBean.setHldVol(flightInfo.getAircraftNoRS().getHld1vol());
                    mCargosBean.setHldMaxWgt(flightInfo.getAircraftNoRS().getHld1maxWgt());
                    break;
                case 1:
                    mCargosBean.setPos("2H");
                    mCargosBean.setHldVol(flightInfo.getAircraftNoRS().getHld2vol());
                    mCargosBean.setHldMaxWgt(flightInfo.getAircraftNoRS().getHld2maxWgt());
                    break;
                case 2:
                    mCargosBean.setPos("3H");
                    mCargosBean.setHldVol(flightInfo.getAircraftNoRS().getHld3vol());
                    mCargosBean.setHldMaxWgt(flightInfo.getAircraftNoRS().getHld3maxWgt());
                    break;
                case 3:
                    mCargosBean.setPos("4H");
                    mCargosBean.setHldVol(flightInfo.getAircraftNoRS().getHld4vol());
                    mCargosBean.setHldMaxWgt(flightInfo.getAircraftNoRS().getHld4maxWgt());
                    break;
                case 4:
                    mCargosBean.setPos("5H");
                    mCargosBean.setHldVol(flightInfo.getAircraftNoRS().getHld5vol());
                    mCargosBean.setHldMaxWgt(flightInfo.getAircraftNoRS().getHld5maxWgt());
                    break;
            }

            listCargoCabinInfoShow.add(mCargosBean);
        }

        mCargoCabinAdapter.notifyDataSetChanged();
    }

    /**
     * ????????? ?????? ????????????????????? ??????????????????  ??????????????? ?????? ????????? ??????
     *
     * @param flag    0 ?????? 1 ??????
     * @param mRcInfo
     */
    private void rcinfoToHandcar(FtGroupScooter mRcInfo, int flag) {

        listHandcar.get(nowHandcarPositionSelect).setTotal(listHandcar.get(nowHandcarPositionSelect).getTotal() + mRcInfo.getNumber());
        listHandcar.get(nowHandcarPositionSelect).setWeight(listHandcar.get(nowHandcarPositionSelect).getWeight() + mRcInfo.getWeight());
        listHandcar.get(nowHandcarPositionSelect).setVolume(listHandcar.get(nowHandcarPositionSelect).getVolume() + mRcInfo.getVolume());

        // ?????????????????????id  ????????????????????????
        String strOnly1 = mRcInfo.getWaybillId();

        for (FtGroupScooter handcarRcInfo : listHandcar.get(nowHandcarPositionSelect).getGroupScooters()) {

            String strOnly2 = handcarRcInfo.getWaybillId();
            if (strOnly1.equals(strOnly2)) {
                handcarRcInfo.setWeight(handcarRcInfo.getWeight() + mRcInfo.getWeight());
                handcarRcInfo.setVolume(handcarRcInfo.getVolume() + mRcInfo.getVolume());
                handcarRcInfo.setNumber(handcarRcInfo.getNumber() + mRcInfo.getNumber());

                //????????? ?????? ??????????????????????????????????????????2 ???????????????????????????
                if (flag == 1) {
                    if (2 != mRcInfo.getUpdateStatus()) {
                        listDeleteYes.add(mRcInfo);
                    }
                    listWaybill.remove(mRcInfo);
                    listWaybill_ORIGIN.remove(mRcInfo);
                    mCargoHandlingWaybillAdapter.notifyDataSetChanged();
                }
                // ??????????????????????????? ?????????????????? ????????? ????????????
                if (2 != handcarRcInfo.getUpdateStatus()) {
                    handcarRcInfo.setUpdateStatus((short) 1);
                }

                mRcInfo = null;
                break;
            }
        }
        //?????????
        //??????
        if (flag == 1) {
            //????????????????????????????????? ?????????  ???????????? ???????????? ????????????
            if (null != mRcInfo) {
                if (2 != mRcInfo.getUpdateStatus()) {
                    mRcInfo.setUpdateStatus((short) 1);
                }
                listHandcar.get(nowHandcarPositionSelect).getGroupScooters().add(mRcInfo);
                listWaybill.remove(mRcInfo);
                mCargoHandlingWaybillAdapter.notifyDataSetChanged();
            }

        } else {//??????
            //????????????????????????????????? ?????????  ???????????? ????????? ?????????????????? ????????????
            if (null != mRcInfo) {
                mRcInfo.setUpdateStatus((short) 2);
                listHandcar.get(nowHandcarPositionSelect).getGroupScooters().add(mRcInfo);
            }
        }
        mCargoHandlingAdapter.notifyDataSetChanged();
    }

    /**
     * ?????????????????????????????????????????? ??? ???????????????????????? ????????? ????????????????????? ????????????
     * ????????? ???????????????????????????????????????????????? ??????????????????????????????
     * ????????? ??? ???????????????????????? ?????? ???????????????????????? ?????????????????????????????? ?????????????????????????????????????????????????????????
     */
    private void comparePullDownListOfWaybillList(List <FtGroupScooter> listPullDown) {

        Map <String, FtGroupScooter> mapTemp = new HashMap <>();
        //??????list ????????????
        listWaybill.addAll(listPullDown);
        for (FtGroupScooter info1 : listWaybill) {
            //????????????id ??????
            String key = info1.getWaybillId();

            if (null == mapTemp.get(key))
                mapTemp.put(key, info1);
            else {
                int baseCount = mapTemp.get(key).getNumber();
                double baseWeight = mapTemp.get(key).getWeight();
                double baseVolume = mapTemp.get(key).getVolume();
                mapTemp.get(key).setNumber(info1.getNumber() + baseCount);
                mapTemp.get(key).setWeight(info1.getWeight() + baseWeight);
                mapTemp.get(key).setVolume(info1.getVolume() + baseVolume);
                //????????????id??????????????????????????????????????? ?????? ??????????????????????????? ???????????????????????????
                if (null != info1.getId()) {
                    listDeleteYes.add(info1);
                }
                //???????????????????????????????????????????????? ???????????????????????? ?????? ??????
                if (2 != mapTemp.get(key).getUpdateStatus()) {
                    mapTemp.get(key).setUpdateStatus((short) 1);
                }
            }
        }
        listWaybill.clear();
        listWaybill.addAll(new ArrayList <FtGroupScooter>(mapTemp.values()));
        mCargoHandlingWaybillAdapter.notifyDataSetChanged();
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param scooterListInfoBean
     */
    @Override
    public void getScooterListInfoResult(GetScooterListInfoBean scooterListInfoBean) {
        if (scooterListInfoBean != null) {
            //???????????? ??????
            listHandcar_ORIGIN = scooterListInfoBean.getScooters();
            //????????????????????? inFlight ?????????
            for (FtGroupScooter mFtGroupScooter:scooterListInfoBean.getWithoutScootereRcInfos()){
                if (mFtGroupScooter.getInFlight() == null)
                    mFtGroupScooter.setInFlight((short)0);
//                mFtGroupScooter.setNumber(5);
//                mFtGroupScooter.setWeight(50d);
//                mFtGroupScooter.setVolume(20d);
            }
            listWaybill_ORIGIN = scooterListInfoBean.getWithoutScootereRcInfos();
            //???????????? ??????
            flightInfo = scooterListInfoBean.getFlight();

            //???????????? ??????
            CURRENT_FLIGHT_COURSE_EN = flightInfo.getFlightInfo().getFlightCourseByAndroid().get(1);

            //???????????????UI
            String flightPartStr = flightInfo.getFlightInfo().getFlightCourseCn().get(0);

                for (int i = 1; i < flightInfo.getFlightInfo().getFlightCourseCn().size(); i++) {
                    flightPartStr +=  "-" + flightInfo.getFlightInfo().getFlightCourseCn().get(i);
                    TabLayout.Tab tab = tabLayout.newTab().setText(flightPartStr).setTag(flightInfo.getFlightInfo().getFlightCourseByAndroid().get(i));
                    tabLayout.addTab(tab);
                }
            //????????????????????????????????????????????????RecyclerView??????????????????????????????
            screenDataByCourseCn(CURRENT_FLIGHT_COURSE_EN);

//            listHandcar.addAll(scooterListInfoBean.getScooters());//????????????
//            listWaybill.addAll(scooterListInfoBean.getWithoutScootereRcInfos());//????????????
//            mCargoHandlingWaybillAdapter.notifyDataSetChanged();
//            mCargoHandlingAdapter.notifyDataSetChanged();
            //????????????
            mCargoCabinAdapter.notifyDataSetChanged();
            if (flightInfo != null)
                setFlightInfo(flightInfo);
        }
    }


    @Override
    public void fightScooterSubmitResult(String result) {

        if (null != result) {

            ToastUtil.showToast(this, result);
            EventBus.getDefault().post("CargoHandlingActivity_refresh");
            finish();
        }
    }

    @Override
    public void scooterInfoListResult(List<ScooterInfoListBean> scooterInfoListBeans) {
        if (null != scooterInfoListBeans) {

            //????????????  ???????????????
            ScooterInfoListBean mScooterInfoListBean = scooterInfoListBeans.get(0);
            if (null != mScooterInfoListBean){
                FtRuntimeFlightScooter mFtRuntimeFlightScooter = new FtRuntimeFlightScooter();
                mFtRuntimeFlightScooter.setScooterId(mScooterInfoListBean.getId());
                mFtRuntimeFlightScooter.setScooterCode(mScooterInfoListBean.getScooterCode());
                mFtRuntimeFlightScooter.setScooterWeight(mScooterInfoListBean.getScooterWeight());
                //??????????????? ?????? ?????????
                mFtRuntimeFlightScooter.setId(Tools.generateUniqueKey());
                mFtRuntimeFlightScooter.setUldId("");
                mFtRuntimeFlightScooter.setUldType("");
                mFtRuntimeFlightScooter.setUldCode("");
                mFtRuntimeFlightScooter.setUldWeight(0);
//                mFtRuntimeFlightScooter.setScooterType(mScooterInfoListBean.getScooterType());
                mFtRuntimeFlightScooter.setDelFlag(mScooterInfoListBean.getDelFlag());
//                mFtRuntimeFlightScooter.setCreateDate(mScooterInfoListBean.getCreateDate());
//                mFtRuntimeFlightScooter.setCreateUser(mScooterInfoListBean.getCreateUser());
//                mFtRuntimeFlightScooter.setUpdateDate(mScooterInfoListBean.getUpdateDate());
//                mFtRuntimeFlightScooter.setUpdateUser(mScooterInfoListBean.getUpdateUser());
                mFtRuntimeFlightScooter.setGroupScooters(new ArrayList <FtGroupScooter>());
                mFtRuntimeFlightScooter.setWeight((double)0);
                mFtRuntimeFlightScooter.setVolume((double)0);
                mFtRuntimeFlightScooter.setTotal(0);
                mFtRuntimeFlightScooter.setInFlight((short)0);
                mFtRuntimeFlightScooter.setInFlightCourse(1);
                mFtRuntimeFlightScooter.setUpdateStatus((short)2);
                mFtRuntimeFlightScooter.setToCityEn(CURRENT_FLIGHT_COURSE_EN);//????????????????????????????????????

                listHandcar.add(mFtRuntimeFlightScooter);
                listHandcar_ORIGIN.add(mFtRuntimeFlightScooter);
                mCargoHandlingAdapter.notifyDataSetChanged();
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

    @Override
    public void toastView(String error) {

        ToastUtil.showToast(this, error);
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
     * ????????? ???????????? dialog
     */
    private void showBackDialog() {

        finish();

    }

    /**
     * ??????????????? ??????
     *
     * @param result
     */
    @Override
    public void returnToPrematching(Object result) {
        ToastUtil.showToast(result.toString());
        EventBus.getDefault().post("CargoHandlingActivity_refresh");
        Log.e("dime", "returnToPrematching:" + result);
        finish();
    }

    /**
     * ??????????????????????????????
     *
     * @param courseEn ??????????????? toCityEn
     */
    private void screenDataByCourseCn(String courseEn) {
        //????????????????????? listWaybill
        listWaybill.clear();
        for (FtGroupScooter data : listWaybill_ORIGIN) {

            if (data.getToCityEn() == null || data.getToCityEn().equals(courseEn)) {
                listWaybill.add(data);
            }
        }

        //????????? ???????????? listHandcar
        listHandcar.clear();
        for (FtRuntimeFlightScooter data : listHandcar_ORIGIN) {
            if (data.getToCityEn() == null || data.getToCityEn().equals(courseEn)) {
                listHandcar.add(data);
            }
        }

        //????????????
        waybillSlideRecyclerView.getAdapter().notifyDataSetChanged();
        mCargoHandlingAdapter.notifyDataSetChanged();
    }
}
