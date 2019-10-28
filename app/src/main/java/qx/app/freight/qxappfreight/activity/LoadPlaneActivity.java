package qx.app.freight.qxappfreight.activity;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.beidouapp.et.c.n;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.LoadPlaneInstallAdapter;
import qx.app.freight.qxappfreight.adapter.ManifestWaybillListjianyiAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.ManifestMainBean;
import qx.app.freight.qxappfreight.bean.ManifestScooterListBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.FlightIdBean;
import qx.app.freight.qxappfreight.bean.request.InstallChangeEntity;
import qx.app.freight.qxappfreight.bean.request.LoadingListRequestEntity;
import qx.app.freight.qxappfreight.bean.request.LoadingListSendEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.request.PullGoodsEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.CargoCabinData;
import qx.app.freight.qxappfreight.bean.response.FlightAllReportInfo;
import qx.app.freight.qxappfreight.bean.response.GetFlightCargoResBean;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;
import qx.app.freight.qxappfreight.contract.GetFlightCargoResContract;
import qx.app.freight.qxappfreight.contract.GetLastReportInfoContract;
import qx.app.freight.qxappfreight.contract.LoadAndUnloadTodoContract;
import qx.app.freight.qxappfreight.contract.StartPullContract;
import qx.app.freight.qxappfreight.dialog.InputCodeDialog;
import qx.app.freight.qxappfreight.dialog.TakeSpiltDialog;
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
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.FlightInfoLayout;
import qx.app.freight.qxappfreight.widget.MyHorizontalScrollView;

/**
 * 装机页面
 */
public class LoadPlaneActivity extends BaseActivity implements GetFlightCargoResContract.getFlightCargoResView, LoadAndUnloadTodoContract.loadAndUnloadTodoView, GetLastReportInfoContract.getLastReportInfoView, StartPullContract.startPullView {
    @BindView(R.id.rv_data)
    RecyclerView mRvData;
    @BindView(R.id.rv_data_nonuse)
    RecyclerView mRvDataNonuse; //不使用 离港系统 列表 根据货邮舱单 显示 板车信息
    @BindView(R.id.hor_scroll)
    MyHorizontalScrollView horScroll;
    @BindView(R.id.ll_operation)
    LinearLayout llOperation;//发送至结载 最终装机单确认 布局
    @BindView(R.id.ll_operation2)
    LinearLayout llOperation2;//发送至结载 最终装机单确认 布局

    @BindView(R.id.tv_goods)
    TextView tvGoods; //title 是否显示 货位
    @BindView(R.id.tv_operation)
    TextView tvOperation; //title 是否显示 操作

    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;//确认人
    @BindView(R.id.tv_confim_date)
    TextView mTvConfirmDate;//确认时间

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

    @BindView(R.id.tv_install_version)
    TextView tvChooseVersion;
    private List<String> versions = new ArrayList <>();

    private int sureFlag; //1 预装机单 2 最终装机单

    private List <LoadingListBean.DataBean> mLoadingList = new ArrayList <>();
    private LoadPlaneInstallAdapter adapter;

    private String mCurrentTaskId;
    private String mCurrentFlightId;
    private String mCurrentFlightNo;
    private WaitCallBackDialog mWaitCallBackDialog;
    private LoadAndUnloadTodoBean data;
    private ArrayList <LoadingListBean.DataBean.ContentObjectBean> mBaseContent;
    private ArrayList <LoadingListBean.DataBean.ContentObjectBean.ScooterBean> newScooters = new ArrayList <>();//修改过后的板车列表数据
    private int mOperateErrorStatus = -1;
    private boolean mConfirmPlan = false;

    private boolean useLGsys = true ;//是否使用离港系统 m默认为使用
    //不使用离港系统 显示货邮舱单 使用
    private List <ManifestScooterListBean> manifestScooterListBeans = new ArrayList <>();
    private ManifestWaybillListjianyiAdapter adapterNonuse;
    private String flightInfoId = null; //货邮舱单 上的 flightInfoId

