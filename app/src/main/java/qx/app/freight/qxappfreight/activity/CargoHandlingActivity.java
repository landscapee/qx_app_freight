package qx.app.freight.qxappfreight.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

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
import qx.app.freight.qxappfreight.adapter.GeneralSpinnerAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.FightScooterSubmitEntity;
import qx.app.freight.qxappfreight.bean.request.GeneralSpinnerBean;
import qx.app.freight.qxappfreight.bean.request.GetScooterListInfoEntity;
import qx.app.freight.qxappfreight.bean.response.ExistBean;
import qx.app.freight.qxappfreight.bean.response.FtGroupScooter;
import qx.app.freight.qxappfreight.bean.response.FtRuntimeFlightScooter;
import qx.app.freight.qxappfreight.bean.response.GetScooterListInfoBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.GetScooterListInfoContract;
import qx.app.freight.qxappfreight.contract.ScooterInfoListContract;
import qx.app.freight.qxappfreight.presenter.GetScooterListInfoPresenter;
import qx.app.freight.qxappfreight.presenter.ScooterInfoListPresenter;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CommonPopupWindow;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

/**
 * 理货页面
 */
public class CargoHandlingActivity extends BaseActivity implements GetScooterListInfoContract.getScooterListInfoView, ScooterInfoListContract.scooterInfoListView {

    @BindView(R.id.tv_flight_number)
    TextView tvFlightNumber;
    @BindView(R.id.tv_arrive_time)
    TextView tvArriveTime;
    @BindView(R.id.tv_plane_info)
    TextView tvPlaneInfo;

    @BindView(R.id.srv_cargo_handling_facility)
    SlideRecyclerView slideRecyclerView;
    @BindView(R.id.srv_cargo_handling_waybill)
    SlideRecyclerView waybillSlideRecyclerView;
    @BindView(R.id.rv_cargo_cabin)
    RecyclerView recyclerView;

    //货物舱位列表
    private List <String> listCargoCabinInfo = new ArrayList <>();
    private CargoCabinAdapter mCargoCabinAdapter;
    //带货板车列表
    private List <FtRuntimeFlightScooter> listHandcar = new ArrayList <>();
    private CargoHandlingAdapter mCargoHandlingAdapter;
    //无板运单收运记录列表
    private List <FtGroupScooter> listWaybill = new ArrayList <>();
    private CargoHandlingWaybillAdapter mCargoHandlingWaybillAdapter;

    //删除不是本航班收运记录列表
    private List <FtGroupScooter> listDeleteNo = new ArrayList <>();
    //删除是本航班收运记录列表
    private List <FtGroupScooter> listDeleteYes = new ArrayList <>();

    private int nowHandcarPosition;//当前点击进入的 板车

    private int nowHandcarPositionSelect = -1;//当前选中板车

    private int nowWaybillPosition;//当前点击进入的 无板收运记录

    private GetScooterListInfoBean.FlightBean flightInfo;//航班信息

    private String taskId = null;//待办任务ID
    private String flightId = null;//待办航班id

    private CommonPopupWindow window;
    RecyclerView dataRc;
    ImageView ivClose;
    TextView tvTitle;
    private boolean isPopWindow = false;
    private int flag = -1;//0 分装，1 全装

    //选择仓位
    private CabinAdapter mSpProductAdapter;


