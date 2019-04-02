package qx.app.freight.qxappfreight.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ouyben.empty.EmptyLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.InstallEquipAdapter;
import qx.app.freight.qxappfreight.adapter.InstallEquipStepAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.InstallEquipEntity;
import qx.app.freight.qxappfreight.bean.MultiStepEntity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.contract.LoadAndUnloadTodoContract;
import qx.app.freight.qxappfreight.dialog.PushLoadUnloadDialog;
import qx.app.freight.qxappfreight.presenter.LoadAndUnloadTodoPresenter;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/**
 * 装机fragment
 */
public class InstallEquipFragment extends BaseFragment implements MultiFunctionRecylerView.OnRefreshListener, LoadAndUnloadTodoContract.loadAndUnloadTodoView, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;
    private List<InstallEquipEntity> mList = new ArrayList<>();
    private int mCurrentPage = 1;
    private int mCurrentSize = 10;
    private static final String[] mStepNamesInstall = {"领受", "到位", "开启舱门", "装机", "关闭舱门"};
    private static final String[] mStepNamesUninstall = {"领受", "到位", "开启舱门", "卸机", "关闭舱门"};
    private static final String[] mStepCodeInstall = {"FreightPass_receive", "FreightPass_ready", "FreightPass_open", "FreightPass_load_begin", "FreightPass_close"};
    private static final String[] mStepCodeUninstall = {"FreightPass_receive", "FreightPass_ready", "FreightPass_open", "FreightPass_unload_begin", "FreightPass_close"};
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINESE);
    private InstallEquipStepAdapter mSlideadapter;
    private int mOperatePos;
    private List<LoadAndUnloadTodoBean> mListCache = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_install_equip, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvData.setRefreshListener(this);
        mMfrvData.setOnRetryLisenter(this);
        mPresenter = new LoadAndUnloadTodoPresenter(this);
        loadData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String result) {
        if (result.equals("InstallEquipFragment_refresh")) {
            loadData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {
        if (result != null) {
            if (result.isCancelFlag()) {
                loadData();
            } else {
                List<LoadAndUnloadTodoBean> list = result.getTaskData();
                if (list != null)
                    mListCache.addAll(list);
                PushLoadUnloadDialog dialog = new PushLoadUnloadDialog();
                dialog.setData(getContext(), mListCache, success -> {
                    if (success) {
                        ToastUtil.showToast("领受装卸机新任务成功");
                        loadData();
                        mListCache.clear();
                    } else {
                        mListCache.clear();
                        Log.e("tagPush", "推送出错了");
                    }
                });
                dialog.setCancelable(false);
                dialog.show(getFragmentManager(), "333");
            }
        }
    }

    private void loadData() {
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setWorkerId(UserInfoSingle.getInstance().getUserId());
//        entity.setWorkerId("3628f73591914a48ab5613d6c7b7ce64");
        entity.setCurrent(mCurrentPage);
        entity.setSize(mCurrentSize);
        ((LoadAndUnloadTodoPresenter) mPresenter).LoadAndUnloadTodo(entity);
    }

    @Override
    public void onRetry() {
        showProgessDialog("正在加载数据……");
        new Handler().postDelayed(() -> {
            loadData();
            dismissProgessDialog();
        }, 2000);
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        loadData();
    }

    @Override
    public void onLoadMore() {
        loadData();
    }

    @Override
    public void loadAndUnloadTodoResult(List<LoadAndUnloadTodoBean> loadAndUnloadTodoBean) {
        if (loadAndUnloadTodoBean.size() == 0) return;
        List<Boolean> checkedList = new ArrayList<>();
        //未分页
        mList.clear();
        if (mCurrentPage == 1) {
//            mList.clear();
            mMfrvData.finishRefresh();
        } else {
            mMfrvData.finishLoadMore();
        }
        mCurrentPage++;
        for (LoadAndUnloadTodoBean bean : loadAndUnloadTodoBean) {
            InstallEquipEntity entity = new InstallEquipEntity();
            entity.setShowDetail(false);
            entity.setAirCraftNo(bean.getAircraftno());
            entity.setFlightInfo(bean.getFlightNo());
            entity.setSeat(bean.getSeat());
            entity.setTaskTpye(bean.getTaskType());//1，装机；2，卸机
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd HH:mm", Locale.CHINESE);
            String time = sdf2.format(new Date(bean.getScheduleTime()));
            String hourMinute = time.substring(3);
            String day = time.substring(0, 2);
            String result;
            if (Integer.valueOf(day) < 10) {
                result = hourMinute + "(" + day.substring(1) + ")";
            } else {
                result = hourMinute + "(" + day + ")";
            }
            entity.setScheduleTime(result);
            String places = bean.getRoute();
            places = places.substring(1, places.length() - 1).replaceAll("\",\"", ",");
            String[] placeArray = places.substring(1, places.length() - 1).split(",");
            if (placeArray.length == 2) {
                entity.setStartPlace(placeArray[0]);
                entity.setMiddlePlace("--");
                entity.setEndPlace(placeArray[1]);
            } else {
                entity.setStartPlace(placeArray[0]);
                entity.setMiddlePlace(placeArray[1]);
                entity.setEndPlace(placeArray[2]);
            }
            entity.setLoadUnloadType(bean.getTaskType());
            List<MultiStepEntity> data = new ArrayList<>();
            List<String> times = new ArrayList<>();
            times.add(String.valueOf(bean.getAcceptTime()));
            times.add(String.valueOf(bean.getArrivalTime()));
            times.add(String.valueOf(bean.getOpenDoorTime()));
            times.add(bean.getBeginLoadUnloadTime() + ":" + bean.getLoadUnloadTime());
            times.add(String.valueOf(bean.getCloseDoorTime()));
            //FlightNumber   0               AirCraftNo   1               StartPlace   2           MiddlePlace     3            EndPlace    4
            String planeInfo = entity.getFlightInfo() + "*" + entity.getAirCraftNo() + "*" + entity.getStartPlace() + "*" + entity.getMiddlePlace() + "*" + entity.getEndPlace()
                    //机位     5              6， scheduleTime                         7，FlightId                ActualTakeoffTime  8                   ActualArriveTime 9                 Movement  10      Id   11
                    + "*" + entity.getSeat() + "*" + bean.getScheduleTime() + "*" + bean.getFlightId() + "*" + bean.getActualTakeoffTime() + "*" + bean.getActualArriveTime() + "*" + bean.getMovement() + "*" + bean.getId() + "*" + bean.getTaskId();
            int posNow = 0;
            boolean hasChecked = false;
            for (int i = 0; i < times.size(); i++) {
                String timeNow = times.get(i);
                if ("0".equals(timeNow)) {
                    posNow = i;
                    break;
                } else if (timeNow.contains(":0")) {
                    hasChecked = true;
                    posNow = i;
                    break;
                }
            }
            checkedList.add(hasChecked);
            for (int i = 0; i < 5; i++) {
                MultiStepEntity entity1 = new MultiStepEntity();
                entity1.setLoadUnloadType(bean.getTaskType());
                if (bean.getTaskType() == 1) {//装机
                    entity1.setStepName(mStepNamesInstall[i]);
                } else {
                    entity1.setStepName(mStepNamesUninstall[i]);
                }
                int type;
                if (i < posNow) {
                    type = 0;
                } else if (i == posNow) {
                    type = 1;
                } else {
                    type = 2;
                }
                entity1.setItemType(type);
                entity1.setPlaneInfo(planeInfo);
                if (i == 3) {
                    String[] timeArray = times.get(i).split(":");
                    String start = ("0".equals(timeArray[0])) ? "" : sdf.format(new Date(Long.valueOf(timeArray[0])));
                    String end = ("0".equals(timeArray[1])) ? "" : sdf.format(new Date(Long.valueOf(timeArray[1])));
                    entity1.setStepDoneDate(start + "-" + end);
                } else {
                    entity1.setStepDoneDate("0".equals(times.get(i)) ? "" : sdf.format(new Date(Long.valueOf(times.get(i)))));
                }
                data.add(entity1);
            }
            entity.setList(data);
            mList.add(entity);
        }
        InstallEquipAdapter mAdapter = new InstallEquipAdapter(mList);
        mMfrvData.setAdapter(mAdapter);
        mAdapter.setOnSlideStepListener((bigPos, adapter, smallPos) -> {
            if (smallPos == 3 && checkedList.get(bigPos)) {
                Log.e("tagTest", "已经开始装卸机，但是返回退出了页面！");
            } else {
                mOperatePos = smallPos;
                mSlideadapter = adapter;
                PerformTaskStepsEntity entity = new PerformTaskStepsEntity();
                entity.setType(1);
                entity.setLoadUnloadDataId(loadAndUnloadTodoBean.get(bigPos).getId());
                entity.setFlightId(Long.valueOf(loadAndUnloadTodoBean.get(bigPos).getFlightId()));
                entity.setFlightTaskId(loadAndUnloadTodoBean.get(bigPos).getTaskId());
                entity.setLatitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLatitude());
                entity.setLongitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLongitude());
                if (loadAndUnloadTodoBean.get(bigPos).getTaskType() == 1) {
                    entity.setOperationCode(mStepCodeInstall[smallPos]);
                } else {
                    entity.setOperationCode(mStepCodeUninstall[smallPos]);
                }
                entity.setTerminalId(DeviceInfoUtil.getDeviceInfo(getContext()).get("deviceId"));
                entity.setUserId(UserInfoSingle.getInstance().getUserId());
                entity.setUserName(loadAndUnloadTodoBean.get(bigPos).getWorkerName());
                entity.setCreateTime(System.currentTimeMillis());
                ((LoadAndUnloadTodoPresenter) mPresenter).slideTask(entity);
            }
        });
        TaskFragment fragment = (TaskFragment) getParentFragment();
        if (fragment != null) {
            fragment.setTitleText(mList.size());
        }
    }

    @Override
    public void slideTaskResult(String result) {
        if ("正确".equals(result)) {
            mSlideadapter.notifyDataSetChanged();
            if (mOperatePos == 4) {
                mCurrentPage = 1;
                loadData();
                mOperatePos = 0;
            }
        }
    }

    @Override
    public void toastView(String error) {
        if (mCurrentPage == 1) {
            mMfrvData.finishRefresh();
        } else {
            mMfrvData.finishLoadMore();
        }
    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }
}
