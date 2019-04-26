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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ouyben.empty.EmptyLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.CollectorDeclareActivity;
import qx.app.freight.qxappfreight.activity.DeliveryVerifyActivity;
import qx.app.freight.qxappfreight.activity.ReceiveGoodsActivity;
import qx.app.freight.qxappfreight.activity.ReturnGoodsActivity;
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

/****
 * 收运
 */
public class CollectorFragment extends BaseFragment implements TransportListContract.transportListContractView, MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;
    private MainListRvAdapter adapter;
    private List<TransportListBean> list1;
    private List<TransportListBean> list;
    private int pageCurrent = 1;
    private String seachString = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collector, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvData.setRefreshListener(this);
        mMfrvData.setOnRetryLisenter(this);
//        String test=null;
//        Log.e("test","test============="+test.length());
        initView();
        SearchToolbar searchToolbar = ((TaskFragment) getParentFragment()).getSearchView();
        searchToolbar.setHintAndListener("请输入运单号", text -> {
            seachString = text;
            seachWith();
        });
        loadData();
    }

    private void seachWith() {
        list.clear();
        if (TextUtils.isEmpty(seachString)) {
            list.addAll(list1);
        } else {
            for (TransportListBean team: list1){
                if (team.getWaybillCode().toLowerCase().contains(seachString.toLowerCase())){
                    list.add(team);
                }
            }
        }
        mMfrvData.notifyForAdapter(adapter);
    }


    private void initView() {
        EventBus.getDefault().register(this);
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        adapter = new MainListRvAdapter(list);
        mMfrvData.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            trunToCollectorActivity(list.get(position));

        });
    }

    private void trunToCollectorActivity(TransportListBean bean){
        switch (bean.getTaskTypeCode()){
            case "changeApply": //换单审核
                DeliveryVerifyActivity.startActivity(getContext(),bean.getId(), bean.getTaskId());
                break;
            case "collection"://出港收货

                startActivity(new Intent(getContext(), CollectorDeclareActivity.class)
                        .putExtra("wayBillId",bean.getId())
                        .putExtra("taskId",bean.getTaskId()));
                break;
            case "RR_collectReturn"://出港退货
                ReturnGoodsActivity.startActivity(getActivity(),bean);
                break;
        }
    }


    private void loadData() {
        mPresenter = new TransportListPresenter(this);
        BaseFilterEntity<TransportListBean> entity = new BaseFilterEntity();
        entity.setCurrent(pageCurrent);
        entity.setSize(Constants.PAGE_SIZE);
        entity.setStepOwner(UserInfoSingle.getInstance().getUserId());
        entity.setRoleCode(UserInfoSingle.getInstance().getRoleRS().get(0).getRoleCode());
        ((TransportListPresenter) mPresenter).transportListPresenter(entity);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String result) {
        if (result.equals("collector_refresh")) {
            pageCurrent = 1;
            Log.e("refresh", result);
            loadData();
        }
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

            for (TransportListBean mTransportListBean:list1){
                if (mWebSocketResultBean.getChgData().get(0).getId().equals(mTransportListBean.getId()))
                    list1.remove(mTransportListBean);
            }
        }
        seachWith();
    }

    /**
     * 通过获取的code，筛选代办，直接进入处理代办
     * @param daibanCode  代办号
     */
    private void chooseCode(String daibanCode){
        for (TransportListBean item:list) {
            if (daibanCode.equals(item.getId())){
                trunToCollectorActivity(item);
                return;
            }
        }
    }

    @Override
    public void transportListContractResult(List<TransportListBean> transportListBeans) {
        if (transportListBeans != null) {
            TaskFragment fragment = (TaskFragment) getParentFragment();
            //未分页
            list1.clear();
            if (pageCurrent == 1) {
//                list.clear();
                mMfrvData.finishRefresh();
            }
            else{

                mMfrvData.finishLoadMore();
            }
            list1.addAll(transportListBeans);
            seachWith();
//            mMfrvData.notifyForAdapter(adapter);
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
