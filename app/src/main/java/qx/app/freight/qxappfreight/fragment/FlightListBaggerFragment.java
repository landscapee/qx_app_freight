package qx.app.freight.qxappfreight.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ouyben.empty.EmptyLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.BaggageListActivity;
import qx.app.freight.qxappfreight.adapter.FlightListAdapter;
import qx.app.freight.qxappfreight.adapter.InPortDeliveryAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightLuggageBean;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.LookLUggageScannigFlightContract;
import qx.app.freight.qxappfreight.contract.TransportListContract;
import qx.app.freight.qxappfreight.presenter.LookLUggageScannigFlightPresenter;
import qx.app.freight.qxappfreight.presenter.TransportListPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

public class FlightListBaggerFragment extends BaseFragment implements LookLUggageScannigFlightContract.lookLUggageScannigFlightView, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_data)
    RecyclerView mMfrvData;

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
        mPresenter = new LookLUggageScannigFlightPresenter(this);
        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mList = new ArrayList<>();
        mAdapter = new FlightListAdapter(mList);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            startActivity(new Intent(getContext(), BaggageListActivity.class).putExtra("flightBean",mList.get(position)));
        });
        mMfrvData.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setMinutes("120");
        ((LookLUggageScannigFlightPresenter) mPresenter).getDepartureFlightByAndroid(entity);
    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(error);
        Log.e("22222", "toastView: "+error);
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    @Override
    public void onRetry() {
        showProgessDialog("正在加载数据。。。。。。");
        new Handler().postDelayed(() -> {
            loadData();
            dismissProgessDialog();
        }, 2000);
    }


    @Override
    public void lookLUggageScannigFlightResult(String result) {

    }

    @Override
    public void getDepartureFlightByAndroidResult(List<FlightLuggageBean> flightLuggageBeans) {
        mList.clear();
        mList.addAll(flightLuggageBeans);
        mAdapter.notifyDataSetChanged();
    }
}
