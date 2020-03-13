package qx.app.freight.qxappfreight.fragment;

import android.content.Intent;
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
import android.widget.CompoundButton;
import android.widget.RadioButton;

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
import qx.app.freight.qxappfreight.activity.InportDeliveryDetailActivity;
import qx.app.freight.qxappfreight.adapter.DeliveryDetailIndexAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FilterTransportDateBase;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;
import qx.app.freight.qxappfreight.contract.GroupBoardToDoContract;
import qx.app.freight.qxappfreight.contract.TaskLockContract;
import qx.app.freight.qxappfreight.presenter.GroupBoardToDoPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/**
 * 进港-交货页面
 * Created by swd
 */
public class InPortDeliveryFragment extends BaseFragment implements GroupBoardToDoContract.GroupBoardToDoView, TaskLockContract.taskLockView, MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;

    @BindView(R.id.rg_todo)
    RadioButton rgTodo;
    @BindView(R.id.rg_done)
    RadioButton rgDone;

    DeliveryDetailIndexAdapter mAdapter;
    List <WaybillsBean> mList; //adapter 绑定的条件list
    List <WaybillsBean> list1; //原始list

    private int pageCurrent = 1;

    private String searchString; //条件搜索关键字

    private TaskFragment mTaskFragment; //父容器fragment
    private SearchToolbar searchToolbar;//父容器的输入框
    private boolean isShow = false;


    /**
     * 待办锁定 当前的任务bean
     */
    private TransportDataBase CURRENT_TASK_BEAN = null;

    private boolean isScan = false;

    private int waybillStatus = 5;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inport_delivery, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mTaskFragment = (TaskFragment) getParentFragment();
        searchToolbar = mTaskFragment.getSearchView();
        initView();
        setUserVisibleHint(true);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isShow = isVisibleToUser;
        if (isVisibleToUser) {
            Log.e("111111", "setUserVisibleHint: " + "展示");
            if (mTaskFragment != null) {
                mTaskFragment.setTitleText(list1.size());
            }
            if (searchToolbar != null) {
                searchToolbar.setHintAndListener("请输入运单", text -> {
                    searchString = text;
                    loadData(searchString);
                });
            }
        }
    }

    private void seachWithNum() {
        mList.clear();
        if (TextUtils.isEmpty(searchString)) {
            mList.addAll(list1);
        } else {
            for (WaybillsBean item : list1) {
                if (item.getFlightNo() != null && item.getWaybillCode().toLowerCase().contains(searchString.toLowerCase())) {
                    mList.add(item);
                }
            }
        }
        if (mMfrvData != null) {
            mMfrvData.notifyForAdapter(mAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        pageCurrent = 1;
        loadData(searchString);
    }

    private void initView() {
        rgTodo.setChecked(true);
        rgTodo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    waybillStatus = 5;
                    loadData(searchString);

                }

            }
        });
        rgDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    waybillStatus = 6;
                    loadData(searchString);
                }
            }
        });
