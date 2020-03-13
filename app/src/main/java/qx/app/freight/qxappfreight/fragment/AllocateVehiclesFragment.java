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
import android.widget.TextView;

import com.ouyben.empty.EmptyLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.AllocaaateHistoryActivity;
import qx.app.freight.qxappfreight.activity.AllocaaateScanActivity;
import qx.app.freight.qxappfreight.activity.AllocateScooterActivity;
import qx.app.freight.qxappfreight.adapter.AllocateVehiclesAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.GroupBoardRequestEntity;
import qx.app.freight.qxappfreight.bean.response.FilterTransportDateBase;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;
import qx.app.freight.qxappfreight.bean.response.WebSocketResultBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.GroupBoardToDoContract;
import qx.app.freight.qxappfreight.dialog.ChooseFlightDialog;
import qx.app.freight.qxappfreight.presenter.GroupBoardToDoPresenter;
import qx.app.freight.qxappfreight.utils.ActManager;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/**
 * 出港-配载-复重页面
 * Created by pr
 */
public class AllocateVehiclesFragment extends BaseFragment implements GroupBoardToDoContract.GroupBoardToDoView, EmptyLayout.OnRetryLisenter, MultiFunctionRecylerView.OnRefreshListener {
    @BindView(R.id.mfrv_allocate_list)
    MultiFunctionRecylerView mMfrvAllocateList;
    @BindView(R.id.tv_history)
    TextView tvHistory;

    private AllocateVehiclesAdapter adapter;

    private List <TransportDataBase> list; //条件list
    private List <TransportDataBase> list1; //原始list

    private int pageCurrent = 1;
    private String searchString = "";//条件搜索关键字
    private TaskFragment mTaskFragment; //父容器fragment
    private SearchToolbar searchToolbar;//父容器的输入框
    private boolean isShow = false;
    private TransportDataBase CURRENT_TASK_BEAN = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_allocate_vehicles, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    //    @Override
//    public void onResume() {
//        super.onResume();
//        getData();
//    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        tvHistory.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AllocaaateHistoryActivity.class));
        });
        mMfrvAllocateList.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvAllocateList.setOnRetryLisenter(this);
        mTaskFragment = (TaskFragment) getParentFragment();
        searchToolbar = mTaskFragment.getSearchView();
        initData();

        initTitle();
