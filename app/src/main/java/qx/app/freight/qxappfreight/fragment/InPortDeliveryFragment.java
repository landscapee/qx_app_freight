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
import com.ouyben.empty.EmptyLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.InportDeliveryDetailActivity;
import qx.app.freight.qxappfreight.activity.ReceiveGoodsActivity;
import qx.app.freight.qxappfreight.adapter.InPortDeliveryAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.bean.response.WebSocketResultBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.TransportListContract;
import qx.app.freight.qxappfreight.presenter.TransportListPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/**
 * 进港-交货页面
 * Created by swd
 */
public class InPortDeliveryFragment extends BaseFragment implements TransportListContract.transportListContractView , MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter{
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;

    InPortDeliveryAdapter mAdapter;
    List<TransportListBean> mList; //adapter 绑定的条件list
    List<TransportListBean> list1; //原始list

    private int pageCurrent = 1;

    private String searchString; //条件搜索关键字

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
        SearchToolbar searchToolbar=((TaskFragment)getParentFragment()).getSearchView();
        searchToolbar.setHintAndListener("请输入流水号", text -> {
            searchString = text;
            seachWithNum();
        });
        initView();
    }

    private void seachWithNum() {
        mList.clear();
        if (TextUtils.isEmpty(searchString)){
            mList.addAll(list1);
        }else {
            for (TransportListBean item:list1) {
                if (item.getSerialNumber().toLowerCase().contains(searchString.toLowerCase())){
                    mList.add(item);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        pageCurrent = 1;
        loadData();
    }

    private void initView() {
        mPresenter = new TransportListPresenter(this);
        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvData.setRefreshListener(this);
        mMfrvData.setOnRetryLisenter(this);
        mList = new ArrayList<>();
        list1 = new ArrayList<>();
        mAdapter = new InPortDeliveryAdapter(mList);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            turnToDetailActivity(mList.get(position));

        });
        mMfrvData.setAdapter(mAdapter);
    }

    private void loadData() {
        BaseFilterEntity<TransportListBean> entity = new BaseFilterEntity();
        entity.setCurrent(pageCurrent);
        entity.setSize(Constants.PAGE_SIZE);
        entity.setUndoType("3");
        entity.setStepOwner(UserInfoSingle.getInstance().getUserId());
        entity.setRoleCode(UserInfoSingle.getInstance().getRoleRS().get(0).getRoleCode());
        ((TransportListPresenter) mPresenter).transportListPresenter(entity);
    }

    /**
     * 跳转到代办详情
     * @param bean
     */
    private void turnToDetailActivity(TransportListBean bean){

        startActivity(new Intent(getContext(), InportDeliveryDetailActivity.class)
                .putExtra("num1" ,bean.getOutboundNumber())
                .putExtra("num2" ,bean.getWaybillCount())
                .putExtra("taskId", bean.getTaskId())
                .putExtra("billId", bean.getSerialNumber()));
    }

    /**
     * 激光扫码回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        String daibanCode = result.getData();
        Log.e("22222","daibanCode"+daibanCode);
        if (!TextUtils.isEmpty(daibanCode)) {
            chooseCode(daibanCode);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(WebSocketResultBean mWebSocketResultBean) {
        if ("N".equals(mWebSocketResultBean.getFlag())) {

            list1.addAll(mWebSocketResultBean.getChgData());
        }
        else if ("D".equals(mWebSocketResultBean.getFlag())){

            for (TransportListBean mTransportListBean:mList){
                if (mWebSocketResultBean.getChgData().get(0).getId().equals(mTransportListBean.getId()))
                    list1.remove(mTransportListBean);
            }
        }
        mMfrvData.notifyForAdapter(mAdapter);
    }




    /**
     * 通过获取的code，筛选代办，直接进入处理代办
     * @param daibanCode  代办号
     */
    private void chooseCode(String daibanCode){
        for (TransportListBean item:list1) {
            if (daibanCode.equals(item.getId())){
                turnToDetailActivity(item);
                return;
            }
        }
    }

    @Override
    public void onRetry() {
        showProgessDialog("加载数据中……");
        new Handler().postDelayed(() -> {
            loadData();
            dismissProgessDialog();
        }, 2000);
    }

    @Override
    public void onRefresh() {
        pageCurrent = 1;
        loadData();
    }

    @Override
    public void onLoadMore() {
        pageCurrent++;
        loadData();
    }

    @Override
    public void transportListContractResult(List<TransportListBean> transportListBeans) {
        if (transportListBeans != null) {
            TaskFragment fragment = (TaskFragment) getParentFragment();

            if (pageCurrent == 1) {
                list1.clear();
                mMfrvData.finishRefresh();
            }
            else{
                mMfrvData.finishLoadMore();
            }
            list1.addAll(transportListBeans);
            seachWithNum();
            if (fragment != null) {
                fragment.setTitleText(transportListBeans.size());
            }
        } else {
            ToastUtil.showToast(getActivity(), "数据为空");
        }
    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(getActivity(), error);
        if (pageCurrent == 1) {
            mMfrvData.finishRefresh();
        }
        else
            mMfrvData.finishLoadMore();
    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }



}
