package qx.app.freight.qxappfreight.fragment;

import android.os.Bundle;
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
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.VerifyStaffActivity;
import qx.app.freight.qxappfreight.adapter.MainListRvAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.TaskLockEntity;
import qx.app.freight.qxappfreight.bean.response.DeclareWaybillBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.bean.response.WebSocketResultBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.GetWayBillInfoByIdContract;
import qx.app.freight.qxappfreight.contract.SearchTodoTaskContract;
import qx.app.freight.qxappfreight.contract.TaskLockContract;
import qx.app.freight.qxappfreight.presenter.GetWayBillInfoByIdPresenter;
import qx.app.freight.qxappfreight.presenter.SearchTodoTaskPresenter;
import qx.app.freight.qxappfreight.presenter.TaskLockPresenter;
import qx.app.freight.qxappfreight.utils.ActManager;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/**
 * 出港-收验
 */
public class TaskCollectVerifyFragment extends BaseFragment implements SearchTodoTaskContract.searchTodoTaskView, TaskLockContract.taskLockView, MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter, GetWayBillInfoByIdContract.getWayBillInfoByIdView {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;
    private MainListRvAdapter adapter;

    private int pageCurrent = 1;

    private List<TransportDataBase> transportListList1 = new ArrayList<>();
    private List<TransportDataBase> transportListList = new ArrayList<>();
    private String seachString = "";//条件搜索关键字
    private TaskFragment mTaskFragment; //父容器fragment
    private SearchToolbar searchToolbar;//父容器的输入框

    private TransportDataBase mBean;
    private boolean isShow = false;

    /**
     * 待办锁定 当前的任务bean
     */
    private TransportDataBase CURRENT_TASK_BEAN = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_stowage, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTaskFragment = (TaskFragment) getParentFragment();
        searchToolbar = mTaskFragment.getSearchView();
        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvData.setRefreshListener(this);
        mMfrvData.setOnRetryLisenter(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
        initTitle();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isShow = isVisibleToUser;
        if (isVisibleToUser) {
            Log.e("111111", "setUserVisibleHint: " + "展示");
            initTitle();
        }
    }

    private void initTitle(){
            if (mTaskFragment == null){
                mTaskFragment = (TaskFragment) getParentFragment();

            }
            if (mTaskFragment != null) {
                mTaskFragment.setTitleText(transportListList1.size());
                searchToolbar = mTaskFragment.getSearchView();
            }
            if (searchToolbar != null) {
                searchToolbar.setHintAndListener("请输入运单号", text -> {
                    seachString = text;
                    seachWith();
                });
            }
    }

    public void seachWith() {
        transportListList.clear();
        if (TextUtils.isEmpty(seachString)) {
            transportListList.addAll(transportListList1);
        } else {
            for (TransportDataBase team : transportListList1) {
                if (team.getWaybillCode().toLowerCase().contains(seachString.toLowerCase())) {
                    transportListList.add(team);
                }
            }
        }
        if (mMfrvData != null) {
            mMfrvData.notifyForAdapter(adapter);
        }

    }

