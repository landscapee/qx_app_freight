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
import qx.app.freight.qxappfreight.adapter.InPortDeliveryAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.GroupBoardRequestEntity;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.bean.response.WebSocketResultBean;
import qx.app.freight.qxappfreight.contract.GroupBoardToDoContract;
import qx.app.freight.qxappfreight.presenter.GroupBoardToDoPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/**
 * 进港-交货页面
 * Created by swd
 */
//TransportListContract.transportListContractView ,
public class InPortDeliveryFragment extends BaseFragment implements GroupBoardToDoContract.GroupBoardToDoView, MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;

    InPortDeliveryAdapter mAdapter;
    List<TransportDataBase> mList; //adapter 绑定的条件list
    List<TransportDataBase> list1; //原始list

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
        EventBus.getDefault().register(this);
        SearchToolbar searchToolbar = ((TaskFragment) getParentFragment()).getSearchView();
        searchToolbar.setHintAndListener("请输入流水号", text -> {
            searchString = text;
            seachWithNum();
        });
        initView();
    }

    private void seachWithNum() {
        mList.clear();
        if (TextUtils.isEmpty(searchString)) {
            mList.addAll(list1);
        } else {
            for (TransportDataBase item : list1) {
                if (item.getSerialNumber().toLowerCase().contains(searchString.toLowerCase())) {
                    mList.add(item);
                }
            }
        }
        mMfrvData.notifyForAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        pageCurrent = 1;
        loadData();
    }

    private void initView() {
//        mPresenter = new TransportListPresenter(this);
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
        /**
         *
         BaseFilterEntity<TransportDataBase> entity = new BaseFilterEntity();

         TransportDataBase tempBean = new TransportDataBase();
         tempBean.setWaybillCode("");
         tempBean.setTaskStartTime("");
         tempBean.setTaskEndTime("");
         tempBean.setRole(UserInfoSingle.getInstance().getRoleRS().get(0).getRoleCode());
         entity.setFilter(tempBean);

         entity.setCurrent(pageCurrent);

         entity.setSize(Constants.PAGE_SIZE);

         entity.setUndoType("3");

         entity.setStepOwner(UserInfoSingle.getInstance().getUserId());

         entity.setRoleCode(UserInfoSingle.getInstance().getRoleRS().get(0).getRoleCode());

         ((TransportListPresenter) mPresenter).transportListPresenter(entity);
         */
        mPresenter = new GroupBoardToDoPresenter(this);
        GroupBoardRequestEntity entity = new GroupBoardRequestEntity();

        entity.setStepOwner(UserInfoSingle.getInstance().getUserId());

        entity.setRoleCode("delivery_in");

        entity.setUndoType(3);

        List<String> ascs = new ArrayList<>();
        ascs.add("flight_number");
        ascs.add("lastUpdate_time");

        entity.setAscs(ascs);

        ((GroupBoardToDoPresenter) mPresenter).getGroupBoardToDo(entity);


    }

    /**
     * 跳转到代办详情
     *
     * @param bean
     */
    private void turnToDetailActivity(TransportDataBase bean) {

        startActivity(new Intent(getContext(), InportDeliveryDetailActivity.class)
//                .putExtra("num1", bean.getOutboundNumber())
//                .putExtra("num2", bean.getWaybillCount())
//                .putExtra("taskId", bean.getTaskId())
//                .putExtra("billId", bean.getSerialNumber())
                .putExtra("DATA",bean));
    }

    /**
     * 激光扫码回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        String daibanCode = result.getData();
        Log.e("22222", "提货代办流水号： " + daibanCode);
        //   /QR/1a7d0a00541bed0e06a935a998efe038/201905241162/QR/
        if (!TextUtils.isEmpty(daibanCode)) {
            String[] parts = daibanCode.split("\\/");
            List<String> strsToList= Arrays.asList(parts);
            if (strsToList.size()>=4){
                chooseCode(strsToList.get(3));
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(WebSocketResultBean mWebSocketResultBean) {
        if ("N".equals(mWebSocketResultBean.getFlag())) {

            list1.addAll(mWebSocketResultBean.getChgData());
        } else if ("D".equals(mWebSocketResultBean.getFlag())) {

            for (TransportDataBase mTransportListBean : mList) {
                if (mWebSocketResultBean.getChgData().get(0).getId().equals(mTransportListBean.getId())) {
                    list1.remove(mTransportListBean);
                }
            }
        }
        seachWithNum();
    }


    /**
     * 通过获取的code，筛选代办，直接进入处理代办
     *
     * @param daibanCode 代办号
     */
    private void chooseCode(String daibanCode) {
        for (TransportDataBase item : list1) {
            if (daibanCode.equals(item.getSerialNumber())) {
                turnToDetailActivity(item);
                return;
            }
        }
        ToastUtil.showToast("该流水号无效");
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

    /** -- guohao
    @Override
    public void transportListContractResult(TransportListBean transportListBeans) {
        if (transportListBeans != null) {
            TaskFragment fragment = (TaskFragment) getParentFragment();

            if (pageCurrent == 1) {
                list1.clear();
                mMfrvData.finishRefresh();
            }
            else{
                mMfrvData.finishLoadMore();
            }
            list1.addAll(transportListBeans.getRecords());
            seachWithNum();
            if (fragment != null) {
                fragment.setTitleText(transportListBeans.getRecords().size());
            }
        } else {
            ToastUtil.showToast(getActivity(), "数据为空");
        }
    }
     */

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(getActivity(), error);
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
    public void getGroupBoardToDoResult(List<TransportDataBase> transportListBeans) {
        if (transportListBeans != null&&transportListBeans.size()>0) {
            TaskFragment fragment = (TaskFragment) getParentFragment();

            if (pageCurrent == 1) {
                list1.clear();
                mMfrvData.finishRefresh();
            } else {
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
}
