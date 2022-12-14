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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.TaskTpDoneAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.DoneTaskEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.response.AcceptTerminalTodoBean;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.OutFieldTaskBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.LoadAndUnloadTodoContract;
import qx.app.freight.qxappfreight.contract.TransportTaskHisContract;
import qx.app.freight.qxappfreight.presenter.LoadAndUnloadTodoPresenter;
import qx.app.freight.qxappfreight.presenter.TransportTaskHisPresenter;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/**
 * ??????????????????
 */
public class TaskDriverOutDoneFragment extends BaseFragment implements MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter, TransportTaskHisContract.transportTaskHisView, LoadAndUnloadTodoContract.loadAndUnloadTodoView {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;

    List<AcceptTerminalTodoBean> listCache;

    private List<OutFieldTaskBean> list;
    private int slidePosition, slidePositionChild, step;
    private TaskTpDoneAdapter adapter;
    private int currentPage = 1;

    private TaskDoneFragment mTaskFragment; //?????????fragment
    private SearchToolbar searchToolbar;//?????????????????????
    private CustomToolbar mToolBar;//???????????????
    private boolean isShow = false;

    private int max = 0, index = 0; //??????????????????????????????????????????

    private String nowDoTaskId = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_driver_out_done, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvData.setRefreshListener(this);
        mMfrvData.setOnRetryLisenter(this);
        mTaskFragment = (TaskDoneFragment) getParentFragment();
        searchToolbar = mTaskFragment.getSearchView();
        mToolBar = mTaskFragment.getToolbar();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        initData();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUserVisibleHint(true);
    }
    private void initData() {
        listCache = new ArrayList<>();
        list = new ArrayList<>();
        adapter = new TaskTpDoneAdapter(list);
        mMfrvData.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            if (!nowDoTaskId.equals(list.get(position).getTaskId())) {
                nowDoTaskId = list.get(position).getTaskId();
//                setListStatus();
            }

        });
        getData();
    }

    private void getData() {
        mPresenter = new TransportTaskHisPresenter(this);
        BaseFilterEntity<DoneTaskEntity> entity = new BaseFilterEntity();
        DoneTaskEntity doneTaskEntity = new DoneTaskEntity();
        doneTaskEntity.setOperatorId(UserInfoSingle.getInstance().getUserId());
        entity.setCurrent(currentPage);
        entity.setSize(Constants.PAGE_SIZE);
        entity.setFilter(doneTaskEntity);
        ((TransportTaskHisPresenter) mPresenter).transportTaskHis(entity);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isShow = isVisibleToUser;
        if (isVisibleToUser) {
            Log.e("111111", "setUserVisibleHint: " + "??????");
            if (mTaskFragment != null)
                mTaskFragment.setTitleText(list.size());
            if (mToolBar != null) {
                mToolBar.setRightIconViewVisiable(false);
            }

        } else {
            if (mToolBar != null) {
                mToolBar.setRightIconViewVisiable(true);
            }
        }
    }

    /**
     * x???????????? ??????????????????????????? ???????????? ??????
     *
     * @param mOutFieldTaskBean
     * @param step
     */
    private void submitStep(OutFieldTaskBean mOutFieldTaskBean, int step) {
        index++;
        mPresenter = new LoadAndUnloadTodoPresenter(this);
        PerformTaskStepsEntity entity = new PerformTaskStepsEntity();
        entity.setType(0);
        entity.setLoadUnloadDataId(mOutFieldTaskBean.getId());
        entity.setFlightId(Long.valueOf(mOutFieldTaskBean.getFlightId()));
        entity.setFlightTaskId(mOutFieldTaskBean.getTaskId());
        entity.setLatitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLatitude()+"");
        entity.setLongitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLongitude()+"");

        if (step == 0)
            entity.setOperationCode(Constants.TP_START);//????????????
        else if (step == 1)
            entity.setOperationCode(Constants.TP_END);//????????????
        else
            entity.setOperationCode(Constants.TP_ACCEPT);//????????????

        entity.setUserName(UserInfoSingle.getInstance().getUsername());

        entity.setTerminalId(DeviceInfoUtil.getDeviceInfo(getContext()).get("deviceId"));
        entity.setUserId(UserInfoSingle.getInstance().getUserId());
        entity.setCreateTime(System.currentTimeMillis());
        ((LoadAndUnloadTodoPresenter) mPresenter).slideTask(entity);

        nowDoTaskId = mOutFieldTaskBean.getTaskId();
    }

    @Override
    public void onRetry() {
        showProgessDialog("????????????????????????");
        new Handler().postDelayed(() -> {
            getData();
            dismissProgessDialog();
        }, 2000);
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        getData();
    }

    @Override
    public void onLoadMore() {
        getData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String result) {
        if (result.equals("TaskDriverOutFragment_refresh")||"refresh_data_update".equals(result)) {
            currentPage = 1;
            getData();
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @param result
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {


    }


    /**
     * ??????????????????????????????
     *
     * @param acceptTerminalTodoBeanList
     */
    @Override
    public void transportTaskHisResult(List<OutFieldTaskBean> acceptTerminalTodoBeanList) {
        if (acceptTerminalTodoBeanList != null) {

            if (currentPage == 1) {
                list.clear();
            }
            currentPage++;

            mMfrvData.finishRefresh();
            mMfrvData.finishLoadMore();

            list.addAll(acceptTerminalTodoBeanList);
            mMfrvData.notifyForAdapter(adapter);
            if (mTaskFragment != null) {
                if (isShow)
                    mTaskFragment.setTitleText(list.size());
            }

        } else {
            Log.e("??????", "??????????????????");
        }
    }


    /**
     * ?????? ??????????????????????????? ????????????
     *
     * @param mAcceptTerminalTodoBean
     * @return
     */
    private List<List<OutFieldTaskBean>> getUseTasks(AcceptTerminalTodoBean mAcceptTerminalTodoBean) {

        Map<String, List<OutFieldTaskBean>> map = new HashMap<>();

        for (OutFieldTaskBean task : mAcceptTerminalTodoBean.getTasks()) {
            if (map.get(task.getBeginAreaCargoType()) == null) {
                List<OutFieldTaskBean> list = new ArrayList<>();
                list.add(task);
                map.put(task.getBeginAreaCargoType(), list);
            } else {
                map.get(task.getBeginAreaCargoType()).add(task);
            }

        }
        List<List<OutFieldTaskBean>> OutFieldTaskBeans = new ArrayList<>(map.values());

        return OutFieldTaskBeans;
    }

    @Override
    public void toastView(String error) {
        if (mMfrvData != null)
            mMfrvData.finishLoadMore();
        if (mMfrvData != null)
            mMfrvData.finishRefresh();
    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }

    @Override
    public void loadAndUnloadTodoResult(List<LoadAndUnloadTodoBean> loadAndUnloadTodoBean) {

    }

    @Override
    public void slideTaskResult(String result) {

        if (index == max) {
            ToastUtil.showToast("??????????????????");
            currentPage = 1; //????????????
            index = 0;
            getData();
        }
//        StepBean stepBean = JSON.parseObject(result,StepBean.class);
//        upDateStepStatus(stepBean);
    }

    @Override
    public void startClearTaskResult(String result) {

    }


}
