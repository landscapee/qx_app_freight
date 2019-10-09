package qx.app.freight.qxappfreight.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.ManifestWaybillListjianyiAdapter;
import qx.app.freight.qxappfreight.adapter.UnloadPlaneAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.ManifestMainBean;
import qx.app.freight.qxappfreight.bean.ManifestScooterListBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.loadinglist.CompareInfoBean;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.InstallChangeEntity;
import qx.app.freight.qxappfreight.bean.request.LoadingListRequestEntity;
import qx.app.freight.qxappfreight.bean.request.LoadingListSendEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.request.PullGoodsEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.FlightAllReportInfo;
import qx.app.freight.qxappfreight.bean.response.GetFlightCargoResBean;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;
import qx.app.freight.qxappfreight.contract.GetFlightCargoResContract;
import qx.app.freight.qxappfreight.contract.GetLastReportInfoContract;
import qx.app.freight.qxappfreight.contract.LoadAndUnloadTodoContract;
import qx.app.freight.qxappfreight.contract.StartPullContract;
import qx.app.freight.qxappfreight.dialog.WaitCallBackDialog;
import qx.app.freight.qxappfreight.presenter.GetFlightCargoResPresenter;
import qx.app.freight.qxappfreight.presenter.GetLastReportInfoPresenter;
import qx.app.freight.qxappfreight.presenter.LoadAndUnloadTodoPresenter;
import qx.app.freight.qxappfreight.presenter.StartPullPresenter;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.PushDataUtil;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CustomRecylerView;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.FlightInfoLayout;
import qx.app.freight.qxappfreight.widget.MyHorizontalScrollView;

/**
 * 装机页面 （修改前）
 */
public class LoadPlaneActivity extends BaseActivity implements GetFlightCargoResContract.getFlightCargoResView, LoadAndUnloadTodoContract.loadAndUnloadTodoView, GetLastReportInfoContract.getLastReportInfoView, StartPullContract.startPullView {
    @BindView(R.id.rv_data)
    CustomRecylerView mRvData;
    @BindView(R.id.rv_data_nonuse)
    RecyclerView mRvDataNonuse; //不使用 离港系统 列表 根据货邮舱单 显示 板车信息
    @BindView(R.id.hor_scroll)
    MyHorizontalScrollView horScroll;
    @BindView(R.id.ll_operation)
    LinearLayout llOperation;//发送至结载 最终装机单确认 布局
    @BindView(R.id.ll_operation2)
    LinearLayout llOperation2;//发送至结载 最终装机单确认 布局


    @BindView(R.id.tv_plane_info)
    TextView mTvPlaneInfo;
    @BindView(R.id.tv_flight_craft)
    TextView mTvFlightType;
    @BindView(R.id.ll_flight_info_container)
    LinearLayout mLlInfoContainer;
    @BindView(R.id.tv_seat)
    TextView mTvSeat;
    @BindView(R.id.tv_start_time)
    TextView mTvStartTime;
    @BindView(R.id.tv_pull_goods_report)
    TextView mTvPullGoodsReport;
    @BindView(R.id.tv_error_report)
    TextView mTvErrorReport;
    @BindView(R.id.tv_send_over)
    TextView mTvSendOver;
    @BindView(R.id.iv_pull_hint)
    ImageView mIvHint;
    @BindView(R.id.tv_confirm_cargo)
    TextView mTvConfirmCargo;
    @BindView(R.id.tv_end_install_equip)
    TextView mTvEndInstall;
    @BindView(R.id.tv_sure_pull)
    TextView mTvSurePull;
    private List <LoadingListBean.DataBean> mLoadingList = new ArrayList <>();
//    private UnloadPlaneAdapter adapter;

