package qx.app.freight.qxappfreight.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
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
import qx.app.freight.qxappfreight.activity.InportDeliveryDetailActivity;
import qx.app.freight.qxappfreight.adapter.TaskStowageAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.bean.response.WebSocketResultBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.TransportListContract;
import qx.app.freight.qxappfreight.presenter.TransportListPresenter;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/**
 * 理货
 * <p>
 * 出港-配载-组板
 */
public class TaskStowageFragment extends BaseFragment implements TransportListContract.transportListContractView, MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;

    private List<TransportListBean> list;
    private TaskStowageAdapter adapter;

    private int pageCurrent = 1;//页数

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
        initData();
    }

    private void initData() {
        list = new ArrayList<>();
        adapter = new TaskStowageAdapter(list);
        mMfrvData.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
//            ToastUtil.showToast(getContext(), list.get(position));
            turnToDetailActivity(list.get(position));
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
        mPresenter = new TransportListPresenter(this);
        BaseFilterEntity<TransportListBean> entity = new BaseFilterEntity();
        entity.setCurrent(pageCurrent);
        entity.setSize(Constants.PAGE_SIZE);
        entity.setStepOwner(UserInfoSingle.getInstance().getUserId());
        entity.setRoleCode(UserInfoSingle.getInstance().getRoleRS().get(0).getRoleCode());
        entity.setUndoType("2");
        ((TransportListPresenter) mPresenter).transportListPresenter(entity);
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
    private void turnToDetailActivity(TransportListBean bean) {
        CargoHandlingActivity.startActivity(mContext,bean.getTaskId()
                ,bean.getFlightId());
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(WebSocketResultBean mWebSocketResultBean) {
        if ("N".equals(mWebSocketResultBean.getFlag())) {

            list.addAll(mWebSocketResultBean.getChgData());
        }
        else if ("D".equals(mWebSocketResultBean.getFlag())){

            for (TransportListBean mTransportListBean:list){
                if (mWebSocketResultBean.getChgData().get(0).getId().equals(mTransportListBean.getId()))
                    list.remove(mTransportListBean);
            }
        }
        mMfrvData.notifyForAdapter(adapter);
    }

    /**
     * 通过获取的code，筛选代办，直接进入处理代办
     *
     * @param daibanCode 代办号
     */
    private void chooseCode(String daibanCode) {
        for (TransportListBean item : list) {
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
    public void transportListContractResult(List<TransportListBean> transportListBeans) {

        if (transportListBeans != null) {
            //未分页
            list.clear();
            if (pageCurrent == 1) {
//                list.clear();
                mMfrvData.finishRefresh();
            } else {
                mMfrvData.finishLoadMore();
            }

            for (TransportListBean mTransportListBean : transportListBeans) {

                if (Constants.INSTALLSCOOTER.equals(mTransportListBean.getTaskTypeCode()))
                    list.add(mTransportListBean);
            }
        }
        adapter.notifyDataSetChanged();
        setTitleNum(list.size());
    }

    @Override
    public void toastView(String error) {
        if (pageCurrent == 1) {
            list.clear();
            mMfrvData.finishRefresh();
        } else
            mMfrvData.finishLoadMore();
    }

    @Override
    public void showNetDialog() {
//        showProgessDialog("加载中……");
    }

    @Override
    public void dissMiss() {
//        dismissProgessDialog();
    }
}
