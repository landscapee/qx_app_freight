package qx.app.freight.qxappfreight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.UnloadPlaneAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.LoadingListRequestEntity;
import qx.app.freight.qxappfreight.bean.request.LoadingListSendEntity;
import qx.app.freight.qxappfreight.bean.response.GetFlightCargoResBean;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;
import qx.app.freight.qxappfreight.contract.GetFlightCargoResContract;
import qx.app.freight.qxappfreight.dialog.UpdatePushDialog;
import qx.app.freight.qxappfreight.dialog.WaitCallBackDialog;
import qx.app.freight.qxappfreight.presenter.GetFlightCargoResPresenter;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomRecylerView;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 理货装机页面
 */
public class LoadPlaneActivity extends BaseActivity implements GetFlightCargoResContract.getFlightCargoResView {
    @BindView(R.id.rv_data)
    CustomRecylerView mRvData;
    @BindView(R.id.tv_plane_info)
    TextView mTvPlaneInfo;
    @BindView(R.id.tv_flight_craft)
    TextView mTvFlightType;
    @BindView(R.id.tv_start_place)
    TextView mTvStartPlace;
    @BindView(R.id.iv_two_place)
    ImageView mIvTwoPlace;
    @BindView(R.id.tv_middle_place)
    TextView mTvMiddlePlace;
    @BindView(R.id.tv_target_place)
    TextView mTvTargetPlace;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {
        if (result != null) {
            if (result.isCancelFlag()) {
                String taskId = result.getTaskId();
                if (taskId.equals(mCurrentTaskId)) {
                    ToastUtil.showToast("当前装机任务已取消");
                    Observable.timer(2, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()) // timer 默认在新线程，所以需要切换回主线程
                            .subscribe(aLong -> finish());
                }
            }
        }
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
        LoadAndUnloadTodoBean data = (LoadAndUnloadTodoBean) getIntent().getSerializableExtra("plane_info");
        mCurrentTaskId = data.getTaskId();
        toolbar.setMainTitle(Color.WHITE, data.getFlightNo() + "  装机");
        mTvPlaneInfo.setText(data.getFlightNo());
        mTvFlightType.setText(data.getAircraftno());
        String route = data.getRoute();
        String start = "", middle = "", end = "";
        if (route != null) {
            String[] placeArray = route.split(",");
            List<String> resultList = new ArrayList<>();
            for (String str : placeArray) {
                String temp = str.replaceAll("[^(a-zA-Z\\u4e00-\\u9fa5)]", "");
                resultList.add(temp);
            }
            if (resultList.size() == 3) {
                middle = resultList.get(1);
            }
            start = resultList.get(0);
            end = resultList.get(resultList.size() - 1);
        }
        if (TextUtils.isEmpty(start)) {//起点都没有，说明没有航线信息，全部隐藏
            mTvStartPlace.setVisibility(View.GONE);
            mIvTwoPlace.setVisibility(View.GONE);
            mTvMiddlePlace.setVisibility(View.GONE);
            mTvTargetPlace.setVisibility(View.GONE);
        } else {
            if (TextUtils.isEmpty(middle)) {//没有中转站信息
                mTvStartPlace.setVisibility(View.VISIBLE);
                mTvStartPlace.setText(start);
                mIvTwoPlace.setVisibility(View.VISIBLE);
                mTvMiddlePlace.setVisibility(View.GONE);
                mTvTargetPlace.setVisibility(View.VISIBLE);
                mTvTargetPlace.setText(end);
            } else {
                mTvStartPlace.setVisibility(View.VISIBLE);
                mIvTwoPlace.setVisibility(View.GONE);
                mTvMiddlePlace.setVisibility(View.VISIBLE);
                mTvTargetPlace.setVisibility(View.VISIBLE);
                mTvStartPlace.setText(start);
                mTvMiddlePlace.setText(middle);
                mTvTargetPlace.setText(end);
            }
        }
        mTvSeat.setText(data.getSeat());
        mTvStartTime.setText(TimeUtils.getHMDay(data.getScheduleTime()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //配置布局，默认为vertical（垂直布局），下边这句将布局改为水平布局
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvData.setLayoutManager(linearLayoutManager);
        mPresenter = new GetFlightCargoResPresenter(this);
        mCurrentFlightId = data.getFlightId();
        LoadingListRequestEntity entity = new LoadingListRequestEntity();
        entity.setDocumentType(2);
        entity.setFlightId(mCurrentFlightId);
        ((GetFlightCargoResPresenter) mPresenter).getLoadingList(entity);
        mTvPullGoodsReport.setOnClickListener(v -> {
            if (mLoadingList.size() == 0) {
                ToastUtil.showToast("当前航班无装机单数据，暂时无法进行下一步操作");
            } else {
                Intent intent = new Intent(LoadPlaneActivity.this, PullGoodsReportActivity.class);
                intent.putExtra("plane_info", data);
                intent.putExtra("loading_list_data", mLoadingList.get(0));
                LoadPlaneActivity.this.startActivity(intent);
            }
        });
        mTvErrorReport.setOnClickListener(v -> {
            Intent intent = new Intent(LoadPlaneActivity.this, ErrorReportActivity.class);
            intent.putExtra("flight_number", data.getFlightNo());//航班号
            intent.putExtra("task_id", data.getTaskId());//任务id
            intent.putExtra("flight_id", data.getFlightId());//Flight id
            intent.putExtra("error_type", 1);
            LoadPlaneActivity.this.startActivity(intent);
        });
        mTvEndInstall.setOnClickListener(v -> {
            if (mLoadingList.size() == 0) {
                ToastUtil.showToast("当前航班无装机单数据，暂时无法进行下一步操作");
            } else {
                boolean doRight = true;
                for (LoadingListBean.DataBean entity1 : mLoadingList) {
                    for (LoadingListBean.DataBean.ContentObjectBean entity2 : entity1.getContentObject()) {
                        if (!entity2.isLocked()) {
                            doRight = false;
                            break;
                        }
                    }
                }
                if (doRight) {
                    GetFlightCargoResBean bean = new GetFlightCargoResBean();
                    bean.setTpFlightId(data.getFlightId());
                    bean.setTaskId(data.getTaskId());
                    bean.setTpOperator(UserInfoSingle.getInstance().getUserId());
                    ((GetFlightCargoResPresenter) mPresenter).flightDoneInstall(bean);
                } else {
                    ToastUtil.showToast("有未锁定修改的数据，请检查！");
                }
            }
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
                LoadingListSendEntity requestModel = new LoadingListSendEntity();
                requestModel.setCreateDate(entity.getCreateDate());
                requestModel.setFlightNo(entity.getFlightNo());
                requestModel.setLoadingUser(UserInfoSingle.getInstance().getUsername());
                requestModel.setCreateUser(entity.getCreateUser());
                requestModel.setFlightId(entity.getFlightId());
                requestModel.setContent(entity.getContentObject());
                ((GetFlightCargoResPresenter) mPresenter).overLoad(requestModel);
            });
        }
    }

    @Override
    public void flightDoneInstallResult(String result) {
        ToastUtil.showToast("结束装机成功");
        Log.e("tagNet", "result=====" + result);
        EventBus.getDefault().post("InstallEquipFragment_refresh");
        finish();
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
    }

    @Override
    public void dissMiss() {
    }
}