    private String mCurrentTaskId;
    private String mCurrentFlightId;
    private String mCurrentFlightNo;
    private WaitCallBackDialog mWaitCallBackDialog;
    private LoadAndUnloadTodoBean data;
    private ArrayList <LoadingListBean.DataBean.ContentObjectBean> mBaseContent;
    private ArrayList <LoadingListBean.DataBean.ContentObjectBean.ScooterBean> oriScooters;//原始板车列表数据
    private ArrayList <LoadingListBean.DataBean.ContentObjectBean.ScooterBean> newScooters = new ArrayList <>();//修改过后的板车列表数据
    private int mOperateErrorStatus = -1;
    private boolean mConfirmPlan = false;

    private boolean useLGsys = true ;//是否使用离港系统 m默认为使用
    //不使用离港系统 显示货邮舱单 使用
    private List <ManifestScooterListBean> manifestScooterListBeans = new ArrayList <>();
    private ManifestWaybillListjianyiAdapter adapterNonuse;
    private String flightInfoId = null; //货邮舱单 上的 flightInfoId



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {
        PushDataUtil.handlePushInfo(result, mCurrentTaskId, this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(InstallChangeEntity result) {
        if (result.getFlightNo() != null && result.getFlightNo().equals(mCurrentFlightNo)) {
            showCargoResUpdate();
        }
    }

    /**
     * 弹出收到新装机提示
     */
    private void showCargoResUpdate() {
//        String remark = "";
//        if (data.getRelateInfoObj() != null)
//            remark = data.getRelateInfoObj().getFlightNo()+"预装机单已更新，请查看！";
//        else
//            remark = data.getFlightNo()+"预装机单已更新，请查看！";
//        UpdatePushDialog updatePushDialog = new UpdatePushDialog(this, R.style.custom_dialog, remark, () -> {
//            LoadingListRequestEntity entity = new LoadingListRequestEntity();
//            entity.setDocumentType(2);
//            entity.setFlightId(mCurrentFlightId);
//            ((GetFlightCargoResPresenter) mPresenter).getLoadingList(entity);
//        });
//        updatePushDialog.show();

        LoadingListRequestEntity entity = new LoadingListRequestEntity();
        entity.setDocumentType(2);
        entity.setFlightId(mCurrentFlightId);
        mPresenter = new GetFlightCargoResPresenter(this);
        ((GetFlightCargoResPresenter) mPresenter).getLoadingList(entity);
        if (mWaitCallBackDialog != null) {
            mWaitCallBackDialog.dismiss();
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_load_plane;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mWaitCallBackDialog = new WaitCallBackDialog(this, R.style.dialog2);
        mWaitCallBackDialog.setCancelable(false);
        mWaitCallBackDialog.setCanceledOnTouchOutside(false);
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", v -> finish());
        data = (LoadAndUnloadTodoBean) getIntent().getSerializableExtra("plane_info");
        //是否是连班航班,连班航班的话所有关联操作都应该使用连班航班的数据
        boolean mIsKeepOnTask = data.getMovement() == 4;
        mCurrentTaskId = mIsKeepOnTask ? data.getRelateInfoObj().getTaskId() : data.getTaskId();
        toolbar.setMainTitle(Color.WHITE, (mIsKeepOnTask ? data.getRelateInfoObj().getFlightNo() : data.getFlightNo()) + "  装机");
        mTvPlaneInfo.setText(mIsKeepOnTask ? data.getRelateInfoObj().getFlightNo() : data.getFlightNo());
        mTvFlightType.setText(mIsKeepOnTask ? data.getRelateInfoObj().getAircraftno() : data.getAircraftno());
        String route = mIsKeepOnTask ? data.getRelateInfoObj().getRoute() : data.getRoute();
        FlightInfoLayout layout = new FlightInfoLayout(this, StringUtil.getFlightList(route));
        LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLlInfoContainer.removeAllViews();
        mLlInfoContainer.addView(layout, paramsMain);
        mTvSeat.setText(mIsKeepOnTask ? data.getRelateInfoObj().getSeat() : data.getSeat());
        String time;
        if (!StringUtil.isTimeNull(String.valueOf(mIsKeepOnTask ? data.getRelateInfoObj().getAtd() : data.getAtd()))) {
            time = TimeUtils.getHMDay(mIsKeepOnTask ? data.getRelateInfoObj().getAtd() : data.getAtd());
        } else if (!StringUtil.isTimeNull(String.valueOf(mIsKeepOnTask ? data.getRelateInfoObj().getEtd() : data.getEtd()))) {
            time = TimeUtils.getHMDay(mIsKeepOnTask ? data.getRelateInfoObj().getEtd() : data.getEtd());
        } else {
            time = TimeUtils.getHMDay(mIsKeepOnTask ? data.getRelateInfoObj().getStd() : data.getStd());
        }
        mTvStartTime.setText(time);



        mCurrentFlightId = mIsKeepOnTask ? data.getRelateInfoObj().getFlightId() : data.getFlightId();
        mCurrentFlightNo = mIsKeepOnTask ? data.getRelateInfoObj().getFlightNo() : data.getFlightNo();
        //发送结载
        mTvSendOver.setOnClickListener(v -> {
            mConfirmPlan = false;
            LoadingListSendEntity requestModel = new LoadingListSendEntity();
            if (data.getRelateInfoObj() != null) {
                requestModel.setFlightNo(data.getRelateInfoObj().getFlightNo());
            } else {
                requestModel.setFlightNo(data.getFlightNo());
            }
            requestModel.setCreateTime(mLoadingList.get(0).getCreateTime());
            requestModel.setLoadingUser(UserInfoSingle.getInstance().getUsername());
            requestModel.setCreateUser(mLoadingList.get(0).getCreateUser());
            requestModel.setFlightInfoId(mLoadingList.get(0).getFlightInfoId());
            requestModel.setContent(mLoadingList.get(0).getContentObject());
            mPresenter = new GetFlightCargoResPresenter(this);
            ((GetFlightCargoResPresenter) mPresenter).overLoad(requestModel);
        });
        mTvConfirmCargo.setOnClickListener(v -> {
            if (mLoadingList.size() > 0 && mLoadingList.get(0).getContentObject() != null && mLoadingList.get(0).getContentObject().size() > 0
                    && mLoadingList.get(0).getContentObject().get(0).getScooters() != null && mLoadingList.get(0).getContentObject().get(0).getScooters().size() > 0) {
                BaseFilterEntity entity1 = new BaseFilterEntity();
                entity1.setReportInfoId(mLoadingList.get(0).getContentObject().get(0).getScooters().get(0).getReportInfoId());
                String userName = UserInfoSingle.getInstance().getUsername();
                entity1.setInstalledSingleConfirmUser((userName.contains("-")) ? userName.substring(0, userName.indexOf("-")) : userName);
                mPresenter = new GetFlightCargoResPresenter(this);
                ((GetFlightCargoResPresenter) mPresenter).confirmLoadPlan(entity1);
            } else {

                ToastUtil.showToast("当前航班任务无装机单数据，不能进行装机单确认操作");
            }
        });
        mTvPullGoodsReport.setOnClickListener(v -> {
            if (mLoadingList.size() > 0 && mLoadingList.get(0).getContentObject() != null && mLoadingList.get(0).getContentObject().size() > 0
                    && mLoadingList.get(0).getContentObject().get(0).getScooters() != null && mLoadingList.get(0).getContentObject().get(0).getScooters().size() > 0) {
                if (mBaseContent != null && mBaseContent.size() > 0 && mBaseContent.get(0) != null && mBaseContent.get(0).getScooters() != null
                        && mBaseContent.get(0).getScooters().size() > 0 && mBaseContent.get(0).getScooters().get(0) != null) {
                    Intent intent = new Intent(LoadPlaneActivity.this, PullGoodsReportActivity.class);
                    intent.putExtra("plane_info", data);
                    intent.putExtra("flight_info_id", mBaseContent.get(0).getScooters().get(0).getFlightInfoId());
                    intent.putExtra("id", data.getId());
                    LoadPlaneActivity.this.startActivity(intent);
                } else
                    ToastUtil.showToast("当前航班舱位集合，无法进行拉货");

            } else {
                ToastUtil.showToast("当前航班无装机单数据，无法进行拉货");
            }
        });
        mTvErrorReport.setOnClickListener(v -> {
            Intent intent = new Intent(LoadPlaneActivity.this, ErrorReportActivity.class);
            intent.putExtra("flight_number", mIsKeepOnTask ? data.getRelateInfoObj().getFlightNo() : data.getFlightNo());//航班号
            intent.putExtra("task_id", mIsKeepOnTask ? data.getRelateInfoObj().getTaskId() : data.getTaskId());//任务id
            intent.putExtra("flight_id", mIsKeepOnTask ? data.getRelateInfoObj().getFlightId() : data.getFlightId());//Flight id
            intent.putExtra("area_id", mIsKeepOnTask ? data.getRelateInfoObj().getSeat() : data.getSeat());//area_id
            intent.putExtra("step_code", data.getOperationStepObj().get(getIntent().getIntExtra("position", -1)).getOperationCode());//step_code
            intent.putExtra("error_type", 1);
            LoadPlaneActivity.this.startActivity(intent);
        });
        mTvEndInstall.setOnClickListener(v -> {
//            if (useLGsys)
                endLoadInstall();
//            else {
//                boolean canNot = false;
//                for (ManifestScooterListBean manifestScooterListBean: manifestScooterListBeans){
//                    if (manifestScooterListBean.isPull()){
//                        canNot=true;
//                    }
//                }
//                if (!canNot)
//                    endLoadInstall();
//                else {
//                    ToastUtil.showToast("有勾选的拉货板车未");
//                }
//            }
        });
        mTvSurePull.setOnClickListener(v -> {
            if (!useLGsys)
                endLoadHyManifest();
        });
        if (useLGsys) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            //配置布局，默认为vertical（垂直布局），下边这句将布局改为水平布局
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRvData.setLayoutManager(linearLayoutManager);
//            adapter = new UnloadPlaneAdapter(mLoadingList);
//            mRvData.setAdapter(adapter);

            loadData();
            mRvData.setVisibility(View.VISIBLE);
            llOperation.setVisibility(View.VISIBLE);
            llOperation2.setVisibility(View.GONE);
            horScroll.setVisibility(View.GONE);
        } else {

            mRvDataNonuse.setLayoutManager(new LinearLayoutManager(this));
            adapterNonuse = new ManifestWaybillListjianyiAdapter(manifestScooterListBeans, data.getWidthAirFlag(),true);
            mRvDataNonuse.setAdapter(adapterNonuse);

            loadData1();
            mRvData.setVisibility(View.GONE);
            llOperation.setVisibility(View.GONE);
            llOperation2.setVisibility(View.VISIBLE);
            horScroll.setVisibility(View.VISIBLE);
        }


    }
    /**
     * 获取 货邮舱单
     */
    private void loadData1() {
        mPresenter = new GetLastReportInfoPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setFlightId(mCurrentFlightId);
        //货邮舱单
        entity.setDocumentType(1);
        entity.setSort(1);
        ((GetLastReportInfoPresenter) mPresenter).getLastReportInfo(entity);
    }

    /**
     * 获取装机单
     */
    private void loadData() {
        mPresenter = new GetFlightCargoResPresenter(this);
        LoadingListRequestEntity entity = new LoadingListRequestEntity();
        entity.setDocumentType(2);
        entity.setFlightId(mCurrentFlightId);
        ((GetFlightCargoResPresenter) mPresenter).getLoadingList(entity);
    }

    /**
     * 结束装机 提交装机单
     */
   private void endLoadInstall(){
       mPresenter = new GetFlightCargoResPresenter(this);
       if (mLoadingList.size() == 0) {
           AlertDialog.Builder builder = new AlertDialog.Builder(LoadPlaneActivity.this);
           builder.setTitle("提示");
           builder.setMessage("未收到装机单，是否结束装机?");
           builder.setPositiveButton("确定", (dialog, which) -> {
               dialog.dismiss();
               GetFlightCargoResBean bean = new GetFlightCargoResBean();
               bean.setTpFlightId(data.getFlightId());
               bean.setTaskId(data.getId());
               bean.setTpOperator(UserInfoSingle.getInstance().getUserId());
               ((GetFlightCargoResPresenter) mPresenter).flightDoneInstall(bean);
           });
           builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
           builder.show();
       } else {
           boolean doRight = true;//全部装机单的状态锁定后才能提交结束装机，暂取消判断
           LoadingListBean.DataBean entity1 = mLoadingList.get(0);
           if (entity1.getContentObject() != null && entity1.getContentObject().size() != 0) {
               for (LoadingListBean.DataBean.ContentObjectBean entity2 : entity1.getContentObject()) {
                   for (LoadingListBean.DataBean.ContentObjectBean.ScooterBean scooterBean : entity2.getScooters()) {
                       if (!scooterBean.isLocked()) {
                           doRight = false;
                           break;
                       }
                   }
               }
           }
           boolean hasPullTask = mIvHint.getVisibility() == View.VISIBLE;
           if (hasPullTask) {//还有拉货任务，优先级最高
               mOperateErrorStatus = 1;
           } else {
               if (!mConfirmPlan) {//未进行装机单确认，优先级其次
                   mOperateErrorStatus = 2;
               } else {
                   if (!doRight) {
                       mOperateErrorStatus = 3;//信息未锁定修改
                   } else {
                       mOperateErrorStatus = -1;//所有操作都正确
                   }
               }
           }
           switch (mOperateErrorStatus) {
               case 1:
                   ToastUtil.showToast("当前航班还有拉货任务，请检查！");
                   break;
               case 2:
                   ToastUtil.showToast("未进行装机单确认操作，请检查！");
                   break;
               case 3:
                   ToastUtil.showToast("装机单信息中有未锁定的数据，请检查！");
                   break;
               case -1:
                   GetFlightCargoResBean bean = new GetFlightCargoResBean();
                   bean.setTpFlightId(data.getFlightId());
                   bean.setTaskId(data.getId());
                   bean.setTpOperator(UserInfoSingle.getInstance().getUserId());
                   ((GetFlightCargoResPresenter) mPresenter).flightDoneInstall(bean);
                   break;
           }
       }
   }

    /**
     *  提交货邮舱单 （生成的拉下板车数据）
     */
    private void endLoadHyManifest() {
        List<String> listScooters = new ArrayList <>();
        for (ManifestScooterListBean manifestScooterListBean: manifestScooterListBeans){
            if (manifestScooterListBean.isPull())
                listScooters.add(manifestScooterListBean.getScooterId());
        }
        mPresenter = new StartPullPresenter(this);
        PullGoodsEntity pullGoodsEntity = new PullGoodsEntity();
        pullGoodsEntity.setCreateUserType(1);
        pullGoodsEntity.setPullGoodsType(0);
        pullGoodsEntity.setScooterIds(listScooters);
        if (flightInfoId != null)
            pullGoodsEntity.setFlightInfoId(flightInfoId);
        else {
            ToastUtil.showToast("flightInfoId 为空");
            return;
        }
        pullGoodsEntity.setUserId(UserInfoSingle.getInstance().getUserId());
        pullGoodsEntity.setUserName(UserInfoSingle.getInstance().getUsername());
        ((StartPullPresenter) mPresenter).startPull(pullGoodsEntity);

    }

    private void getPullgoodsStatus(){
        mPresenter = new GetFlightCargoResPresenter(this);
        BaseFilterEntity <Object> entity = new BaseFilterEntity <>();
        entity.setFlightInfoId(mLoadingList.get(0).getFlightInfoId());
        ((GetFlightCargoResPresenter) mPresenter).getPullStatus(entity);
    }
    @Override
    public void getLoadingListResult(LoadingListBean result) {
        Tools.closeVibrator(getApplicationContext());
        if ("318".equals(result.getStatus())) {
            mWaitCallBackDialog.show();
        } else {
            mLoadingList.clear();
            if (result.getData() == null || result.getData().size() == 0) {
                mTvSendOver.setEnabled(false);
                mTvConfirmCargo.setEnabled(true);
                mTvSendOver.setTextColor(Color.parseColor("#888888"));
                mTvConfirmCargo.setTextColor(Color.parseColor("#ff0000"));
            } else {

                mLoadingList.add(result.getData().get(0));

                getPullgoodsStatus();

                if (!TextUtils.isEmpty(result.getData().get(0).getContent())) {
                    Gson mGson = new Gson();
                    mTvSendOver.setEnabled(false);
                    mTvConfirmCargo.setEnabled(true);
                    mTvSendOver.setTextColor(Color.parseColor("#888888"));
                    mTvConfirmCargo.setTextColor(Color.parseColor("#ff0000"));
                    LoadingListBean.DataBean.ContentObjectBean[] datas = mGson.fromJson(result.getData().get(0).getContent(), LoadingListBean.DataBean.ContentObjectBean[].class);
                    //舱位集合
                    mBaseContent = new ArrayList <>(Arrays.asList(datas));
                    oriScooters = new ArrayList <>();

                }
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mLoadingList != null && mLoadingList.size() != 0) {
            mPresenter = new GetFlightCargoResPresenter(this);
            BaseFilterEntity <Object> entity = new BaseFilterEntity <>();
            entity.setFlightInfoId(mLoadingList.get(0).getFlightInfoId());
            ((GetFlightCargoResPresenter) mPresenter).getPullStatus(entity);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWaitCallBackDialog != null && mWaitCallBackDialog.isShowing())
            mWaitCallBackDialog.dismiss();
    }
    @Override
    public void flightDoneInstallResult(String result) {
        PerformTaskStepsEntity entity = new PerformTaskStepsEntity();
        entity.setType(1);
        entity.setLoadUnloadDataId(data.getId());
        entity.setFlightId(Long.valueOf(data.getFlightId()));
        entity.setFlightTaskId(data.getTaskId());
        entity.setLatitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLatitude());
        entity.setLongitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLongitude());
        entity.setOperationCode("FreightLoadFinish");
        entity.setTerminalId(DeviceInfoUtil.getDeviceInfo(this).get("deviceId"));
        entity.setUserId(UserInfoSingle.getInstance().getUserId());
        entity.setUserName(data.getWorkerName());
        entity.setCreateTime(System.currentTimeMillis());
        mPresenter = new LoadAndUnloadTodoPresenter(this);
        ((LoadAndUnloadTodoPresenter) mPresenter).slideTask(entity);
    }

    @Override
    public void overLoadResult(String result) {
        ToastUtil.showToast("发送结载成功");
        mWaitCallBackDialog.show();
    }

    @Override
    public void toastView(String error) {
        Log.e("tagTest", "error===" + error);
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
    public void loadAndUnloadTodoResult(List <LoadAndUnloadTodoBean> loadAndUnloadTodoBean) {

    }

    @Override
    public void slideTaskResult(String result) {
        ToastUtil.showToast("结束装机成功");
        EventBus.getDefault().post("InstallEquipFragment_refresh" + "@" + mCurrentTaskId);
        finish();
    }

    @Override
    public void startClearTaskResult(String result) {

    }

    @Override
    public void confirmLoadPlanResult(String result) {
        mConfirmPlan = true;
        ToastUtil.showToast("装机单版本信息确认成功");
        mTvConfirmCargo.setEnabled(false);
        mTvConfirmCargo.setTextColor(Color.parseColor("#888888"));
    }

    @Override
    public void getPullStatusResult(BaseEntity <String> result) {
        if (Integer.valueOf(result.getData()) != 0) {
            mIvHint.setVisibility(View.VISIBLE);
        } else {
            mIvHint.setVisibility(View.GONE);
        }
    }

    /**
     * 货邮舱单返回数据
     *
     * @param lastReportInfoListBeans
     */
    @Override
    public void getLastReportInfoResult(List <FlightAllReportInfo> lastReportInfoListBeans) {
        if (lastReportInfoListBeans != null && lastReportInfoListBeans.get(0) != null) {
            FlightAllReportInfo result = lastReportInfoListBeans.get(0);
            flightInfoId = result.getFlightInfoId();
            //TODO 是否是宽体机 0 宽体机 1 窄体机
            if (1 == data.getWidthAirFlag()) {
                ManifestScooterListBean title = new ManifestScooterListBean();
                title.setSuggestRepository("舱位");
                title.setScooterCode("板车号");
                title.setTotal("件数");
                title.setWeight("重量");
                title.setSpecialNumber("特货代码");
                title.setMailType("货邮代码");
                manifestScooterListBeans.add(0, title);
            } else if (0 == data.getWidthAirFlag()) {
                ManifestScooterListBean title = new ManifestScooterListBean();
                title.setUldType("型号");
                title.setUldCode("集装箱板号");
                title.setTotal("件数");
                title.setWeight("重量");
                title.setSpecialNumber("特货代码");
                title.setMailType("货邮代码");
                manifestScooterListBeans.add(0, title);
            }
            Gson mGson = new Gson();
            ManifestMainBean[] datas = mGson.fromJson(result.getContent(), ManifestMainBean[].class);
            //遍历所有的板车
            for (int i = 0; i < datas.length; i++) {
                for (int j = 0; j < datas[i].getCargos().size(); j++) {
                    for (int k = 0; k < datas[i].getCargos().get(j).getScooters().size(); k++) {
                        //为所有运单 设置航线
                        for (int l = 0; l < datas[i].getCargos().get(j).getScooters().get(k).getWaybillList().size(); l++) {
                            datas[i].getCargos().get(j).getScooters().get(k).getWaybillList().get(l).setRouteEn(datas[i].getRouteEn());
                        }
                        //为所有运单 设置
                        if (datas[i].getCargos().get(j).getScooters().get(k).getWaybillList() != null && datas[i].getCargos().get(j).getScooters().get(k).getWaybillList().size() > 0) {
                            for (ManifestScooterListBean.WaybillListBean waybillListBeans : datas[i].getCargos().get(j).getScooters().get(k).getWaybillList()) {
                                datas[i].getCargos().get(j).getScooters().get(k).setMailType(waybillListBeans.getMailType());
                                if ("C".equals(waybillListBeans.getMailType())) {
                                    break;
                                }
                            }
                        }
//                            datas[i].getCargos().get(j).getScooters().get(k).setMailType(datas[i].getCargos().get(j).getScooters().get(k).getWaybillList().get(0).getMailType());
                        if (datas[i].getCargos().get(j).getScooters().get(k).getWaybillList() != null && datas[i].getCargos().get(j).getScooters().get(k).getWaybillList().size() > 0)
                            datas[i].getCargos().get(j).getScooters().get(k).setSpecialNumber(datas[i].getCargos().get(j).getScooters().get(k).getWaybillList().get(0).getSpecialCode());
                    }
                    manifestScooterListBeans.addAll(datas[i].getCargos().get(j).getScooters());
                }
            }
          adapterNonuse.notifyDataSetChanged();

        }
    }

    @Override
    public void startPullResult(String result) {
        if (result != null)
            ToastUtil.showToast(result);

    }
}