//        setUserVisibleHint(true);
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

    private void initTitle() {
        if (!isShow)
            return;
        if (mTaskFragment == null) {
            mTaskFragment = (TaskFragment) getParentFragment();

        }
        if (mTaskFragment != null) {
            mTaskFragment.setTitleText(list1.size());
            searchToolbar = mTaskFragment.getSearchView();
            mTaskFragment.getToolbar().setRightIconViewVisiable(true);
            mTaskFragment.getToolbar().setleftIconViewVisiable(true);
            mTaskFragment.setTitleText();
        }
        if (searchToolbar != null) {
            searchToolbar.setHintAndListener("请输入航班号", text -> {
                searchString = text;
                seachWithNum();
            });
        }

    }

    //根据条件筛选数据
    private void seachWithNum() {
        list.clear();
        if (TextUtils.isEmpty(searchString)) {
            list.addAll(list1);
        } else {
            for (TransportDataBase item : list1) {
                if (item.getFlightNo() != null && item.getFlightNo().toLowerCase().contains(searchString.toLowerCase())) {
                    list.add(item);
                }
            }
        }
        if (mMfrvAllocateList != null) {
            mMfrvAllocateList.notifyForAdapter(adapter);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(WebSocketResultBean mWebSocketResultBean) {
//        if ("N".equals(mWebSocketResultBean.getFlag())) {
//            if (null != mWebSocketResultBean.getChgData().get(0).getTaskTypeCode() && mWebSocketResultBean.getChgData().get(0).getTaskTypeCode().contains("checkWeight")) {
//                list1.addAll(mWebSocketResultBean.getChgData());
//                seachWithNum();
//                if (isShow) {
//                    mTaskFragment.setTitleText(list1.size());
//                }
//            }
//        } else if ("D".equals(mWebSocketResultBean.getFlag())) {
//            if (null != CURRENT_TASK_BEAN) {
//                if (CURRENT_TASK_BEAN.getFlightId().equals(mWebSocketResultBean.getChgData().get(0).getFlightId())) {
//                    ActManager.getAppManager().finishAllocate();
//                    ToastUtil.showToast("任务已完成");
//                }
//            }
//            if (null != mWebSocketResultBean.getChgData().get(0).getTaskTypeCode() && mWebSocketResultBean.getChgData().get(0).getTaskTypeCode().contains("checkWeight")) {
//                getData();
//            }
//
//        }
        pageCurrent = 1;
        getData();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String postStr){
        if (Constants.REWEIGHT_DONE.equals(postStr)){
            pageCurrent = 1;
            getData();
        }
    }

    private void initData() {
        list = new ArrayList <>();
        list1 = new ArrayList <>();
        adapter = new AllocateVehiclesAdapter(list, getContext());
        mMfrvAllocateList.setRefreshListener(this);
        mMfrvAllocateList.setOnRetryLisenter(this);
        mMfrvAllocateList.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            CURRENT_TASK_BEAN = list.get(position);
            startActivity(new Intent(getActivity(), AllocateScooterActivity.class)
                    .putExtra("flightInfoId", list.get(position).getFlightInfoId())
                    .putExtra("taskId", list.get(position).getTaskId()));
        });
//        mPresenter = new GetInfosByFlightIdPresenter(this);
        getData();
    }

    /**
     * 获取列表数据
     */
    public void getData() {
        mPresenter = new GroupBoardToDoPresenter(this);

        BaseFilterEntity baseFilterEntity = new BaseFilterEntity();
        GroupBoardRequestEntity entity = new GroupBoardRequestEntity();
        entity.setStepOwner(UserInfoSingle.getInstance().getUserId());
//        {"stepOwner":"uef9de97d6c53428c946089d63cfaaa4c","undoType":2,"roleCode":"weighter","ascs":["ETD"]}
        entity.setRoleCode(Constants.WEIGHTER);
        entity.setUndoType(2);
        List <String> ascs = new ArrayList <>();
        ascs.add("ETD");
        entity.setAscs(ascs);
        baseFilterEntity.setFilter(entity);
        baseFilterEntity.setCurrent(pageCurrent);
        baseFilterEntity.setSize(Constants.PAGE_SIZE);
        ((GroupBoardToDoPresenter) mPresenter).getGroupBoardToDo(baseFilterEntity);
//
//        BaseFilterEntity<GetInfosByFlightIdBean> entity = new BaseFilterEntity();
//        entity.setUserId("weighter");
//        entity.setRoleCode(Constants.WEIGHTER);
//        ((GetInfosByFlightIdPresenter) mPresenter).getInfosByFlightId(entity);
    }

    /**
     * 根据板车号获取板车信息
     */
    public void getScooterByScooterCode(String scooterCode) {
        mPresenter = new GroupBoardToDoPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setScooterCode(scooterCode);
        ((GroupBoardToDoPresenter) mPresenter).getScooterByScooterCode(entity);
    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(getActivity(), error);
        if (pageCurrent == 1) {
            mMfrvAllocateList.finishRefresh();
        } else {
            mMfrvAllocateList.finishLoadMore();
        }
    }

    @Override
    public void showNetDialog() {
    }

    @Override
    public void dissMiss() {
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
        pageCurrent = 1;
        getData();
    }

    @Override
    public void onLoadMore() {
        getData();
    }

    /**
     * 激光扫码回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if (!TextUtils.isEmpty(result.getData()) && result.getFunctionFlag().equals("MainActivity") && isShow) {
            String daibanCode = result.getData();
            getScooterByScooterCode(daibanCode);
        }

    }

    @Override
    public void getGroupBoardToDoResult(FilterTransportDateBase transportListBeans) {
        if (transportListBeans!=null&&transportListBeans.getRecords()!=null){
            //因为没有分页，不做分页判断

            if (transportListBeans.getCurrent() == 1) {
                list1.clear();
                mMfrvAllocateList.finishRefresh();
            } else {
                mMfrvAllocateList.finishLoadMore();
            }
            pageCurrent = transportListBeans.getCurrent()+1;
            list1.addAll(transportListBeans.getRecords());
            if (mTaskFragment != null) {
                if (isShow)
                    mTaskFragment.setTitleText(list1.size());
            }
            seachWithNum();
        }
        else {
            ToastUtil.showToast("无更多数据");
        }

    }

    @Override
    public void getScooterByScooterCodeResult(List <GetInfosByFlightIdBean> getInfosByFlightIdBeans) {
        if (getInfosByFlightIdBeans != null && getInfosByFlightIdBeans.size() > 0) {
            if (getInfosByFlightIdBeans.size() == 1) {
                startActivity(new Intent(getActivity(), AllocaaateScanActivity.class).putExtra("dataBean", getInfosByFlightIdBeans.get(0)));
            } else {
                ChooseFlightDialog dialog = new ChooseFlightDialog();
                dialog.setChooseDialogInterface(position -> {
                    if (getInfosByFlightIdBeans.get(position) != null) {
                        startActivity(new Intent(getActivity(), AllocaaateScanActivity.class).putExtra("dataBean", getInfosByFlightIdBeans.get(position)));
                    }
                });
                dialog.setData(getInfosByFlightIdBeans, getActivity());
                dialog.show(getActivity().getSupportFragmentManager(), "123");
            }

        } else {
            ToastUtil.showToast("没有查询到相应的板车");
        }

    }

    @Override
    public void searchWaybillByWaybillCodeResult(List <WaybillsBean> waybillsBeans) {

    }
}
