package qx.app.freight.qxappfreight.fragment;

import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
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
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.TransportListContract;
import qx.app.freight.qxappfreight.presenter.TransportListPresenter;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/**
 *
 * 理货
 *
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
        list = new ArrayList <>();
        adapter = new TaskStowageAdapter(list);
        mMfrvData.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
//            ToastUtil.showToast(getContext(), list.get(position));
            CargoHandlingActivity.startActivity(mContext,list.get(position).getTaskId(),list.get(position).getFlightId());
        });
        getData();
    }

    private void setTitleNum(int size){
        TaskFragment fragment = (TaskFragment) getParentFragment();
        if (fragment != null) {
            fragment.setTitleText(size);
        }
    }

    private void getData(){
        mPresenter = new TransportListPresenter(this);
        BaseFilterEntity<TransportListBean> entity = new BaseFilterEntity();
        entity.setCurrent(pageCurrent);
        entity.setSize(Constants.PAGE_SIZE);
        entity.setStepOwner(UserInfoSingle.getInstance().getUserId());
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
    public void transportListContractResult(List <TransportListBean> transportListBeans) {

        if (transportListBeans != null){
            //未分页
            list.clear();
            if (pageCurrent == 1){
//                list.clear();
                mMfrvData.finishRefresh();
            }
            else {
                mMfrvData.finishLoadMore();
            }

            for (TransportListBean mTransportListBean : transportListBeans){

                if (Constants.INSTALLSCOOTER.equals(mTransportListBean.getTaskTypeCode()))
                    list.add(mTransportListBean);
            }
        }
        adapter.notifyDataSetChanged();
        setTitleNum(list.size());
    }

    @Override
    public void toastView(String error) {
        if (pageCurrent == 1){
            list.clear();
            mMfrvData.finishRefresh();
        }
        else
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
