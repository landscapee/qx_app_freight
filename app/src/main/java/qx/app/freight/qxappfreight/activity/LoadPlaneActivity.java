package qx.app.freight.qxappfreight.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import qx.app.freight.qxappfreight.adapter.UnloadPlaneAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.loadinglist.CompareInfoBean;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.LoadingListRequestEntity;
import qx.app.freight.qxappfreight.bean.request.LoadingListSendEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.GetFlightCargoResBean;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;
import qx.app.freight.qxappfreight.contract.GetFlightCargoResContract;
import qx.app.freight.qxappfreight.contract.LoadAndUnloadTodoContract;
import qx.app.freight.qxappfreight.dialog.UpdatePushDialog;
import qx.app.freight.qxappfreight.dialog.WaitCallBackDialog;
import qx.app.freight.qxappfreight.presenter.GetFlightCargoResPresenter;
import qx.app.freight.qxappfreight.presenter.LoadAndUnloadTodoPresenter;
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

/**
 * 装机页面
 */
public class LoadPlaneActivity extends BaseActivity implements GetFlightCargoResContract.getFlightCargoResView, LoadAndUnloadTodoContract.loadAndUnloadTodoView {
    @BindView(R.id.rv_data)
    CustomRecylerView mRvData;
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
    private List<LoadingListBean.DataBean> mLoadingList = new ArrayList<>();
    private String mCurrentTaskId;
    private String mCurrentFlightId;
    private WaitCallBackDialog mWaitCallBackDialog;
    private LoadAndUnloadTodoBean data;
    @SuppressLint("UseSparseArrays")
    private Map<Integer, List<CompareInfoBean>> mBaseIdMap = new HashMap<>();
    @SuppressLint("UseSparseArrays")
    private Map<Integer, List<CompareInfoBean>> mNewIdMap = new HashMap<>();
    private ArrayList<LoadingListBean.DataBean.ContentObjectBean> mBaseContent;
    private int mOperateErrorStatus = -1;
    private boolean mConfirmPlan = false;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {
        PushDataUtil.handlePushInfo(result, mCurrentTaskId, this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String result) {
        if (result != null && result.equals(mCurrentFlightId)) {
            showCargoResUpdate();
        }
    }

    private void showCargoResUpdate() {
        UpdatePushDialog updatePushDialog = new UpdatePushDialog(this, R.style.custom_dialog, "预装机单已更新，请查看！", () -> {
            LoadingListRequestEntity entity = new LoadingListRequestEntity();
            entity.setDocumentType(2);
            entity.setFlightId(mCurrentFlightId);
            ((GetFlightCargoResPresenter) mPresenter).getLoadingList(entity);
        });
        if (mWaitCallBackDialog != null) {
            mWaitCallBackDialog.dismiss();
        }
        Tools.startVibrator(getApplicationContext(), true, R.raw.ring);
        updatePushDialog.show();
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //配置布局，默认为vertical（垂直布局），下边这句将布局改为水平布局
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvData.setLayoutManager(linearLayoutManager);
        mPresenter = new GetFlightCargoResPresenter(this);
        mCurrentFlightId = mIsKeepOnTask ? data.getRelateInfoObj().getFlightId() : data.getFlightId();
        LoadingListRequestEntity entity = new LoadingListRequestEntity();
        entity.setDocumentType(2);
        entity.setFlightId(mCurrentFlightId);
        ((GetFlightCargoResPresenter) mPresenter).getLoadingList(entity);
        mTvSendOver.setOnClickListener(v -> {
            mConfirmPlan = false;
            LoadingListSendEntity requestModel = new LoadingListSendEntity();
            requestModel.setFlightNo(data.getFlightNo());
            requestModel.setCreateTime(mLoadingList.get(0).getCreateTime());
            requestModel.setLoadingUser(UserInfoSingle.getInstance().getUsername());
            requestModel.setCreateUser(mLoadingList.get(0).getCreateUser());
            requestModel.setFlightInfoId(mLoadingList.get(0).getFlightInfoId());
            requestModel.setContent(mLoadingList.get(0).getContentObject());
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
        });
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
                mPresenter = new GetFlightCargoResPresenter(this);
                BaseFilterEntity<Object> entity = new BaseFilterEntity<>();
                entity.setFlightInfoId(mLoadingList.get(0).getFlightInfoId());
                ((GetFlightCargoResPresenter) mPresenter).getPullStatus(entity);
                for (LoadingListBean.DataBean bean : mLoadingList) {
                    bean.setShowDetail(true);
                }
                if (!TextUtils.isEmpty(result.getData().get(0).getContent())) {
                    Gson mGson = new Gson();
                    mTvSendOver.setEnabled(false);
                    mTvConfirmCargo.setEnabled(true);
                    mTvSendOver.setTextColor(Color.parseColor("#888888"));
                    mTvConfirmCargo.setTextColor(Color.parseColor("#ff0000"));
                    LoadingListBean.DataBean.ContentObjectBean[] datas = mGson.fromJson(result.getData().get(0).getContent(), LoadingListBean.DataBean.ContentObjectBean[].class);
                    //舱位集合
                    mBaseContent = new ArrayList<>(Arrays.asList(datas));
                    mBaseIdMap.clear();
                    for (int i = 0; i < mBaseContent.size(); i++) {
                        List<CompareInfoBean> idList = new ArrayList<>();
                        for (LoadingListBean.DataBean.ContentObjectBean.ScooterBean scooterBean : mBaseContent.get(i).getScooters()) {
                            CompareInfoBean bean = new CompareInfoBean();
                            bean.setId(scooterBean.getId());
                            bean.setPullStatus(scooterBean.getExceptionFlag());
                            idList.add(bean);
                        }
                        mBaseIdMap.put(i, idList);
                    }
                    Disposable subscription = Observable.just(result.getData().get(0).getContent()).flatMap((Function<String, ObservableSource<List<LoadingListBean.DataBean.ContentObjectBean>>>) s -> {
                        LoadingListBean.DataBean.ContentObjectBean[] datasNew = mGson.fromJson(s, LoadingListBean.DataBean.ContentObjectBean[].class);
                        return Observable.just(new ArrayList<>(Arrays.asList(datasNew)));
                    }).subscribe(boardMultiBeans -> {
                        result.getData().get(0).setContentObject(boardMultiBeans);
                        UnloadPlaneAdapter adapter = new UnloadPlaneAdapter(mLoadingList);
                        mRvData.setAdapter(adapter);
                        adapter.setOnDataCheckListener(() -> {
                            for (LoadingListBean.DataBean.ContentObjectBean contentObjectBean : boardMultiBeans) {
                                for (LoadingListBean.DataBean.ContentObjectBean.ScooterBean scooterBean : contentObjectBean.getScooters()) {
                                    Iterator iterator = scooterBean.getWaybillList().iterator();
                                    while (iterator.hasNext()) {
                                        LoadingListBean.DataBean.ContentObjectBean.ScooterBean.WaybillBean bill = (LoadingListBean.DataBean.ContentObjectBean.ScooterBean.WaybillBean) iterator.next();
                                        if (bill.isTitle()) {
                                            iterator.remove();
                                        }
                                    }
                                }
                            }
                            mNewIdMap.clear();
                            for (int i = 0; i < boardMultiBeans.size(); i++) {
                                List<CompareInfoBean> idList = new ArrayList<>();
                                for (LoadingListBean.DataBean.ContentObjectBean.ScooterBean scooterBean : boardMultiBeans.get(i).getScooters()) {
                                    CompareInfoBean bean = new CompareInfoBean();
                                    bean.setId(scooterBean.getId());
                                    bean.setPullStatus(scooterBean.getExceptionFlag());
                                    idList.add(bean);
                                }
                                mNewIdMap.put(i, idList);
                            }
                            boolean modified = false;
                            for (Integer set : mBaseIdMap.keySet()) {
                                List<CompareInfoBean> ids = mBaseIdMap.get(set);
                                List<CompareInfoBean> idNews = mNewIdMap.get(set);
                                for (CompareInfoBean s : Objects.requireNonNull(idNews)) {
                                    if (!Objects.requireNonNull(ids).contains(s)) {
                                        modified = true;
                                    }
                                }
                            }
                            for (Integer set : mNewIdMap.keySet()) {
                                List<CompareInfoBean> ids = mNewIdMap.get(set);
                                List<CompareInfoBean> idNews = mBaseIdMap.get(set);
                                for (CompareInfoBean s : Objects.requireNonNull(idNews)) {
                                    if (!Objects.requireNonNull(ids).contains(s)) {
                                        modified = true;
                                    }
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
                        });
                    });
                    Log.e("tagTest", "string====" + subscription.toString());
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLoadingList != null && mLoadingList.size() != 0) {
            mPresenter = new GetFlightCargoResPresenter(this);
            BaseFilterEntity<Object> entity = new BaseFilterEntity<>();
            entity.setFlightInfoId(mLoadingList.get(0).getFlightInfoId());
            ((GetFlightCargoResPresenter) mPresenter).getPullStatus(entity);
        }
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
    public void loadAndUnloadTodoResult(List<LoadAndUnloadTodoBean> loadAndUnloadTodoBean) {

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
    public void getPullStatusResult(BaseEntity<String> result) {
        if (Integer.valueOf(result.getData()) != 0) {
            mIvHint.setVisibility(View.VISIBLE);
        } else {
            mIvHint.setVisibility(View.GONE);
        }
    }

}
