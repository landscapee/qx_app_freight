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
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.bean.response.WebSocketResultBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.TransportListContract;
import qx.app.freight.qxappfreight.presenter.TransportListPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/**
 * 出港-收验
 */
public class TaskCollectVerifyFragment extends BaseFragment implements TransportListContract.transportListContractView, MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;
    private MainListRvAdapter adapter;

    private int pageCurrent = 1;

    private List<TransportListBean.TransportDataBean> transportListList1;
    private List<TransportListBean.TransportDataBean> transportListList;
    private String seachString = "";
    TaskFragment fragment;
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
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

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
            for (TransportListBean.TransportDataBean team : transportListList1) {
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
        mPresenter = new TransportListPresenter(this);
        adapter = new MainListRvAdapter(transportListList);
        mMfrvData.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            turnToDetailActivity(transportListList.get(position));
        });
        getData();
    }

    private void getData() {
        BaseFilterEntity<TransportListBean> entity = new BaseFilterEntity();
        entity.setCurrent(pageCurrent);
        entity.setSize(Constants.PAGE_SIZE);
        entity.setStepOwner(UserInfoSingle.getInstance().getUserId());
        entity.setRoleCode(UserInfoSingle.getInstance().getRoleRS().get(0).getRoleCode());
        ((TransportListPresenter) mPresenter).transportListPresenter(entity);

    }

    /**
     * 跳转到代办详情
     *
     * @param bean
     */
    private void turnToDetailActivity(TransportListBean.TransportDataBean bean) {
        if (null == bean.getDeclareWaybillAddition()) {
            ToastUtil.showToast("数据为空请重新申报");
        } else {
            VerifyStaffActivity.startActivity(getActivity()
                    , bean.getDeclareWaybillAddition()
                    , bean.getTaskId()
                    , bean.getSpotFlag()
                    , bean.getFlightNumber()
                    , bean.getShipperCompanyId());
        }
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
        for (TransportListBean.TransportDataBean item : transportListList) {
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


    @Override
    public void transportListContractResult(TransportListBean transportListBeans) {
        if (transportListBeans != null) {
            //未分页
            transportListList1.clear();
            if (pageCurrent == 1) {
//                transportListList.clear();
                mMfrvData.finishRefresh();
            } else {
                mMfrvData.finishLoadMore();
            }
            transportListList1.addAll(transportListBeans.getRecords());

            seachWith();
//            mMfrvData.notifyForAdapter(adapter);
        } else {
            ToastUtil.showToast(getActivity(), "数据为空");
        }
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
            for (TransportListBean.TransportDataBean mTransportListBean : transportListList1) {
                if (mWebSocketResultBean.getChgData().get(0).getId().equals(mTransportListBean.getId()))
                    transportListList1.remove(mTransportListBean);
            }
        }
        seachWith();
    }


    @Override
    public void toastView(String error) {
        ToastUtil.showToast(getActivity(), "数据为空");
        if (pageCurrent == 1)
            mMfrvData.finishRefresh();
        else {
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