    private List<String> cargos = new ArrayList <>();
    private List<String> goods = new ArrayList <>();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {
        PushDataUtil.handlePushInfo(result, mCurrentTaskId, this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(InstallChangeEntity result) {
        String flightNo = result.getFlightNo().substring(0,result.getFlightNo().indexOf(":"));
        if (flightNo != null && flightNo.equals(mCurrentFlightNo)) {
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


        //选择装机单版本
        tvChooseVersion.setOnClickListener(v->{
            showStoragePickView();
        });

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
            requestModel.setVersion(mLoadingList.get(0).getVersion());
            if(sureFlag == 1)
                requestModel.setDocumentType(3);
            else if(sureFlag == 2)
                requestModel.setDocumentType(6);
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
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRvData.setLayoutManager(linearLayoutManager);
            adapter = new LoadPlaneInstallAdapter(newScooters,data.getWidthAirFlag(),true,cargos,goods);
            mRvData.setAdapter(adapter);
            adapter.setOnDataCheckListener(new LoadPlaneInstallAdapter.OnDataCheckListener() {
                @Override
                public void onDataChecked(String scooterId) {

                    compareInstall();

                }

                @Override
                public void onTakeSplit(int position) {// 拆舱 /拆箱
                    if (newScooters.get(position).isLocked()){
                        ToastUtil.showToast("板车已锁定");
                        return;
                    }
                    if (newScooters.get(position).getExceptionFlag() == 1){
                        ToastUtil.showToast("拉下的板车无法拆分");
                        return;
                    }

                    if (!newScooters.get(position).isSplit()){
                        showSpiltDialog(position);
                    }
                    else {
                        removeInstall(position);
                        compareInstall();
                        adapter.notifyDataSetChanged();
                    }
                }
            });

//            loadData();
            getPlaneSpace();
            //TODO 是否是宽体机 0 宽体机 1 窄体机
            if (1 == data.getWidthAirFlag()) {
                 tvGoods.setVisibility(View.GONE);
            } else if (0 == data.getWidthAirFlag()) {
                tvGoods.setVisibility(View.VISIBLE);
            }

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
     * 比较装机单是否有改变
     */
    private void compareInstall() {
        boolean modified = false;
        for (LoadingListBean.DataBean.ContentObjectBean.ScooterBean scooterBean:newScooters){
            if (scooterBean.isChange()|| scooterBean.getExceptionFlag() == 1||scooterBean.isSplit()){
                modified = true;
                break;
            }
        }
        if (modified) {
            mTvSendOver.setEnabled(true);
            mTvConfirmCargo.setEnabled(false);
            mTvSendOver.setTextColor(Color.parseColor("#009EB5"));
            mTvConfirmCargo.setTextColor(Color.parseColor("#888888"));
        } else {
            mTvSendOver.setEnabled(false);
            mTvConfirmCargo.setEnabled(true);
            mTvSendOver.setTextColor(Color.parseColor("#888888"));
            mTvConfirmCargo.setTextColor(Color.parseColor("#ff0000"));
        }
    }

    /**
     * 拆舱弹出框
     * @param position
     */
    private void showSpiltDialog(int position) {
        TakeSpiltDialog dialog1 = new TakeSpiltDialog(this,cargos,goods,newScooters.get(position).getOldCargoName(),newScooters.get(position).getOldLocation(),newScooters.get(position).getWeight(),data.getWidthAirFlag());
        dialog1.setTitle("请选择调整舱位/货位")
                .setPositiveButton("确定")
                .setNegativeButton("取消")
                .isCanceledOnTouchOutside(false)
                .isCanceled(true)
                .setOnClickListener(new TakeSpiltDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm, String strBerth, String strGoos, Double weight) {
                        if (confirm) {
                            try {
                                LoadingListBean.DataBean.ContentObjectBean.ScooterBean splitScooter = Tools.IOclone(newScooters.get(position));
                                newScooters.get(position).setWeight(newScooters.get(position).getWeight() - weight);
                                splitScooter.setWeight(weight);
                                splitScooter.setCargoName(strBerth);
                                if (data.getWidthAirFlag() == 0)
                                    splitScooter.setLocation(strGoos);
                                splitScooter.setSplit(true);
                                newScooters.add(position+1,splitScooter);
                                joinInstall(newScooters.get(position).getId(),splitScooter);
                                adapter.notifyDataSetChanged();
                                compareInstall();
                            }
                            catch (Exception e){
                                Log.e("Tools.IOclone",e.getMessage());
                            }
                        } else {
                            if (dialog!=null && dialog.isShowing())
                                dialog.dismiss();
                        }
                    }
                })
                .show();

    }

    /**
     * 把拆分出来的装机单 加入到装机单
     * @param id
     * @param splitScooter
     */
    private void joinInstall(String id, LoadingListBean.DataBean.ContentObjectBean.ScooterBean splitScooter) {
        for (LoadingListBean.DataBean.ContentObjectBean contentObjectBean:mBaseContent){
            for ( int i = 0; i < contentObjectBean.getScooters().size() ;i++){
                if (id.equals(contentObjectBean.getScooters().get(i).getId())){
//                    contentObjectBean.getScooters().get(i).setWeight(contentObjectBean.getScooters().get(i).getWeight() - splitScooter.getWeight());
                    splitScooter.setOldId(id);//记录被拆分板车的 id
                    splitScooter.setId(String.valueOf(System.currentTimeMillis())); //标记拆分出来的新板车 方便删除
                    contentObjectBean.getScooters().add(i+1,splitScooter);
                    break;
                }
            }
        }

    }

    /**
     * 删除拆分出来的
     * @param position
     */
    private void removeInstall(int position) {

        for (LoadingListBean.DataBean.ContentObjectBean contentObjectBean:mBaseContent){
            for ( int i = 0; i < contentObjectBean.getScooters().size() ;i++){
//                //通过删除拆分板车记录的被拆分板车id 找到被拆分板车 把重量加回去。
//                if (newScooters.get(position).getOldId().equals(contentObjectBean.getScooters().get(i).getId())){
//                    contentObjectBean.getScooters().get(i).setWeight(contentObjectBean.getScooters().get(i).getWeight()+newScooters.get(position).getWeight());
//                }
                //比对被拆分板车 生成的id（时间戳） 删除被拆分板车
                if (newScooters.get(position).getId().equals(contentObjectBean.getScooters().get(i).getId())){
                    contentObjectBean.getScooters().remove(i);
                    break;
                }
            }
        }
        //通过删除拆分板车记录的被拆分板车id 找到被拆分板车 把重量加回去。
        for (LoadingListBean.DataBean.ContentObjectBean.ScooterBean scooterBean:newScooters){
            if (scooterBean.getId().equals(newScooters.get(position).getOldId())){
                scooterBean.setWeight(scooterBean.getWeight()+newScooters.get(position).getWeight());
                newScooters.remove(position);
                break;
            }
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
     * 获取飞机舱位信息
     */
    private void getPlaneSpace() {
        mPresenter = new GetFlightCargoResPresenter(this);
        FlightIdBean entity = new FlightIdBean();
        entity.setFlightId(Long.valueOf(mCurrentFlightId));
        ((GetFlightCargoResPresenter) mPresenter).getFlightSpace(entity);
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
                   ToastUtil.showToast("未进行最终装机单确认操作，请检查！");
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

    private void showStoragePickView() {
        OptionsPickerView pickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (versions.size() > 0) {
                    //切换装机单版本
                    switchoverInstall(options1);
                    tvChooseVersion.setText(versions.get(options1));
                }
            }
        }).build();
        pickerView.setPicker(versions);
        pickerView.setTitleText("版本号");
        if (!versions.isEmpty())
            pickerView.show();
        else {
            ToastUtil.showToast("还没有装机单！");
        }
    }

    /**
     * 切换装机单版本
     * @param options1
     */
    private void switchoverInstall(int options1) {

        if (!TextUtils.isEmpty(mLoadingList.get(options1).getContent())) {
            if (options1 == 0){ // 只有最新版本才能 发送至结载 和 确认装机单
                mTvConfirmCargo.setVisibility(View.VISIBLE);
                mTvSendOver.setVisibility(View.VISIBLE);
                if (mLoadingList.get(options1).getInstalledSingleConfirm() == 1){ //已确认
                    mTvConfirmCargo.setVisibility(View.GONE);
                    mTvSendOver.setVisibility(View.GONE);
                }
                if (versions.get(options1).contains("最终装机单")){
                    sureFlag = 2;
                    mTvConfirmCargo.setText("最终装机单确认");
                }
                else {
                    sureFlag = 1;
                    mTvConfirmCargo.setText("确认按此装机");
                }
            }
            else {
                mTvConfirmCargo.setVisibility(View.GONE);
                mTvSendOver.setVisibility(View.GONE);
            }
            mTvConfirmDate.setVisibility(View.VISIBLE);
            mTvConfirm.setVisibility(View.VISIBLE);
            mTvConfirm.setText("结载员:"+mLoadingList.get(options1).getCreateUserName());
            mTvConfirmDate.setText("发送时间:"+ TimeUtils.date2Tasktime6(mLoadingList.get(options1).getCreateTime()));

            Gson mGson = new Gson();
            mTvSendOver.setEnabled(false);
            mTvConfirmCargo.setEnabled(true);
            mTvSendOver.setTextColor(Color.parseColor("#888888"));
            mTvConfirmCargo.setTextColor(Color.parseColor("#ff0000"));
            LoadingListBean.DataBean.ContentObjectBean[] datas = mGson.fromJson(mLoadingList.get(options1).getContent(), LoadingListBean.DataBean.ContentObjectBean[].class);
            //舱位集合
            mBaseContent = new ArrayList <>(Arrays.asList(datas));
            mLoadingList.get(options1).setContentObject(mBaseContent);
            //保存原有舱位，并 把装机单上的 板车数据 放到一个列表上
            newScooters.clear();
            for (LoadingListBean.DataBean.ContentObjectBean contentObjectBean:mBaseContent){
                for (LoadingListBean.DataBean.ContentObjectBean.ScooterBean scooterBean:contentObjectBean.getScooters()){
                    scooterBean.setOldCargoName(scooterBean.getCargoName());
                    scooterBean.setOldLocation(scooterBean.getLocation());
                }
                newScooters.addAll(contentObjectBean.getScooters());
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getLoadingListResult(LoadingListBean result) {
//        Tools.closeVibrator(getApplicationContext());
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
                mLoadingList.addAll(result.getData());
                versions.clear();
                for (LoadingListBean.DataBean dataBean:mLoadingList){
                    if (dataBean.getDocumentType() == 2){
                        versions.add("预装机单"+dataBean.getVersion());
                    } else{
                        versions.add("最终装机单"+dataBean.getVersion());
                    }
                }
                //已经确认最终装机单
                if (mLoadingList.get(0).getDocumentType()!= 2&&mLoadingList.get(0).getInstalledSingleConfirm() == 1)
                    mConfirmPlan = true;

                getPullgoodsStatus();
                tvChooseVersion.setText(versions.get(0));
                switchoverInstall(0);
            }
        }
    }

    /**
     * 舱位信息返回
     * @param result
     */
    @Override
    public void setFlightSpace(CargoCabinData result) {
        cargos.clear();
        goods.clear();
        if (result.getHld1wgt() > 0) {
            cargos.add("1");
        }
        if (result.getHld2wgt() > 0) {
            cargos.add("2");
        }
        if (result.getHld3wgt() > 0) {
            cargos.add("3");
        }
        if (result.getHld4wgt() > 0) {
            cargos.add("4");
        }
        if (result.getHld5wgt() > 0) {
            cargos.add("5");
        }
        for (CargoCabinData.CargosBean cargosBean:result.getCargos()){
            goods.add(cargosBean.getPos());
        }
        adapter.notifySp(cargos,goods);
        loadData();
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
        ToastUtil.showToast("装机单版本信息确认成功");
        loadData();
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
