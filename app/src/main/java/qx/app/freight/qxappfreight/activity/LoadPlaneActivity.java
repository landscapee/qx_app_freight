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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
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
import qx.app.freight.qxappfreight.bean.request.LockScooterEntity;
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
import qx.app.freight.qxappfreight.dialog.TakeSpiltDialog;
import qx.app.freight.qxappfreight.dialog.WaitCallBackDialog;
import qx.app.freight.qxappfreight.presenter.GetFlightCargoResPresenter;
import qx.app.freight.qxappfreight.presenter.GetLastReportInfoPresenter;
import qx.app.freight.qxappfreight.presenter.StartPullPresenter;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.utils.PushDataUtil;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.FlightInfoLayout;
import qx.app.freight.qxappfreight.widget.MyHorizontalScrollView;

/**
 * ????????????
 */
public class LoadPlaneActivity extends BaseActivity implements GetFlightCargoResContract.getFlightCargoResView, LoadAndUnloadTodoContract.loadAndUnloadTodoView, GetLastReportInfoContract.getLastReportInfoView, StartPullContract.startPullView {
    @BindView(R.id.rv_data)
    RecyclerView mRvData;
    @BindView(R.id.rv_data_nonuse)
    RecyclerView mRvDataNonuse; //????????? ???????????? ?????? ?????????????????? ?????? ????????????
    @BindView(R.id.hor_scroll)
    MyHorizontalScrollView horScroll;
    @BindView(R.id.ll_operation)
    LinearLayout llOperation;//??????????????? ????????????????????? ??????
    @BindView(R.id.ll_operation2)
    LinearLayout llOperation2;//??????????????? ????????????????????? ??????

    @BindView(R.id.tv_goods)
    TextView tvGoods; //title ???????????? ??????
    @BindView(R.id.tv_operation)
    TextView tvOperation; //title ???????????? ??????

    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;//?????????
    @BindView(R.id.tv_confim_date)
    TextView mTvConfirmDate;//????????????

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
    @BindView(R.id.fr_pull_goods_report)
    FrameLayout frameLayout;//??????????????????


    @BindView(R.id.tv_install_version)
    TextView tvChooseVersion;
    private List <String> versions = new ArrayList <>();

    private int sureFlag; //1 ???????????? 2 ???????????????

    private List <LoadingListBean.DataBean> mLoadingList = new ArrayList <>();
    private LoadPlaneInstallAdapter adapter;

    private String mCurrentTaskId;
    private String mCurrentFlightId;
    private String mCurrentFlightNo;
    private WaitCallBackDialog mWaitCallBackDialog;
    private LoadAndUnloadTodoBean data;
    private ArrayList <LoadingListBean.DataBean.ContentObjectBean> mBaseContent;
    private ArrayList <LoadingListBean.DataBean.ContentObjectBean.ScooterBean> newScooters = new ArrayList <>();//?????????????????????????????????
    private int mOperateErrorStatus = -1;
    private boolean mConfirmPlan = false;

    private boolean useLGsys = true;//???????????????????????? m???????????????
    //????????????????????? ?????????????????? ??????
    private List <ManifestScooterListBean> manifestScooterListBeans = new ArrayList <>();
    private ManifestWaybillListjianyiAdapter adapterNonuse;
    private String flightInfoId = null; //???????????? ?????? flightInfoId

    private List <String> cargos = new ArrayList <>();
    private List <String> goods = new ArrayList <>();

    private String loadInstallId;//???????????????id