//        mPresenter = new TransportListPresenter(this);
        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvData.setRefreshListener(this);
        mMfrvData.setOnRetryLisenter(this);
        mList = new ArrayList <>();
        list1 = new ArrayList <>();
        mAdapter = new DeliveryDetailIndexAdapter(mList);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {

            turnToDetailActivity(mList.get(position), waybillStatus);
//            CURRENT_TASK_BEAN = mList.get(position);
//            mPresenter = new TaskLockPresenter(this);
//            TaskLockEntity entity = new TaskLockEntity();
//            List<String> taskIdList = new ArrayList<>();
//            taskIdList.add(mList.get(position).getTaskId());
//            entity.setTaskId(taskIdList);
//            entity.setUserId(UserInfoSingle.getInstance().getUserId());
//            entity.setRoleCode(Constants.INPORTDELIVERY);
//
//            ((TaskLockPresenter) mPresenter).taskLock(entity);

        });
        mMfrvData.setAdapter(mAdapter);
    }

    private void loadData(String waybillCode) {
//        if (rgTodo.isChecked())
//            waybillStatus = 5;
//        else
//            waybillStatus = 6;
        mPresenter = new GroupBoardToDoPresenter(this);
//        GroupBoardRequestEntity entity = new GroupBoardRequestEntity();
//
//        entity.setStepOwner(UserInfoSingle.getInstance().getUserId());
//
//        entity.setRoleCode("delivery_in");
//
//        entity.setUndoType(3);
//
//        List <String> ascs = new ArrayList <>();
//        ascs.add("flight_number");
//        ascs.add("lastUpdate_time");
//
//        entity.setAscs(ascs);
        BaseFilterEntity <WaybillsBean> entity = new BaseFilterEntity();
        entity.setSize(20);
        entity.setCurrent(pageCurrent);
        WaybillsBean waybillsBean = new WaybillsBean();
        waybillsBean.setWaybillCode(waybillCode);
        waybillsBean.setWaybillStatus(waybillStatus);
        entity.setFilter(waybillsBean);
        ((GroupBoardToDoPresenter) mPresenter).searchWaybillByWaybillCode(entity);


    }

    /**
     * 跳转到代办详情
     *
     * @param bean
     */
    private void turnToDetailActivity(WaybillsBean bean, int waybillStatus) {

        startActivity(new Intent(getContext(), InportDeliveryDetailActivity.class)
//                .putExtra("num1", bean.getOutboundNumber())
//                .putExtra("num2", bean.getWaybillCount())
//                .putExtra("taskId", bean.getTaskId())
//                .putExtra("billId", bean.getSerialNumber())
                .putExtra("waybillStatus", waybillStatus)
                .putExtra("DATA", bean));
    }

    /**
     * 激光扫码回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        String daibanCode = result.getData();
        if (!TextUtils.isEmpty(result.getData()) && result.getFunctionFlag().equals("MainActivity")) {
            String[] parts = daibanCode.split("\\/");
            List <String> strsToList = Arrays.asList(parts);
            if (strsToList.size() >= 4) {
                chooseCode(strsToList.get(3));
            }
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(WebSocketResultBean mWebSocketResultBean) {
//        if ("N".equals(mWebSocketResultBean.getFlag())) {
//            if ("DA_outbound".equals(mWebSocketResultBean.getChgData().get(0).getTaskTypeCode())) {
//                list1.addAll(mWebSocketResultBean.getChgData());
//                mTaskFragment.setTitleText(list1.size());
//            }
//        } else if ("D".equals(mWebSocketResultBean.getFlag())) {
//
//            for (TransportDataBase mTransportListBean : mList) {
//                if (mWebSocketResultBean.getChgData().get(0).getId().equals(mTransportListBean.getId())) {
//                    list1.remove(mTransportListBean);
//                }
//            }
//        }
//        seachWithNum();
//    }


    /**
     * 通过获取的code，筛选代办，直接进入处理代办
     *
     * @param daibanCode 代办号
     */
    private void chooseCode(String daibanCode) {
        isScan = true;
        loadData(daibanCode);

//        for (WaybillsBean item : list1) {
//            if (daibanCode.equals(item.getWaybillCode())) {
//                turnToDetailActivity(item, waybillStatus);
////                CURRENT_TASK_BEAN = item;
////                mPresenter = new TaskLockPresenter(this);
////                TaskLockEntity entity = new TaskLockEntity();
////                List<String> taskIdList = new ArrayList<>();
////                taskIdList.add(item.getTaskId());
////                entity.setTaskId(taskIdList);
////                entity.setUserId(UserInfoSingle.getInstance().getUserId());
////                entity.setRoleCode(Constants.INPORTDELIVERY);
////                ((TaskLockPresenter) mPresenter).taskLock(entity);
//
//                return;
//            }
//        }
//        ToastUtil.showToast("该运单号无效");
    }

    @Override
    public void onRetry() {
        showProgessDialog("加载数据中……");
        new Handler().postDelayed(() -> {
            pageCurrent = 1;
            loadData(searchString);
            dismissProgessDialog();
        }, 2000);
    }

    @Override
    public void onRefresh() {
        pageCurrent = 1;
        loadData(searchString);
    }

    @Override
    public void onLoadMore() {
        pageCurrent++;
        loadData(searchString);
    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(getActivity(), error);
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
    public void getGroupBoardToDoResult(FilterTransportDateBase transportListBeans) {
//        //没有分页
//        list1.clear();
//        if (pageCurrent == 1) {
//
//            mMfrvData.finishRefresh();
//        } else {
//            mMfrvData.finishLoadMore();
//        }
//        if (transportListBeans != null && transportListBeans.size() > 0) {
//            list1.addAll(transportListBeans);
//        }
//        seachWithNum();
//        if (mTaskFragment != null) {
//            if (isShow) {
//                mTaskFragment.setTitleText(list1.size());
//            }
//        }
    }

    @Override
    public void getScooterByScooterCodeResult(List <GetInfosByFlightIdBean> getInfosByFlightIdBean) {

    }

    @Override
    public void searchWaybillByWaybillCodeResult(List <WaybillsBean> waybillsBeans) {

        if (pageCurrent == 1) {
            list1.clear();
            mMfrvData.finishRefresh();
        } else {
            mMfrvData.finishLoadMore();
        }
        if (waybillsBeans != null && waybillsBeans.size() > 0) {
            if (waybillsBeans.size() == 1 && isScan) {
                turnToDetailActivity(waybillsBeans.get(0), waybillStatus);
                return;
            }
            list1.addAll(waybillsBeans);
        }
        seachWithNum();
        if (mTaskFragment != null) {
            if (isShow) {
                if (waybillStatus == 5)
                    mTaskFragment.setTitleText( "我的待办（" + list1.size() + "）");
                else {
                    mTaskFragment.setTitleText( "我的已办（" + list1.size() + "）");

                }
            }
        }
    }

    /**
     * 待办锁定
     *
     * @param result
     */
    @Override
    public void taskLockResult(String result) {
//        if(CURRENT_TASK_BEAN != null) {
//            turnToDetailActivity(CURRENT_TASK_BEAN);
//        }
    }
}
