package qx.app.freight.qxappfreight.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ouyben.empty.EmptyLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.FlightListAdapter;
import qx.app.freight.qxappfreight.adapter.InPortDeliveryAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightLuggageBean;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.TransportListContract;
import qx.app.freight.qxappfreight.presenter.TransportListPresenter;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

public class FlightListBaggerFragment extends BaseFragment implements TransportListContract.transportListContractView , MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;

    FlightListAdapter mAdapter;
    List<FlightLuggageBean> mList;

    private int pageCurrent = 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_bagger, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
//        loadData();
    }
    private void initView() {

    }

    @Override
    public void onResume() {
        super.onResume();
        pageCurrent = 1;
        loadData();
    }

    private void loadData() {
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setCurrent(pageCurrent);
        entity.setSize(Constants.PAGE_SIZE);
        entity.setUndoType("3");
        entity.setStepOwner(UserInfoSingle.getInstance().getUserId());
        ((TransportListPresenter) mPresenter).transportListPresenter(entity);
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
    public void onRetry() {
        showProgessDialog("加载数据中……");
        new Handler().postDelayed(() -> {
            loadData();
            dismissProgessDialog();
        }, 2000);
    }
    @Override
    public void transportListContractResult(List<TransportListBean> transportListBeans) {

    }

    @Override
    public void toastView(String error) {

    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }


}