    public static void startActivity(Context context, String taskId, String flightId) {
        Intent intent = new Intent(context, CargoHandlingActivity.class);
        intent.putExtra("taskId", taskId);
        intent.putExtra("flightId", flightId);
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



        mPresenter = new GetScooterListInfoPresenter(this);
        GetScooterListInfoEntity mGetScooterListInfoEntity = new GetScooterListInfoEntity();
        mGetScooterListInfoEntity.setFlightId(flightId);
        ((GetScooterListInfoPresenter) mPresenter).getScooterListInfo(mGetScooterListInfoEntity);

//        //模拟 带货板车数据
//        for (int i = 0; i < 5; i++) {
//            FtRuntimeFlightScooter mFtRuntimeFlightScooter = new FtRuntimeFlightScooter();
//            List <RcInfo> mRcInfos = new ArrayList <>();
//            for (int j = 0; j < 5; j++) {
//                RcInfo mRcInfo = new RcInfo();
//                mRcInfo.setId("456" + j);
//                mRcInfo.setNumber(j+1);
//                mRcInfo.setWeight((double) 2);
//                mRcInfo.setVolume((double) 2);
//                mRcInfo.setWaybillId("123" + j);
//                mRcInfo.setWaybillCode("123" + j);
//                mRcInfo.setUpdateStatus((short) 0);
//                if (j == 1)
//                    mRcInfo.setInFlight((short) 1);
//                else
//                    mRcInfo.setInFlight((short) 0);
//                mRcInfos.add(mRcInfo);
//            }
//            mFtRuntimeFlightScooter.setScooterCode("00000" + i);
//            mFtRuntimeFlightScooter.setRcInfoList(mRcInfos);
//            mFtRuntimeFlightScooter.setSelect(false);
//            mFtRuntimeFlightScooter.setWeight((double)2);
//            mFtRuntimeFlightScooter.setVolume((double)2);
//            mFtRuntimeFlightScooter.setInFlight((short)1);
//            listHandcar.add(mFtRuntimeFlightScooter);
//        }
//        mCargoHandlingAdapter.notifyDataSetChanged();
//
//        //模拟无板运单数据
//        for (int j = 0; j < 5; j++) {
//            RcInfo mRcInfo = new RcInfo();
//            mRcInfo.setId("456" + j);
//            mRcInfo.setNumber(j+1);
//            mRcInfo.setWeight((double) 2);
//            mRcInfo.setVolume((double) 2);
//            mRcInfo.setWaybillId("1234" + j);
//            mRcInfo.setWaybillCode("123" + j);
//            mRcInfo.setUpdateStatus((short) 0);
//            mRcInfo.setInFlight((short) 0);
//            listWaybill.add(mRcInfo);
//        }
//        mCargoHandlingWaybillAdapter.notifyDataSetChanged();

    }

    private void initView() {
        setToolbarShow(View.VISIBLE);
        CustomToolbar toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "理货");

