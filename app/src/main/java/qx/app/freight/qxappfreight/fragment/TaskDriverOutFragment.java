package qx.app.freight.qxappfreight.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
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
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.DriverOutTaskAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.response.AcceptTerminalTodoBean;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.OutFieldTaskBean;
import qx.app.freight.qxappfreight.bean.response.StepBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.AcceptTerminalTodoContract;
import qx.app.freight.qxappfreight.contract.LoadAndUnloadTodoContract;
import qx.app.freight.qxappfreight.dialog.TpPushDialog;
import qx.app.freight.qxappfreight.presenter.AcceptTerminalTodoPresenter;
import qx.app.freight.qxappfreight.presenter.LoadAndUnloadTodoPresenter;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.IMUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/**
 * 外场运输
 */
public class TaskDriverOutFragment extends BaseFragment implements MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter, AcceptTerminalTodoContract.acceptTerminalTodoView, LoadAndUnloadTodoContract.loadAndUnloadTodoView {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;

    List<AcceptTerminalTodoBean> listCache;

    private List<AcceptTerminalTodoBean> list;
    private List<AcceptTerminalTodoBean> listSearch;
    private int slidePosition, slidePositionChild, step;
    private DriverOutTaskAdapter adapter;
    private int currentPage = 1;

    private String searchString = "";//条件搜索关键字
    private TaskFragment mTaskFragment; //父容器fragment
    private SearchToolbar searchToolbar;//父容器的输入框
    private CustomToolbar mToolBar;//父容器标题
    private boolean isShow = false;


    private int max = 0, index = 0; //用于执行多个子任务的领受操作