    private int currentLockPosition;//??????????????? position

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {
        PushDataUtil.handlePushInfo(result, mCurrentTaskId, this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(InstallChangeEntity result) {
        String flightNo = result.getFlightNo().substring(0, result.getFlightNo().indexOf(":"));
        if (flightNo != null && flightNo.equals(mCurrentFlightNo)) {
            showCargoResUpdate();
        }
    }

    /**
     * ???????????????????????????
     */
    private void showCargoResUpdate() {
//        String remark = "";
//        if (data.getRelateInfoObj() != null)
//            remark = data.getRelateInfoObj().getFlightNo()+"????????????????????????????????????";
//        else
//            remark = data.getFlightNo()+"????????????????????????????????????";
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
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "??????", v -> finish());
        data = (LoadAndUnloadTodoBean) getIntent().getSerializableExtra("plane_info");

        if (data.getEndLoadTimeIn() > 0) {
            mTvEndInstall.setVisibility(View.GONE);
            frameLayout.setVisibility(View.GONE);
        } else {
            frameLayout.setVisibility(View.VISIBLE);
            mTvEndInstall.setVisibility(View.VISIBLE);
        }


        //?????????????????????,????????????????????????????????????????????????????????????????????????
        boolean mIsKeepOnTask = data.getMovement() == 4;
        mCurrentTaskId = mIsKeepOnTask ? data.getRelateInfoObj().getTaskId() : data.getTaskId();
        toolbar.setMainTitle(Color.WHITE, (mIsKeepOnTask ? data.getRelateInfoObj().getFlightNo() : data.getFlightNo()) + "  ??????");
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


        //?????????????????????
        tvChooseVersion.setOnClickListener(v -> {
            showStoragePickView();
        });

        //????????????
        mTvSendOver.setOnClickListener(v -> {
            if (mLoadingList.size() == 0) {
                ToastUtil.showToast("????????????????????????????????????????????????????????????????????????");
                return;
            }
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
            if (sureFlag == 1)
                requestModel.setDocumentType(3);
            else if (sureFlag == 2)
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

                ToastUtil.showToast("????????????????????????????????????????????????????????????????????????");
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
                    ToastUtil.showToast("?????????????????????????????????????????????");

            } else {
                ToastUtil.showToast("???????????????????????????????????????????????????");
            }
        });
        mTvErrorReport.setOnClickListener(v -> {
            Intent intent = new Intent(LoadPlaneActivity.this, ErrorReportActivity.class);
            intent.putExtra("flight_number", mIsKeepOnTask ? data.getRelateInfoObj().getFlightNo() : data.getFlightNo());//?????????
            intent.putExtra("task_id", mIsKeepOnTask ? data.getRelateInfoObj().getTaskId() : data.getTaskId());//??????id
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
//                    ToastUtil.showToast("???????????????????????????");
//                }
//            }
        });
        mTvSurePull.setOnClickListener(v -> {
            if (!useLGsys)
                endLoadHyManifest();
        });

        if (useLGsys) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            //????????????????????????vertical????????????????????????????????????????????????????????????
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRvData.setLayoutManager(linearLayoutManager);
            adapter = new LoadPlaneInstallAdapter(newScooters, data.getWidthAirFlag(), true, cargos, goods);
            mRvData.setAdapter(adapter);
            adapter.setOnDataCheckListener(new LoadPlaneInstallAdapter.OnDataCheckListener() {
                @Override
                public void onDataChecked(String scooterId) {

                    compareInstall();

                }

                @Override
                public void onTakeSplit(int position) {// ?????? /??????
                    if (newScooters.get(position).getLock() == 1) {
                        ToastUtil.showToast("???????????????");
                        return;
                    }
                    if (newScooters.get(position).getExceptionFlag() == 1) {
                        ToastUtil.showToast("???????????????????????????");
                        return;
                    }

                    if (!newScooters.get(position).isSplit()) {
                        showSpiltDialog(position);
                    } else {
                        removeInstall(position);
                        compareInstall();
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onLockClicked(int position) {
                    lockOrUnlockScooter(position);
                }
            });

//            loadData();
            getPlaneSpace();
            //TODO ?????????????????? 0 ????????? 1 ?????????
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
            adapterNonuse = new ManifestWaybillListjianyiAdapter(manifestScooterListBeans, data.getWidthAirFlag(), true);
            mRvDataNonuse.setAdapter(adapterNonuse);
            loadData1();
            mRvData.setVisibility(View.GONE);
            llOperation.setVisibility(View.GONE);
            llOperation2.setVisibility(View.VISIBLE);
            horScroll.setVisibility(View.VISIBLE);
        }


    }


    /**
     * ??????????????????????????????
     */
    private void compareInstall() {
        boolean modified = false;
        for (LoadingListBean.DataBean.ContentObjectBean.ScooterBean scooterBean : newScooters) {
            if (scooterBean.isChange() || scooterBean.getExceptionFlag() == 1 || scooterBean.isSplit()) {
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
     * ???????????????
     *
     * @param position
     */
    private void showSpiltDialog(int position) {
        TakeSpiltDialog dialog1 = new TakeSpiltDialog(this, cargos, goods, newScooters.get(position).getOldCargoName(), newScooters.get(position).getOldLocation(), newScooters.get(position).getWeight(), data.getWidthAirFlag());
        dialog1.setTitle("?????????????????????/??????")
                .setPositiveButton("??????")
                .setNegativeButton("??????")
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
                                joinInstall(newScooters.get(position).getId(), splitScooter);
                                newScooters.add(position + 1, splitScooter);
                                adapter.notifyDataSetChanged();
                                compareInstall();
                            } catch (Exception e) {
                                Log.e("Tools.IOclone", e.getMessage());
                            }
                        } else {
                            if (dialog != null && dialog.isShowing())
                                dialog.dismiss();
                        }
                    }
                })
                .show();

    }

    /**
     * ??????????????????????????? ??????????????????
     *
     * @param id
     * @param splitScooter
     */
    private void joinInstall(String id, LoadingListBean.DataBean.ContentObjectBean.ScooterBean splitScooter) {
        for (LoadingListBean.DataBean.ContentObjectBean contentObjectBean : mBaseContent) {
            for (int i = 0; i < contentObjectBean.getScooters().size(); i++) {
                if (id.equals(contentObjectBean.getScooters().get(i).getId())) {
//                    contentObjectBean.getScooters().get(i).setWeight(contentObjectBean.getScooters().get(i).getWeight() - splitScooter.getWeight());
                    splitScooter.setOldId(id);//???????????????????????? id
                    splitScooter.setId(String.valueOf(System.currentTimeMillis())); //?????????????????????????????? ????????????
                    contentObjectBean.getScooters().add(i + 1, splitScooter);
                    break;
                }
            }
        }

    }

    /**
     * ?????????????????????
     *
     * @param position
     */
    private void removeInstall(int position) {

        for (LoadingListBean.DataBean.ContentObjectBean contentObjectBean : mBaseContent) {
            for (int i = 0; i < contentObjectBean.getScooters().size(); i++) {
//                //????????????????????????????????????????????????id ????????????????????? ?????????????????????
//                if (newScooters.get(position).getOldId().equals(contentObjectBean.getScooters().get(i).getId())){
//                    contentObjectBean.getScooters().get(i).setWeight(contentObjectBean.getScooters().get(i).getWeight()+newScooters.get(position).getWeight());
//                }
                //????????????????????? ?????????id??????????????? ?????????????????????
                if (newScooters.get(position).getId().equals(contentObjectBean.getScooters().get(i).getId())) {
                    contentObjectBean.getScooters().remove(i);
                    break;
                }
            }
        }
        //????????????????????????????????????????????????id ????????????????????? ?????????????????????
        for (LoadingListBean.DataBean.ContentObjectBean.ScooterBean scooterBean : newScooters) {
            if (scooterBean.getId().equals(newScooters.get(position).getOldId())) {
                scooterBean.setWeight(scooterBean.getWeight() + newScooters.get(position).getWeight());
                newScooters.remove(position);
                break;
            }
        }

    }

    /**
     * ?????? ????????????
     */
    private void loadData1() {
        mPresenter = new GetLastReportInfoPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setFlightId(mCurrentFlightId);
        //????????????
        entity.setDocumentType(1);
        entity.setSort(1);
        ((GetLastReportInfoPresenter) mPresenter).getLastReportInfo(entity);
    }

    /**
     * ???????????????
     */
    private void loadData() {
        mPresenter = new GetFlightCargoResPresenter(this);
        LoadingListRequestEntity entity = new LoadingListRequestEntity();
        entity.setDocumentType(2);
        entity.setFlightId(mCurrentFlightId);
        ((GetFlightCargoResPresenter) mPresenter).getLoadingList(entity);
    }

    /**
     * ????????????????????????
     */
    private void getPlaneSpace() {
        mPresenter = new GetFlightCargoResPresenter(this);
        FlightIdBean entity = new FlightIdBean();
        entity.setFlightId(Long.valueOf(mCurrentFlightId));
        ((GetFlightCargoResPresenter) mPresenter).getFlightSpace(entity);
    }

    /**
     * ?????????????????????
     */
    private void lockOrUnlockScooter(int position) {
        mPresenter = new GetLastReportInfoPresenter(this);
        LockScooterEntity entity = new LockScooterEntity();
        if (newScooters.get(position).getLock() == 0 || newScooters.get(position).getLock() == 3)
            entity.setOperationType(1);
        else
            entity.setOperationType(2);

        entity.setScooterId(newScooters.get(position).getId());
        entity.setReportInfoId(loadInstallId);
        entity.setOperationUser(UserInfoSingle.getInstance().getUsername());
        ((GetLastReportInfoPresenter) mPresenter).lockOrUnlockScooter(entity);
        currentLockPosition = position;
    }

    /**
     * ???????????? ???????????????
     */
    private void endLoadInstall() {
        mPresenter = new GetFlightCargoResPresenter(this);
        if (mLoadingList.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoadPlaneActivity.this);
            builder.setTitle("??????");
            builder.setMessage("????????????????????????????????????????");
            builder.setPositiveButton("??????", (dialog, which) -> {
                dialog.dismiss();
                GetFlightCargoResBean bean = new GetFlightCargoResBean();
                bean.setTpFlightId(data.getFlightId());
                bean.setTaskId(data.getId());
                bean.setTpOperator(UserInfoSingle.getInstance().getUserId());
                ((GetFlightCargoResPresenter) mPresenter).flightDoneInstall(bean);
            });
            builder.setNegativeButton("??????", (dialog, which) -> dialog.dismiss());
            builder.show();
        } else {
            boolean doRight = true;//???????????????????????????????????????????????????????????????????????????
            LoadingListBean.DataBean entity1 = mLoadingList.get(0);
            if (entity1.getContentObject() != null && entity1.getContentObject().size() != 0) {
                for (LoadingListBean.DataBean.ContentObjectBean entity2 : entity1.getContentObject()) {
                    for (LoadingListBean.DataBean.ContentObjectBean.ScooterBean scooterBean : entity2.getScooters()) {
                        if (!(scooterBean.getLock() == 1)) {
                            doRight = false;
                            break;
                        }
                    }
                }
            }
            boolean hasPullTask = mIvHint.getVisibility() == View.VISIBLE;
            if (hasPullTask) {//????????????????????????????????????
                mOperateErrorStatus = 1;
            } else {
                if (!mConfirmPlan) {//??????????????????????????????????????????
                    mOperateErrorStatus = 2;
                } else {
                    if (!doRight) {
                        mOperateErrorStatus = 3;//?????????????????????
                    } else {
                        mOperateErrorStatus = -1;//?????????????????????
                    }
                }
            }
            switch (mOperateErrorStatus) {
                case 1:
                    ToastUtil.showToast("?????????????????????????????????????????????");
                    break;
                case 2:
                    ToastUtil.showToast("???????????????????????????????????????????????????");
                    break;
                case 3:
                    ToastUtil.showToast("??????????????????????????????????????????????????????");
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
     * ?????????????????? ?????????????????????????????????
     */
    private void endLoadHyManifest() {
        List <String> listScooters = new ArrayList <>();
        for (ManifestScooterListBean manifestScooterListBean : manifestScooterListBeans) {
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
            ToastUtil.showToast("flightInfoId ??????");
            return;
        }
        pullGoodsEntity.setUserId(UserInfoSingle.getInstance().getUserId());
        pullGoodsEntity.setUserName(UserInfoSingle.getInstance().getUsername());
        ((StartPullPresenter) mPresenter).startPull(pullGoodsEntity);

    }

    private void getPullgoodsStatus() {
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
                    //?????????????????????
                    switchoverInstall(options1);
                    tvChooseVersion.setText(versions.get(options1));
                }
            }
        }).build();
        pickerView.setPicker(versions);
        pickerView.setTitleText("?????????");
        if (!versions.isEmpty())
            pickerView.show();
        else {
            ToastUtil.showToast("?????????????????????");
        }
    }

    /**
     * ?????????????????????
     *
     * @param options1
     */
    private void switchoverInstall(int options1) {

        if (!TextUtils.isEmpty(mLoadingList.get(options1).getContent())) {
            if (options1 == 0) { // ???????????????????????? ??????????????? ??? ???????????????
                mTvConfirmCargo.setVisibility(View.VISIBLE);
                mTvSendOver.setVisibility(View.VISIBLE);
                if (mLoadingList.get(options1).getInstalledSingleConfirm() == 1) { //?????????
                    mTvConfirmCargo.setVisibility(View.GONE);
                    mTvSendOver.setVisibility(View.GONE);
                }
                if (versions.get(options1).contains("???????????????")) {
                    sureFlag = 2;
                    mTvConfirmCargo.setText("?????????????????????");
                } else {
                    sureFlag = 1;
                    mTvConfirmCargo.setText("??????????????????");
                }
                adapter.setShowLock(true);// ??????????????? ????????? ????????????
            } else {
                adapter.setShowLock(false);
                mTvConfirmCargo.setVisibility(View.GONE);
                mTvSendOver.setVisibility(View.GONE);
            }
            mTvConfirmDate.setVisibility(View.VISIBLE);
            mTvConfirm.setVisibility(View.VISIBLE);
            mTvConfirm.setText("?????????:" + mLoadingList.get(options1).getCreateUserName());
            mTvConfirmDate.setText("????????????:" + TimeUtils.date2Tasktime6(mLoadingList.get(options1).getCreateTime()));

            Gson mGson = new Gson();
            mTvSendOver.setEnabled(false);
            mTvConfirmCargo.setEnabled(true);
            mTvSendOver.setTextColor(Color.parseColor("#888888"));
            mTvConfirmCargo.setTextColor(Color.parseColor("#ff0000"));
            LoadingListBean.DataBean.ContentObjectBean[] datas = mGson.fromJson(mLoadingList.get(options1).getContent(), LoadingListBean.DataBean.ContentObjectBean[].class);
            //????????????
            mBaseContent = new ArrayList <>(Arrays.asList(datas));
            mLoadingList.get(options1).setContentObject(mBaseContent);
            loadInstallId = mLoadingList.get(options1).getId();
            //???????????????????????? ?????????????????? ???????????? ?????????????????????
            newScooters.clear();
            for (LoadingListBean.DataBean.ContentObjectBean contentObjectBean : mBaseContent) {
                for (LoadingListBean.DataBean.ContentObjectBean.ScooterBean scooterBean : contentObjectBean.getScooters()) {
                    //????????????????????????????????? H
                    if (!StringUtil.isEmpty(scooterBean.getCargoName()) && scooterBean.getCargoName().length() == 1) {
                        scooterBean.setCargoName(scooterBean.getCargoName() + "H");
                    } else {
                        scooterBean.setCargoName(scooterBean.getCargoName());
                    }

                    scooterBean.setOldCargoName(scooterBean.getCargoName());
                    scooterBean.setOldLocation(scooterBean.getLocation());
                }
                newScooters.addAll(contentObjectBean.getScooters());
            }
//            //??????????????????
//            if (Build.VERSION.SDK_INT > 24) {
//                newScooters.sort((x, y) -> Integer.compare(Integer.valueOf(!StringUtil.isEmpty(x.getCargoName()) ? x.getCargoName().substring(0, 1) : "0"), Integer.valueOf(!StringUtil.isEmpty(y.getCargoName()) ? y.getCargoName().substring(0, 1) : "0")));
//            } else {
//                Collections.sort(newScooters, new Comparator <LoadingListBean.DataBean.ContentObjectBean.ScooterBean>() {
//                    @Override
//                    public int compare(LoadingListBean.DataBean.ContentObjectBean.ScooterBean o1, LoadingListBean.DataBean.ContentObjectBean.ScooterBean o2) {
//                        int x = Integer.valueOf(!StringUtil.isEmpty(o1.getCargoName()) ? o1.getCargoName().substring(0, 1) : "0");
//                        int y = Integer.valueOf(!StringUtil.isEmpty(o2.getCargoName()) ? o2.getCargoName().substring(0, 1) : "0");
//                        return y - x;
//                    }
//                });
//            }

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
                for (LoadingListBean.DataBean dataBean : mLoadingList) {
                    if (dataBean.getDocumentType() == 2) {
                        versions.add("????????????" + dataBean.getVersion());
                    } else {
                        versions.add("???????????????" + dataBean.getVersion());
                    }
                }
                //???????????????????????????
                if (mLoadingList.get(0).getDocumentType() != 2 && mLoadingList.get(0).getInstalledSingleConfirm() == 1)
                    mConfirmPlan = true;

                getPullgoodsStatus();
                tvChooseVersion.setText(versions.get(0));
                switchoverInstall(0);
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param result
     */
    @Override
    public void setFlightSpace(CargoCabinData result) {
        cargos.clear();
        goods.clear();
        if (result.getHld1wgt() > 0) {
            cargos.add("1H");
        }
        if (result.getHld2wgt() > 0) {
            cargos.add("2H");
        }
        if (result.getHld3wgt() > 0) {
            cargos.add("3H");
        }
        if (result.getHld4wgt() > 0) {
            cargos.add("4H");
        }
        if (result.getHld5wgt() > 0) {
            cargos.add("5H");
        }
        for (CargoCabinData.CargosBean cargosBean : result.getCargos()) {
            goods.add(cargosBean.getPos());
        }
        adapter.notifySp(cargos, goods);
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
        ToastUtil.showToast("??????????????????");
        EventBus.getDefault().post("InstallEquipFragment_refresh");
        finish();
//        PerformTaskStepsEntity entity = new PerformTaskStepsEntity();
//        entity.setType(1);
//        entity.setLoadUnloadDataId(data.getId());
//        entity.setFlightId(Long.valueOf(data.getFlightId()));
//        entity.setFlightTaskId(data.getTaskId());
//        entity.setLatitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLatitude());
//        entity.setLongitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLongitude());
//        entity.setOperationCode("FreightLoadFinish");
//        entity.setTerminalId(DeviceInfoUtil.getDeviceInfo(this).get("deviceId"));
//        entity.setUserId(UserInfoSingle.getInstance().getUserId());
//        entity.setUserName(data.getWorkerName());
//        entity.setCreateTime(System.currentTimeMillis());
//        mPresenter = new LoadAndUnloadTodoPresenter(this);
//        ((LoadAndUnloadTodoPresenter) mPresenter).slideTask(entity);
    }

    @Override
    public void overLoadResult(String result) {
        ToastUtil.showToast("??????????????????");
        mWaitCallBackDialog.show();
    }

    @Override
    public void toastView(String error) {
        Log.e("tagTest", "error===" + error);
        ToastUtil.showToast(error);
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("?????????......");
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
        ToastUtil.showToast("??????????????????");
        EventBus.getDefault().post("InstallEquipFragment_refresh" + "@" + mCurrentTaskId);
        finish();
    }

    @Override
    public void startClearTaskResult(String result) {

    }

    @Override
    public void confirmLoadPlanResult(String result) {
        ToastUtil.showToast("?????????????????????????????????");
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
     * ????????????????????????
     *
     * @param lastReportInfoListBeans
     */
    @Override
    public void getLastReportInfoResult(List <FlightAllReportInfo> lastReportInfoListBeans) {
        if (lastReportInfoListBeans != null && lastReportInfoListBeans.get(0) != null) {
            FlightAllReportInfo result = lastReportInfoListBeans.get(0);
            flightInfoId = result.getFlightInfoId();
            //TODO ?????????????????? 0 ????????? 1 ?????????
            if (1 == data.getWidthAirFlag()) {
                ManifestScooterListBean title = new ManifestScooterListBean();
                title.setSuggestRepository("??????");
                title.setScooterCode("?????????");
                title.setTotal("??????");
                title.setWeight("??????");
                title.setSpecialNumber("????????????");
                title.setMailType("????????????");
                manifestScooterListBeans.add(0, title);
            } else if (0 == data.getWidthAirFlag()) {
                ManifestScooterListBean title = new ManifestScooterListBean();
                title.setUldType("??????");
                title.setUldCode("???????????????");
                title.setTotal("??????");
                title.setWeight("??????");
                title.setSpecialNumber("????????????");
                title.setMailType("????????????");
                manifestScooterListBeans.add(0, title);
            }
            Gson mGson = new Gson();
            ManifestMainBean[] datas = mGson.fromJson(result.getContent(), ManifestMainBean[].class);
            //?????????????????????
            for (int i = 0; i < datas.length; i++) {
                for (int j = 0; j < datas[i].getCargos().size(); j++) {
                    for (int k = 0; k < datas[i].getCargos().get(j).getScooters().size(); k++) {
                        //??????????????? ????????????
                        for (int l = 0; l < datas[i].getCargos().get(j).getScooters().get(k).getWaybillList().size(); l++) {
                            datas[i].getCargos().get(j).getScooters().get(k).getWaybillList().get(l).setRouteEn(datas[i].getRouteEn());
                        }
                        //??????????????? ??????
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

    /**
     * ???????????????????????? ??????
     *
     * @param result
     */
    @Override
    public void lockOrUnlockScooterResult(String result) {
        if (currentLockPosition != -1) {
            if (newScooters.get(currentLockPosition).getLock() == 0 || newScooters.get(currentLockPosition).getLock() == 3)
                newScooters.get(currentLockPosition).setLock(1);
            else
                newScooters.get(currentLockPosition).setLock(0);
        }
        if (result != null)
            ToastUtil.showToast("????????????");

        adapter.notifyDataSetChanged();
    }

    @Override
    public void startPullResult(String result) {
        if (result != null)
            ToastUtil.showToast(result);

    }
}
