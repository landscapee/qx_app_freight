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
import qx.app.freight.qxappfreight.activity.VerifyStaffActivity;
import qx.app.freight.qxappfreight.adapter.MainListRvAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.DeclareWaybillBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.bean.response.WebSocketResultBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.GetWayBillInfoByIdContract;
import qx.app.freight.qxappfreight.contract.SearchTodoTaskContract;
import qx.app.freight.qxappfreight.presenter.GetWayBillInfoByIdPresenter;
import qx.app.freight.qxappfreight.presenter.SearchTodoTaskPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/**
 * 出港-收验
 */
public class TaskCollectVerifyFragment extends BaseFragment implements SearchTodoTaskContract.searchTodoTaskView, MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter, GetWayBillInfoByIdContract.getWayBillInfoByIdView {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;
    private MainListRvAdapter adapter;

    private int pageCurrent = 1;

    private List<TransportDataBase> transportListList1;
    private List<TransportDataBase> transportListList;
    private String seachString = "";
    TaskFragment fragment;

    private TransportDataBase mBean;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_stowage, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
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

        SearchToolbar searchToolbar = ((TaskFragment) getParentFragment()).getSearchView();
        searchToolbar.setHintAndListener("请输入运单号", text -> {
            seachString = text;
            seachWith();
        });
        fragment = (TaskFragment) getParentFragment();
        initData();
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
        if (fragment != null) {
            fragment.setTitleText(transportListList1.size());
        }
        mMfrvData.notifyForAdapter(adapter);
    }

    private void initData() {
        transportListList = new ArrayList<>();
        transportListList1 = new ArrayList<>();
        mPresenter = new SearchTodoTaskPresenter(this);
        adapter = new MainListRvAdapter(transportListList);
        mMfrvData.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            getTaskInfo(transportListList.get(position));
        });
        getData();
    }

    private void getData() {
        BaseFilterEntity<TransportListBean.TransportDataBean> entity = new BaseFilterEntity();
        TransportListBean.TransportDataBean tempBean = new TransportListBean.TransportDataBean();
        tempBean.setWaybillCode("");
        tempBean.setTaskStartTime("");
        tempBean.setTaskEndTime("");
        tempBean.setRole(UserInfoSingle.getInstance().getRoleRS().get(0).getRoleCode());
        entity.setFilter(tempBean);
        entity.setCurrentStep("");
        entity.setSize(Constants.PAGE_SIZE);
        entity.setCurrent(1);
        ((SearchTodoTaskPresenter) mPresenter).searchTodoTask(entity);

    }

    /**
     * 跳转到代办详情
     *
     * @param bean
     */
    public void turnToDetailActivity(TransportDataBase bean) {

    }


    //根据id获取运单信息
    public void getTaskInfo(TransportDataBase bean) {
        mBean = bean;
        mPresenter = new GetWayBillInfoByIdPresenter(this);
        ((GetWayBillInfoByIdPresenter) mPresenter).getWayBillInfoById(mBean.getId());
    }

    /**
     * 激光扫码回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        String daibanCode = result.getData();
        Log.e("22222", "daibanCode" + daibanCode);
        if (!TextUtils.isEmpty(daibanCode)) {
            chooseCode(daibanCode);
        }
    }

    /**
     * 通过获取的code，筛选代办，直接进入处理代办
     *
     * @param daibanCode 代办号
     */
    private void chooseCode(String daibanCode) {
        for (TransportDataBase item : transportListList) {
            if (daibanCode.equals(item.getId())) {
                turnToDetailActivity(item);
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String refresh) {
        if (refresh.equals("collectVerify_refresh")) {
            Log.e("refresh", refresh);
            initData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(WebSocketResultBean mWebSocketResultBean) {
        if ("N".equals(mWebSocketResultBean.getFlag())) {
            transportListList1.addAll(mWebSocketResultBean.getChgData());
        } else if ("D".equals(mWebSocketResultBean.getFlag())) {
            for (TransportDataBase mTransportListBean : transportListList1) {
                if (mWebSocketResultBean.getChgData().get(0).getId().equals(mTransportListBean.getId())) {
                    transportListList1.remove(mTransportListBean);
                }
            }
        }
        seachWith();
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
            //未分页
            transportListList1.clear();
            if (pageCurrent == 1) {
//                transportListList.clear();
                mMfrvData.finishRefresh();
            } else {
                mMfrvData.finishLoadMore();
            }
            transportListList1.addAll(transportListBean.getRecords());

            seachWith();
//            mMfrvData.notifyForAdapter(adapter);
        } else {
            ToastUtil.showToast(getActivity(), "数据为空");
        }
    }

    @Override
    public void getWayBillInfoByIdResult(DeclareWaybillBean result) {
        if (null != result) {
            if (null != mBean) {
                VerifyStaffActivity.startActivity(getActivity()
                        , result.getDeclareWaybillAddition().getId()
                        , result.getAdditionTypeArr()
                        , result.getDeclareWaybillAddition().getWaybillId()
                        , mBean.getTaskId()
                        , result.getSpotFlag()
                        , result.getFlightNumber()
                        , result.getShipperCompanyId());
            }
        }
    }

    @Override
    public void sendPrintMessageResult(String result) {

    }
}
