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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.CargoHandlingActivity;
import qx.app.freight.qxappfreight.adapter.TaskStowageAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.GroupBoardRequestEntity;
import qx.app.freight.qxappfreight.bean.request.TaskLockEntity;
import qx.app.freight.qxappfreight.bean.response.FilterTransportDateBase;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;
import qx.app.freight.qxappfreight.bean.response.WebSocketResultBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.GroupBoardToDoContract;
import qx.app.freight.qxappfreight.contract.TaskLockContract;
import qx.app.freight.qxappfreight.presenter.GroupBoardToDoPresenter;
import qx.app.freight.qxappfreight.presenter.TaskLockPresenter;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/**
 * ===================================================已弃用！！！！！
 * 理货
 * <p>
 * 出港-配载-组板
 */
public class TaskStowageFragment extends BaseFragment implements GroupBoardToDoContract.GroupBoardToDoView, TaskLockContract.taskLockView, MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;

    private List<TransportDataBase> list = new ArrayList<>();
    private List<TransportDataBase> mCacheList = new ArrayList<>();
    private TaskStowageAdapter adapter;

    private int pageCurrent = 1;//页数
    private String mSearchText;

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
        setUserVisibleHint(true);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvData.setRefreshListener(this);
        mMfrvData.setOnRetryLisenter(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
        SearchToolbar searchToolbar = ((TaskFragment) getParentFragment()).getSearchView();
        searchToolbar.setHintAndListener("请输入航班号", text -> {
            mSearchText = text;
            seachByText();
        });
    }

    private void seachByText() {
        list.clear();
        if (TextUtils.isEmpty(mSearchText)) {
            list.addAll(mCacheList);
        } else {
            for (TransportDataBase item : mCacheList) {
                if (item.getFlightNo()!=null&&item.getFlightNo().toLowerCase().contains(mSearchText.toLowerCase())) {
                    list.add(item);
                }
            }
        }
        mMfrvData.notifyForAdapter(adapter);
//        adapter.notifyDataSetChanged();
    }

    private void initData() {
        adapter = new TaskStowageAdapter(list);
        mMfrvData.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {

            CURRENT_TASK_BEAN = list.get(position);
            mPresenter = new TaskLockPresenter(this);
            TaskLockEntity entity = new TaskLockEntity();
            List<String> taskIdList = new ArrayList<>();
            taskIdList.add(list.get(position).getTaskId());
            entity.setTaskId(taskIdList);
            entity.setUserId(UserInfoSingle.getInstance().getUserId());
            entity.setRoleCode(Constants.BEFOREHAND);

            ((TaskLockPresenter) mPresenter).taskLock(entity);

        });
        getData();
    }

    private void setTitleNum(int size) {
        TaskFragment fragment = (TaskFragment) getParentFragment();
        if (fragment != null) {
            fragment.setTitleText(size);
        }
    }

    private void getData() {
        mPresenter = new GroupBoardToDoPresenter(this);
//        BaseFilterEntity entity = new BaseFilterEntity();
//        PrematchingEntity mBean = new PrematchingEntity();
//        entity.setCurrent(pageCurrent);
//        entity.setSize(Constants.PAGE_SIZE);
//        mBean.setTaskType("installScooter");//组板
//        mBean.setFlightNo("");
//        mBean.setTaskStartTime("");
//        mBean.setTaskEndTime("");
//        mBean.setTaskHandler(UserInfoSingle.getInstance().getUserId());
//        entity.setFilter(mBean);
//        if (UserInfoSingle.getInstance() != null && UserInfoSingle.getInstance().getRoleRS() != null && UserInfoSingle.getInstance().getRoleRS().size() > 0) {
//            entity.setStepOwner(UserInfoSingle.getInstance().getUserId());
//            entity.setRoleCode(UserInfoSingle.getInstance().getRoleRS().get(0).getRoleCode());
//        }
//        entity.setUndoType("2");

        GroupBoardRequestEntity entity = new GroupBoardRequestEntity();
        entity.setStepOwner(UserInfoSingle.getInstance().getUserId());
//        {"stepOwner":"u27f95c83a0d24f19a592d16ebdf28fe3","undoType":2,"roleCode":"preplaner","ascs":["ETD"]}
        entity.setRoleCode(Constants.PREPLANER);
        entity.setUndoType(2);
        List<String> ascs = new ArrayList<>();
        ascs.add("ETD");
        entity.setAscs(ascs);
//        ((GroupBoardToDoPresenter) mPresenter).getGroupBoardToDo(entity);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String result) {
        if (result.equals("CargoHandlingActivity_refresh")) {
            pageCurrent = 1;
            getData();
        }
    }

    /**
     * 跳转到代办详情
     *
     * @param bean
     */
    private void turnToDetailActivity(TransportDataBase bean) {
        CargoHandlingActivity.startActivity(mContext, bean.getTaskId(), bean.getFlightId(), bean.getTaskTypeCode());
    }

    /**
     * 激光扫码回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        String daibanCode = result.getData();
        Log.e("22222", "daibanCode" + daibanCode);
        if (!TextUtils.isEmpty(result.getData()) && result.getFunctionFlag().equals("MainActivity")) {
            chooseCode(daibanCode);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(WebSocketResultBean mWebSocketResultBean) {
        if ("N".equals(mWebSocketResultBean.getFlag())) {
            mCacheList.addAll(mWebSocketResultBean.getChgData());
        } else if ("D".equals(mWebSocketResultBean.getFlag())) {
            for (TransportDataBase mTransportListBean : list) {
                if (mWebSocketResultBean.getChgData().get(0).getTaskId().equals(mTransportListBean.getTaskId())) {
                    mCacheList.remove(mTransportListBean);
                }
            }
        }
        seachByText();
    }

    /**
     * 通过获取的code，筛选代办，直接进入处理代办
     *
     * @param daibanCode 代办号
     */
    private void chooseCode(String daibanCode) {
        for (TransportDataBase item : mCacheList) {
            if (daibanCode.equals(item.getId())) {

                CURRENT_TASK_BEAN = item;
                mPresenter = new TaskLockPresenter(this);
                TaskLockEntity entity = new TaskLockEntity();
                List<String> taskIdList = new ArrayList<>();
                taskIdList.add(item.getTaskId());
                entity.setTaskId(taskIdList);
                entity.setUserId(UserInfoSingle.getInstance().getUserId());
                entity.setRoleCode(Constants.BEFOREHAND);

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

    @Override
    public void toastView(String error) {
        if (pageCurrent == 1) {
            mCacheList.clear();
        }
        if (mMfrvData != null)
            mMfrvData.finishLoadMore();
        if (mMfrvData != null)
            mMfrvData.finishRefresh();
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("加载中……");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    @Override
    public void getGroupBoardToDoResult(FilterTransportDateBase transportListBeans) {
        if (transportListBeans != null) {
            //未分页

            if (pageCurrent == 1) {
                mCacheList.clear();
//                list.clear();
                mMfrvData.finishRefresh();
            } else {
                mMfrvData.finishLoadMore();
            }
            for (TransportDataBase mTransportListBean : transportListBeans.getRecords()) {
                if (Constants.INSTALLSCOOTER.equals(mTransportListBean.getTaskTypeCode())) {
                    mCacheList.add(mTransportListBean);
                }
            }
        }
        seachByText();
        setTitleNum(mCacheList.size());
    }

    @Override
    public void getOverWeightToDoResult(FilterTransportDateBase transportListBeans) {

    }

    @Override
    public void getScooterByScooterCodeResult(List<GetInfosByFlightIdBean> getInfosByFlightIdBean) {

    }

    @Override
    public void searchWaybillByWaybillCodeResult(List <WaybillsBean> waybillsBeans) {

    }

    /**
     * 待办锁定 - 回调
     *
     * @param result
     */
    @Override
    public void taskLockResult(String result) {
        if (CURRENT_TASK_BEAN != null) {
            turnToDetailActivity(CURRENT_TASK_BEAN);
        }
    }
}