        setIsBack(true, () -> showBackDialog());

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_inset));
        slideRecyclerView.addItemDecoration(itemDecoration);
        //理货设备
        slideRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mCargoHandlingAdapter = new CargoHandlingAdapter(listHandcar);
        slideRecyclerView.setAdapter(mCargoHandlingAdapter);
        //选择仓位
        mSpProductAdapter = new CabinAdapter(listCargoCabinInfo);
        mSpProductAdapter.setOnItemClickListener((adapter, view, position) -> {

            listHandcar.get(nowHandcarPositionSelect).setSuggestRepository(listCargoCabinInfo.get(position));
            mCargoHandlingAdapter.notifyDataSetChanged();
            dismissPopWindowsCabin();

        });
        mCargoHandlingAdapter.setOnSpinnerClickLister((view, position) ->
                {
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
        //删除板车 将板车上的 本航班 分裝记录 拉下 非本航班 分裝记录 加入 删除列表
        mCargoHandlingAdapter.setOnDeleteClickListener((view, position) ->
                {
                    if (listHandcar.get(position).isLock()) {
                        ToastUtil.showToast("被锁住的板车不能删除");
                        return;
                    }
                    List <FtGroupScooter> listRcInfo = new ArrayList <>();
                    for (FtGroupScooter mFtGroupScooter : listHandcar.get(position).getGroupScooters()) {
                        if (mFtGroupScooter.getInFlight() == 1)
                            listDeleteNo.add(mFtGroupScooter);
                        else
                            listRcInfo.add(mFtGroupScooter);
                    }
                    slideRecyclerView.closeMenu();
                    listHandcar.remove(position);
                    mCargoHandlingAdapter.notifyDataSetChanged();
                    comparePullDownListOfWaybillList(listRcInfo);

                }
        );
        mCargoHandlingAdapter.setOnItemClickListener((adapter, v, position) -> {
            nowHandcarPositionSelect = position;
            nowHandcarPosition = position;
            //弹出popwindow后 板车列表的点击事件
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
        //无板运单
        waybillSlideRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        waybillSlideRecyclerView.addItemDecoration(itemDecoration);
        mCargoHandlingWaybillAdapter = new CargoHandlingWaybillAdapter(listWaybill);
        waybillSlideRecyclerView.setAdapter(mCargoHandlingWaybillAdapter);
        //分装
        mCargoHandlingWaybillAdapter.setOnSubpackageClickListener((view, position) -> {
            flag = 0;
            showPopWindowList(position);

        });
        //全装
        mCargoHandlingWaybillAdapter.setOnAllClickListener((view, position) -> {
            flag = 1;
            showPopWindowList(position);
        });
        mCargoHandlingWaybillAdapter.setOnItemClickListener((adapter, v, position) -> {
        });
        //仓位信息
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mCargoCabinAdapter = new CargoCabinAdapter(listCargoCabinInfo);
        recyclerView.setAdapter(mCargoCabinAdapter);
        recyclerView.setNestedScrollingEnabled(false);

    }

    /**
     * 初始化popWindow
     */
    private void initPopupWindow() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenHeight = metrics.heightPixels;

        window = new CommonPopupWindow(this, R.layout.popup_list, ViewGroup.LayoutParams.MATCH_PARENT, (int) (screenHeight * 0.7)) {
            @Override
            protected void initView() {
                View view = getContentView();
                dataRc = view.findViewById(R.id.rc_handcar);
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
     * 初始化popWindow
     */
    private void initPopupWindowCabin() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenHeight = metrics.heightPixels;

        window = new CommonPopupWindow(this, R.layout.popup_list, ViewGroup.LayoutParams.MATCH_PARENT, (int) (screenHeight * 0.7)) {
            @Override
            protected void initView() {
                View view = getContentView();
                dataRc = view.findViewById(R.id.rc_handcar);
                tvTitle = view.findViewById(R.id.tv_title);
                ivClose = view.findViewById(R.id.iv_close);
                dataRc.setLayoutManager(new LinearLayoutManager(CargoHandlingActivity.this, LinearLayoutManager.VERTICAL, false));
                dataRc.setAdapter(mSpProductAdapter);
                tvTitle.setText("请选择一个仓位");
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
            ToastUtil.showToast(CargoHandlingActivity.this, "请先选择一个板车");
            return;
        } else if (listHandcar.get(position).isLock()) {
            ToastUtil.showToast("该板车已被上锁无法操作");
            return;
        }
        rcinfoToHandcar(listWaybill.get(nowWaybillPosition), flag);
        waybillSlideRecyclerView.closeMenu();
    }

    private void subpackage(int position) {

        if (-1 == nowHandcarPositionSelect) {
            ToastUtil.showToast("请先选择一个板车");
            return;
        } else if (listHandcar.get(position).isLock()) {
            ToastUtil.showToast("该板车已被上锁无法操作");
            return;
        } else if (listWaybill.get(nowWaybillPosition).getNumber() <= 1) {
            ToastUtil.showToast("该收运记录无法再拆分");
            return;
        }

        waybillSlideRecyclerView.closeMenu();
        SubPackageWaybillActivity.startActivity(CargoHandlingActivity.this, listWaybill.get(nowWaybillPosition));
    }



    @OnClick({R.id.tv_add, R.id.btn_sure_cargo_handing})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add:
                ScanManagerActivity.startActivity(this);
                break;
            case R.id.btn_sure_cargo_handing:
                boolean isOK = true;
                for (FtRuntimeFlightScooter ftRuntimeFlightScooter : listHandcar) {
                    if (ftRuntimeFlightScooter.getInFlight() == 1) {
                        isOK = false;
                        break;
                    }
                }
                if (isOK)
                    submitData();
                else
                    ToastUtil.showToast( "板车上还有其他航班数据，请拉下后再提交！");
                break;

        }
    }

    /**
     * 提交理货数据
     */
    private void submitData() {

        setProgressText("数据提交中……");
        mPresenter = new GetScooterListInfoPresenter(this);
        FightScooterSubmitEntity mFightScooterSubmitEntity = new FightScooterSubmitEntity();
        mFightScooterSubmitEntity.setFlightId(flightInfo.getId());
        mFightScooterSubmitEntity.setUserId(UserInfoSingle.getInstance().getUserId());
        mFightScooterSubmitEntity.setTaskId(taskId);//外层待办传入
        mFightScooterSubmitEntity.setDeleteRedRcInfos(listDeleteNo);
        mFightScooterSubmitEntity.setDeleteRcInfos(listDeleteYes);
        mFightScooterSubmitEntity.setScooters(listHandcar);
        mFightScooterSubmitEntity.setWithoutScootereRcInfos(listWaybill);
        ((GetScooterListInfoPresenter) mPresenter).freightInfo(mFightScooterSubmitEntity);

    }

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

        if (Constants.FINISH_HANDCAR_DETAILS == resultCode) {//板车详情界面
            //删除非本航班的收运记录
            List <FtGroupScooter> listDeleteNoTemp = (List <FtGroupScooter>) data.getSerializableExtra("listDelete");
            if (listDeleteNoTemp != null) {
                listDeleteNo.addAll(listDeleteNoTemp);
            }
            //合并/处理 拉下的收运记录
            List <FtGroupScooter> listPullDown = (List <FtGroupScooter>) data.getSerializableExtra("listPullDown");
            if (listPullDown != null) {
                comparePullDownListOfWaybillList(listPullDown);
            }
            listHandcar.set(nowHandcarPosition, (FtRuntimeFlightScooter) data.getSerializableExtra("FtRuntimeFlightScooter"));
            mCargoHandlingAdapter.notifyDataSetChanged();

        } else if (Constants.FINISH_SUBPACKAGE == resultCode) {//分装页面

            FtGroupScooter mRcInfoEd = (FtGroupScooter) data.getSerializableExtra("mRcInfoEd");
            FtGroupScooter mRcInfo = (FtGroupScooter) data.getSerializableExtra("mRcInfo");
            listWaybill.set(nowWaybillPosition, mRcInfoEd);
            mCargoHandlingWaybillAdapter.notifyDataSetChanged();

            //判断是否选中了 板车
            if (nowHandcarPositionSelect != -1) {
                rcinfoToHandcar(mRcInfo, flag);
            }
        } else if (Constants.SCAN_RESULT == resultCode) {

            addHandcar(data.getStringExtra(Constants.SACN_DATA));

        }
    }

    /**
     * 设置航班信息
     *
     * @param flightInfo
     */
    private void setFlightInfo(GetScooterListInfoBean.FlightBean flightInfo) {
        tvFlightNumber.setText(flightInfo.getFlightNo());
        tvArriveTime.setText(TimeUtils.date2Tasktime3(flightInfo.getEtd()));
        tvPlaneInfo.setText("飞机货仓信息");

        listCargoCabinInfo.add("1H");
        listCargoCabinInfo.add("2H");
        listCargoCabinInfo.add("3H");
        listCargoCabinInfo.add("4H");
        mCargoCabinAdapter.notifyDataSetChanged();
    }

    /**
     * 把分装 或者 全装的收运记录 添加到板车上  ，判断是否 合并 并修改 状态
     *
     * @param flag    0 分装 1 全装
     * @param mRcInfo
     */
    private void rcinfoToHandcar(FtGroupScooter mRcInfo, int flag) {

        listHandcar.get(nowHandcarPositionSelect).setWeight(listHandcar.get(nowHandcarPositionSelect).getWeight() + mRcInfo.getWeight());
        listHandcar.get(nowHandcarPositionSelect).setVolume(listHandcar.get(nowHandcarPositionSelect).getVolume() + mRcInfo.getVolume());

        // 根据品名和运单id  确定唯一收运记录
        String strOnly1 = mRcInfo.getWaybillId();

        for (FtGroupScooter handcarRcInfo : listHandcar.get(nowHandcarPositionSelect).getGroupScooters()) {

            String strOnly2 = handcarRcInfo.getWaybillId();
            if (strOnly1.equals(strOnly2)) {
                handcarRcInfo.setWeight(handcarRcInfo.getWeight() + mRcInfo.getWeight());
                handcarRcInfo.setVolume(handcarRcInfo.getVolume() + mRcInfo.getVolume());
                handcarRcInfo.setNumber(handcarRcInfo.getNumber() + mRcInfo.getNumber());

                //全装： 如果 全装那条收运记录的状态不等于2 则把它加入删除列表
                if (flag == 1) {
                    if (2 != mRcInfo.getUpdateStatus()) {
                        listDeleteYes.add(mRcInfo);
                    }
                    listWaybill.remove(mRcInfo);
                    mCargoHandlingWaybillAdapter.notifyDataSetChanged();
                }
                // 如果合并的原有列表 不是新增状态 则改为 修改状态
                if (2 != handcarRcInfo.getUpdateStatus()) {
                    handcarRcInfo.setUpdateStatus((short) 1);
                }

                mRcInfo = null;
                break;
            }
        }
        //未合并
        //全装
        if (flag == 1) {
            //全装的收运记录板上没有 未合并  把该记录 修改状态 放上板车
            if (null != mRcInfo) {
                if (2 != mRcInfo.getUpdateStatus()) {
                    mRcInfo.setUpdateStatus((short) 1);
                }
                listHandcar.get(nowHandcarPositionSelect).getGroupScooters().add(mRcInfo);
                listWaybill.remove(mRcInfo);
                mCargoHandlingWaybillAdapter.notifyDataSetChanged();
            }

        } else {//分装
            //分装的收运记录板上没有 未合并  把该记录 设置未 新增收运记录 放上板车
            if (null != mRcInfo) {
                mRcInfo.setUpdateStatus((short) 2);
                listHandcar.get(nowHandcarPositionSelect).getGroupScooters().add(mRcInfo);
            }
        }
        mCargoHandlingAdapter.notifyDataSetChanged();
    }

    /**
     * 把从板车上拉下的收运记录列表 和 无板运单收运记录 做比较 只要运单号一样 就合并。
     * 并且把 原有收运记录（状态为新增的除外） 放入本航班删除列表。
     * 不相同 则 作为新的收运记录 加入 无板运单收运记录 并将该收运记录的状态 改为修改（如果该状态为新建则不做修改）
     */
    private void comparePullDownListOfWaybillList(List <FtGroupScooter> listPullDown) {

        Map <String, FtGroupScooter> mapTemp = new HashMap <>();
        //合并list 方便遍历
        listWaybill.addAll(listPullDown);
        for (FtGroupScooter info1 : listWaybill) {
            //根据运单id 判断
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
                //如果是有id的（板车带进来的收运记录） 合并 ，并把该条收运记录 放入本航班删除列表
                if (null != info1.getId()) {
                    listDeleteYes.add(info1);
                }
                //如果被合并的收运记录状态不为新增 则把收运记录状态 改为 修改
                if (2 != mapTemp.get(key).getUpdateStatus()) {
                    mapTemp.get(key).setUpdateStatus((short) 1);
                }
            }
        }
        listWaybill.clear();
        listWaybill.addAll(new ArrayList <FtGroupScooter>(mapTemp.values()));
        mCargoHandlingWaybillAdapter.notifyDataSetChanged();
    }

    @Override
    public void getScooterListInfoResult(GetScooterListInfoBean scooterListInfoBean) {
        if (scooterListInfoBean != null) {
            flightInfo = scooterListInfoBean.getFlight();
            listHandcar.addAll(scooterListInfoBean.getScooters());
            listWaybill.addAll(scooterListInfoBean.getWithoutScootereRcInfos());
            mCargoHandlingWaybillAdapter.notifyDataSetChanged();
            mCargoHandlingAdapter.notifyDataSetChanged();
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

            //新增板车  初始化数据
            ScooterInfoListBean mScooterInfoListBean = scooterInfoListBeans.get(0);
            if (null != mScooterInfoListBean){
                FtRuntimeFlightScooter mFtRuntimeFlightScooter = new FtRuntimeFlightScooter();
                mFtRuntimeFlightScooter.setScooterId(mScooterInfoListBean.getId());
                mFtRuntimeFlightScooter.setScooterCode(mScooterInfoListBean.getScooterCode());
                mFtRuntimeFlightScooter.setScooterWeight(mScooterInfoListBean.getScooterWeight());
//                mFtRuntimeFlightScooter.setScooterType(mScooterInfoListBean.getScooterType());
                mFtRuntimeFlightScooter.setDelFlag(mScooterInfoListBean.getDelFlag());
                mFtRuntimeFlightScooter.setCreateDate(mScooterInfoListBean.getCreateDate());
                mFtRuntimeFlightScooter.setCreateUser(mScooterInfoListBean.getCreateUser());
                mFtRuntimeFlightScooter.setUpdateDate(mScooterInfoListBean.getUpdateDate());
                mFtRuntimeFlightScooter.setUpdateUser(mScooterInfoListBean.getUpdateUser());
                mFtRuntimeFlightScooter.setGroupScooters(new ArrayList <FtGroupScooter>());
                mFtRuntimeFlightScooter.setWeight((double)0);
                mFtRuntimeFlightScooter.setVolume((double)0);
                mFtRuntimeFlightScooter.setInFlight((short)0);
                mFtRuntimeFlightScooter.setUpdateStatus((short)2);

                listHandcar.add(mFtRuntimeFlightScooter);
                mCargoHandlingAdapter.notifyDataSetChanged();
            }


        }


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
        showProgessDialog("加载中……");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }


    /**
     * 退出时 温馨提示 dialog
     */
    private void showBackDialog() {

        finish();

    }

}