    private void initData() {
        adapter = new MainListRvAdapter(transportListList);
        mMfrvData.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            CURRENT_TASK_BEAN = transportListList.get(position);
            mPresenter = new TaskLockPresenter(this);
            TaskLockEntity entity = new TaskLockEntity();
            List<String> taskIdList = new ArrayList<>();
            taskIdList.add(transportListList.get(position).getTaskId());
            entity.setTaskId(taskIdList);
            entity.setUserId(UserInfoSingle.getInstance().getUserId());
            entity.setRoleCode(Constants.RECEIVE);
            ((TaskLockPresenter) mPresenter).taskLock(entity);
        });
        getData();
    }

    //获取数据
    private void getData() {
        mPresenter = new SearchTodoTaskPresenter(this);
        BaseFilterEntity<TransportDataBase> entity = new BaseFilterEntity();
        TransportDataBase tempBean = new TransportDataBase();
        tempBean.setWaybillCode("");
        tempBean.setTaskStartTime("");
        tempBean.setTaskEndTime("");
        tempBean.setRole(Constants.RECEIVE);
//        for (LoginResponseBean.RoleRSBean mRoleRSBean : UserInfoSingle.getInstance().getRoleRS()) {
//            if (Constants.RECEIVE.equals(mRoleRSBean.getRoleCode())) {
//                tempBean.setRole(mRoleRSBean.getRoleCode());
//            }
//        }
        entity.setFilter(tempBean);
        entity.setCurrentStep("");
        entity.setSize(Constants.PAGE_SIZE);
        entity.setCurrent(pageCurrent);
        ((SearchTodoTaskPresenter) mPresenter).searchTodoTask(entity);

    }

    //根据id获取运单信息
    public void getTaskInfo(TransportDataBase bean) {
        mBean = bean;
        mPresenter = new GetWayBillInfoByIdPresenter(this);
        ((GetWayBillInfoByIdPresenter) mPresenter).getWayBillInfoById(bean.getWaybillId());
    }

    /**
     * 激光扫码回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        String daibanCode = result.getData();
        if (!TextUtils.isEmpty(result.getData()) && result.getFunctionFlag().equals("MainActivity")&&isShow) {
                String[] parts = daibanCode.split("\\/");
                List<String> strsToList = Arrays.asList(parts);
                if (strsToList.size() >= 4) {
                    chooseCode(strsToList.get(3));
                }
        }
    }

    /**
     * 通过获取的code，筛选代办，直接进入处理代办
     *
     * @param daibanCode 代办号
     */
    private void chooseCode(String daibanCode) {
        for (TransportDataBase item : transportListList1) {
            if (daibanCode.equals(item.getWaybillCode())) {
                CURRENT_TASK_BEAN = item;
                mPresenter = new TaskLockPresenter(this);
                TaskLockEntity entity = new TaskLockEntity();
                List<String> taskIdList = new ArrayList<>();
                taskIdList.add(item.getTaskId());
                entity.setTaskId(taskIdList);
                entity.setUserId(UserInfoSingle.getInstance().getUserId());
                entity.setRoleCode(Constants.RECEIVE);
                ((TaskLockPresenter) mPresenter).taskLock(entity);
                return;
            }
        }
    }

    @Override
    public void onRetry() {
        pageCurrent = 1;
        getData();
    }

    @Override
    public void onRefresh() {
        pageCurrent = 1;
        getData();
    }

    @Override
    public void onLoadMore() {
        pageCurrent++;
        getData();
    }

    //刷新列表数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String refresh) {
        if (refresh.equals("collectVerify_refresh")) {
            Log.e("refresh", refresh);
            pageCurrent = 1;
            initData();
        }
    }

    //收验接受到推送过来的数据，添加或者删除数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(WebSocketResultBean mWebSocketResultBean) {
        if ("N".equals(mWebSocketResultBean.getFlag())) {
            if ("reReceive".equals(mWebSocketResultBean.getChgData().get(0).getTaskTypeCode())
                    || "receive".equals(mWebSocketResultBean.getChgData().get(0).getTaskTypeCode())
                    ||"borrowReceive".equals(mWebSocketResultBean.getChgData().get(0).getTaskTypeCode())) {
                transportListList1.addAll(mWebSocketResultBean.getChgData());
                if (isShow) {
                    mTaskFragment.setTitleText(transportListList1.size());
                }
            }
            seachWith();
        } else if ("D".equals(mWebSocketResultBean.getFlag())) {
            if (null != CURRENT_TASK_BEAN) {
                if (CURRENT_TASK_BEAN.getWaybillId().equals(mWebSocketResultBean.getChgData().get(0).getWaybillId())) {
                    ActManager.getAppManager().finishReceive();
                    ToastUtil.showToast("当前收验任务已完成");
                }
            }
            if ("reReceive".equals(mWebSocketResultBean.getChgData().get(0).getTaskTypeCode())
                    || "receive".equals(mWebSocketResultBean.getChgData().get(0).getTaskTypeCode())
                    ||"borrowReceive".equals(mWebSocketResultBean.getChgData().get(0).getTaskTypeCode())) {
                getData();
            }
        }

    }


    @Override
    public void toastView(String error) {
        ToastUtil.showToast(getActivity(), "数据为空");
        if (pageCurrent == 1) {
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
    public void searchTodoTaskResult(TransportListBean transportListBean) {
        if (transportListBean != null) {
            if (pageCurrent == 1) {
                transportListList1.clear();
                mMfrvData.finishRefresh();
            } else {
                mMfrvData.finishLoadMore();
            }
            transportListList1.addAll(transportListBean.getRecords());
            if (mTaskFragment != null) {
                if (isShow) {
                    mTaskFragment.setTitleText(transportListList1.size());
                }
            }
            seachWith();
        } else {
            ToastUtil.showToast(getActivity(), "数据为空");
        }
    }

    @Override
    public void getWayBillInfoByIdResult(DeclareWaybillBean result) {
        if (null != result) {
            if (null != mBean) {
                VerifyStaffActivity.startActivity(getActivity(), mBean, result);
            }
        } else {
            ToastUtil.showToast("收验点击事件为空");
        }
    }

    @Override
    public void sendPrintMessageResult(String result) {

    }

    /**
     * 待办锁定
     *
     * @param result
     */
    @Override
    public void taskLockResult(String result) {
        if (CURRENT_TASK_BEAN != null) {
            getTaskInfo(CURRENT_TASK_BEAN);
        }
    }
}
