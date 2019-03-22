package qx.app.freight.qxappfreight.fragment;

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
import qx.app.freight.qxappfreight.activity.ReceiveGoodsActivity;
import qx.app.freight.qxappfreight.adapter.MainListRvAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.TransportListContract;
import qx.app.freight.qxappfreight.presenter.TransportListPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/****
 * 收运
 */
public class CollectorFragment extends BaseFragment implements TransportListContract.transportListContractView, MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;
    private MainListRvAdapter adapter;
    private List<TransportListBean> list;
    private int pageCurrent = 1;

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
        initView();
        loadData();
    }


    private void initView() {
        EventBus.getDefault().register(this);
        list = new ArrayList<>();
        adapter = new MainListRvAdapter(list);
        mMfrvData.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            turnToReceiveGoodsActivity(list.get(position));
        });
    }

    /**
     * 跳转到代办详情
     * @param bean
     */
    private void turnToReceiveGoodsActivity(TransportListBean bean){
        ReceiveGoodsActivity.startActivity(getActivity(),
                bean.getId(),
                bean.getTaskId(),
                bean.getWaybillCode(),
                bean.getDeclareItem(),
                bean.getColdStorage()
        );
    }

    private void loadData() {
        mPresenter = new TransportListPresenter(this);
        BaseFilterEntity<TransportListBean> entity = new BaseFilterEntity();
        entity.setCurrent(pageCurrent);
        entity.setSize(Constants.PAGE_SIZE);
        entity.setStepOwner(UserInfoSingle.getInstance().getUserId());
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

    /**
     * 通过获取的code，筛选代办，直接进入处理代办
     * @param daibanCode  代办号
     */
    private void chooseCode(String daibanCode){
        for (TransportListBean item:list) {
            if (daibanCode.equals(item.getId())){
                turnToReceiveGoodsActivity(item);
                return;
            }
        }
    }

    @Override
    public void transportListContractResult(List<TransportListBean> transportListBeans) {
        if (transportListBeans != null) {
            TaskFragment fragment = (TaskFragment) getParentFragment();

            //未分页
            list.clear();
            if (pageCurrent == 1) {
//                list.clear();
                mMfrvData.finishRefresh();
            }
            else{

                mMfrvData.finishLoadMore();
            }
            list.addAll(transportListBeans);
            mMfrvData.notifyForAdapter(adapter);
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
