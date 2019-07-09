package qx.app.freight.qxappfreight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.UnloadPlaneAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.LoadingListRequestEntity;
import qx.app.freight.qxappfreight.bean.request.LoadingListSendEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
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
 * 理货装机页面
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
    @BindView(R.id.tv_end_install_equip)
    TextView mTvEndInstall;
    private List<LoadingListBean.DataBean> mLoadingList = new ArrayList<>();
    private String mCurrentTaskId;
    private String mCurrentFlightId;
    private WaitCallBackDialog mWaitCallBackDialog;
    private LoadAndUnloadTodoBean data;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {
        PushDataUtil.handlePushInfo(result, mCurrentTaskId, this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String result) {
        if (result != null && result.equals(mCurrentFlightId)) {
            showCargoResUpdate(mCurrentFlightId);
        }
    }

    private void showCargoResUpdate(String flightId) {
        UpdatePushDialog updatePushDialog = new UpdatePushDialog(this, R.style.custom_dialog, flightId, s -> {
            LoadingListRequestEntity entity = new LoadingListRequestEntity();
            entity.setDocumentType(2);
            entity.setFlightId(mCurrentFlightId);
            ((GetFlightCargoResPresenter) mPresenter).getLoadingList(entity);
        });
        if (mWaitCallBackDialog != null) {
            mWaitCallBackDialog.dismiss();
        }
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
        mTvPullGoodsReport.setOnClickListener(v -> {
            Intent intent = new Intent(LoadPlaneActivity.this, PullGoodsReportActivity.class);
            intent.putExtra("plane_info", data);
            intent.putExtra("id", data.getId());
            LoadPlaneActivity.this.startActivity(intent);
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
//            if (mLoadingList.size() == 0) {
//                ToastUtil.showToast("当前航班无装机单数据，暂时无法进行下一步操作");
//            } else {
            boolean doRight = true;//全部装机单的状态锁定后才能提交结束装机，暂取消判断
               /* for (LoadingListBean.DataBean entity1 : mLoadingList) {
                    for (LoadingListBean.DataBean.ContentObjectBean entity2 : entity1.getContentObject()) {
                        if (!entity2.isLocked()) {
                            doRight = false;
                            break;
                        }
                    }
                }*/
            if (doRight) {
                GetFlightCargoResBean bean = new GetFlightCargoResBean();
                bean.setTpFlightId(data.getFlightId());
                bean.setTaskId(data.getId());
                bean.setTpOperator(UserInfoSingle.getInstance().getUserId());
                ((GetFlightCargoResPresenter) mPresenter).flightDoneInstall(bean);
            } else {
                ToastUtil.showToast("有未锁定修改的数据，请检查！");
            }
//            }
        });
    }

    @Override
    public void getLoadingListResult(LoadingListBean result) {
        if ("318".equals(result.getStatus())) {
            mWaitCallBackDialog.show();
        } else {
            mLoadingList.clear();
            if (result.getData() == null || result.getData().size() == 0) return;
            mLoadingList.addAll(result.getData());
            Collections.reverse(mLoadingList);
            UnloadPlaneAdapter adapter = new UnloadPlaneAdapter(mLoadingList);
            mRvData.setAdapter(adapter);
            adapter.setOnOverLoadListener(entity -> {
                if (entity.getContentObject() != null && entity.getContentObject().size() != 0) {
                    LoadingListSendEntity requestModel = new LoadingListSendEntity();
                    requestModel.setCreateTime(entity.getCreateTime());
                    requestModel.setLoadingUser(UserInfoSingle.getInstance().getUsername());
                    requestModel.setCreateUser(entity.getCreateUser());
                    requestModel.setFlightInfoId(entity.getFlightInfoId());
                    requestModel.setContent(entity.getContentObject());
                    ((GetFlightCargoResPresenter) mPresenter).overLoad(requestModel);
                } else {
                    ToastUtil.showToast("获取装机单内容失败，无法通知录入装机");
                }
            });
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
        Log.e("tagNet", "result=====" + result);
        EventBus.getDefault().post("InstallEquipFragment_refresh" + "@" + mCurrentTaskId);
        finish();
    }
}