    private String nowDoTaskId = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_driver_out, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvData.setRefreshListener(this);
        mMfrvData.setOnRetryLisenter(this);
        mTaskFragment = (TaskFragment) getParentFragment();
        searchToolbar = mTaskFragment.getSearchView();
        mToolBar = mTaskFragment.getToolbar();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        initData();
        setUserVisibleHint(true);
    }
    private void initData() {
        listCache = new ArrayList<>();
        listSearch = new ArrayList<>();
        list = new ArrayList<>();
        adapter = new DriverOutTaskAdapter(listSearch);
        mMfrvData.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            if (!nowDoTaskId.equals(list.get(position).getTaskId())) {
                nowDoTaskId = list.get(position).getTaskId();
//                setListStatus();
            }

        });
        adapter.setmOnStepListener(new DriverOutTaskAdapter.OnStepListener() {
            @Override
            public void onStepListener(int step1, int parentPosition, int position) {
                /**
                 * 同时开启多个航班
                 */
                max = list.get(parentPosition).getUseTasks().get(position).size();

                for (int i = 0; i < list.get(parentPosition).getUseTasks().get(position).size(); i++) {
                    slidePosition = parentPosition;
                    slidePositionChild = position;
                    step = step1;
                    submitStep(list.get(parentPosition).getUseTasks().get(position).get(i), step);
                }
            }

            @Override
            public void onFlightSafeguardClick(int parentPosition, int position) {
                IMUtils.chatToGroup(getActivity(),list.get(parentPosition).getUseTasks().get(position).get(0).getFlightId());
            }
        });
        getData();
    }

    private void getData() {
        mPresenter = new AcceptTerminalTodoPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setCurrent(currentPage);
        entity.setSize(Constants.PAGE_SIZE);
        entity.setUserId(UserInfoSingle.getInstance().getUserId());
        ((AcceptTerminalTodoPresenter) mPresenter).acceptTerminalTodo(entity);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isShow = isVisibleToUser;
        if (isVisibleToUser) {
            Log.e("111111", "setUserVisibleHint: " + "展示");
            if (mTaskFragment != null)
                mTaskFragment.setTitleText(list.size());
            if (mToolBar != null) {
//                mToolBar.setRightIconViewVisiable(false);
                    searchToolbar.setHintAndListener("请输入航班号", text -> {
                    searchString = text;
                    seachByText();
                });
            }

        }
    }

    /**
     * x循环执行 步骤任务。最后一条 再去拉去 列表
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
        entity.setLatitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLatitude());
        entity.setLongitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLongitude());

        if (step == 0)
            entity.setOperationCode(Constants.TP_START);//任务开始
        else if (step == 1)
            entity.setOperationCode(Constants.TP_END);//任务结束
        else
            entity.setOperationCode(Constants.TP_ACCEPT);//任务领受

        entity.setUserName(UserInfoSingle.getInstance().getUsername());

        entity.setTerminalId(DeviceInfoUtil.getDeviceInfo(getContext()).get("deviceId"));
        entity.setUserId(UserInfoSingle.getInstance().getUserId());
        entity.setCreateTime(System.currentTimeMillis());
        ((LoadAndUnloadTodoPresenter) mPresenter).slideTask(entity);

        nowDoTaskId = mOutFieldTaskBean.getTaskId();
    }

    @Override
    public void onRetry() {
        showProgessDialog("正在加载数据……");
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
     * 接收推送取消或者增加任务
     *
     * @param result
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {

        if (result.getTaskId() != null && (result.isCancelFlag() || result.isChangeWorkerUser())) {
            canCelTask(result.getTaskId());
        } else if (result.isTransportTaskAutoDone()){
            getData();
        }else {
            List<AcceptTerminalTodoBean> list = result.getTaskData();
            if (list != null)
                listCache.addAll(list);
            showTpNewTaskDialog();
        }

//        if (result.equals("TaskDriverOutFragment_refresh")) {
//
//            getData();
//        }
    }

    /**
     * 取消任务
     *
     * @param taskId
     */
    private void canCelTask(String taskId) {

        for (AcceptTerminalTodoBean mAcceptTerminalTodoBean : list) {
            if (taskId.equals(mAcceptTerminalTodoBean.getTaskId())) {
                list.remove(mAcceptTerminalTodoBean);
                break;
            }
        }
        adapter.notifyDataSetChanged();
        Log.e("TaskDriverOutFragment", "取消任务");

    }

    /**
     * 弹出领受任务dialog
     */
    private void showTpNewTaskDialog() {

        if (listCache.size() > 0) {
            TpPushDialog tpPushDialog = new TpPushDialog(getContext(), R.style.custom_dialog, listCache.get(0), OutFieldTaskBeans -> {

                max = OutFieldTaskBeans.size();
                for (int i = 0; i < OutFieldTaskBeans.size(); i++) {
                    submitStep(OutFieldTaskBeans.get(i), 2);
                }
                //检测 listCache 是否还有任务未领受
                showTpNewTaskDialog();
            });
            tpPushDialog.show();
            listCache.remove(0);
        }
    }

    /**
     * 拉取运输任务返回列表
     *
     * @param acceptTerminalTodoBeanList
     */
    @Override
    public void acceptTerminalTodoResult(List<AcceptTerminalTodoBean> acceptTerminalTodoBeanList) {
        if (acceptTerminalTodoBeanList != null) {

            if (currentPage == 1) {
                list.clear();
            } else {
                currentPage++;
            }
            mMfrvData.finishRefresh();
            mMfrvData.finishLoadMore();
            //把运输的板车类型 赋值大 子任务
            for (AcceptTerminalTodoBean mAcceptTerminalTodoBean : acceptTerminalTodoBeanList) {
                for (OutFieldTaskBean mOutFieldTaskBean : mAcceptTerminalTodoBean.getTasks()) {
                    mOutFieldTaskBean.setTransfortType(mAcceptTerminalTodoBean.getTransfortType());
                }
            }
            //根据BeginAreaId分类 21 以下 用 else
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                acceptTerminalTodoBeanList.forEach(acceptTerminalTodoBean -> {
                    acceptTerminalTodoBean.setUseTasks(new ArrayList<List<OutFieldTaskBean>>(acceptTerminalTodoBean.getTasks().stream().collect(Collectors.groupingBy(OutFieldTaskBean::getBeginAreaCargoType)).values()));
                });
            } else {
                for (AcceptTerminalTodoBean mAcceptTerminalTodoBean : acceptTerminalTodoBeanList) {
                    mAcceptTerminalTodoBean.setUseTasks(getUseTasks(mAcceptTerminalTodoBean));
                }
            }
            list.addAll(acceptTerminalTodoBeanList);
            seachByText();
            setListStatus();
            if (mTaskFragment != null) {
                if (isShow)
                    mTaskFragment.setTitleText(list.size());
            }

        } else {
            Log.e("失败", "外场运输待办");
        }
    }

    /**
     * 滚动到指定位置
     */
    private void setListStatus() {
        for (int i = 0; i < list.size(); i++) {
            if (nowDoTaskId.equals(list.get(i).getTaskId())) {

                mMfrvData.setVerticalScrollbarPosition(i);
                list.get(i).setExpand(true);
                adapter.notifyDataSetChanged();
            }
        }


    }

    /**
     * 根据搜索框输入检索对应的结果项
     */
    private void seachByText() {
        listSearch.clear();
        if (TextUtils.isEmpty(searchString)) {
            listSearch.addAll(list);
        } else {
            for (AcceptTerminalTodoBean item : list) {
                if (item.getTasks().get(0).getFlightNo().toLowerCase().contains(searchString.toLowerCase())) {
                    listSearch.add(item);
                }
            }
        }
        if (mMfrvData != null) {
            mMfrvData.notifyForAdapter(adapter);
        }
    }

    /**
     * 根据 开始位置和运输类型 组合列表
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
        if (currentPage == 1) {
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

    @Override
    public void loadAndUnloadTodoResult(List<LoadAndUnloadTodoBean> loadAndUnloadTodoBean) {

    }

    @Override
    public void slideTaskResult(String result) {

        if (index == max) {
            ToastUtil.showToast("任务执行成功");
            currentPage = 1; //刷新数据
            index = 0;
            getData();
        }
//        StepBean stepBean = JSON.parseObject(result,StepBean.class);
//        upDateStepStatus(stepBean);
    }

    @Override
    public void startClearTaskResult(String result) {

    }

    /**
     * 更新步骤状态
     *
     * @param stepBean
     */
    private void upDateStepStatus(StepBean stepBean) {
        for (OutFieldTaskBean taskBeans : list.get(slidePosition).getUseTasks().get(slidePositionChild)) {

            if (taskBeans.getFlightId().equals(stepBean.getData().getFlightId() + "")) {
                switch (step) {
                    case 0:
                        taskBeans.setAcceptTime(stepBean.getData().getCreateTime());
                        break;
                    case 1:
                        taskBeans.setTaskBeginTime(stepBean.getData().getCreateTime());
                        break;
                    case 2:
                        taskBeans.setTaskEndTime(stepBean.getData().getCreateTime());
                        break;
                }
            }

        }
        adapter.notifyDataSetChanged();

    }
}
