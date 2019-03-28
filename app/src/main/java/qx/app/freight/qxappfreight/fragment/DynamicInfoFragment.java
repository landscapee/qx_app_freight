package qx.app.freight.qxappfreight.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.DynamicInfoAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightBean;
import qx.app.freight.qxappfreight.contract.FlightdynamicContract;
import qx.app.freight.qxappfreight.presenter.FlightdynamicPresenter;

public class DynamicInfoFragment extends BaseFragment implements FlightdynamicContract.flightdynamicView {
    @BindView(R.id.rl_dynamic)
    RecyclerView rlDynamic;

    private String info;
    private DynamicInfoAdapter mAdapter;
    private List<FlightBean.FlightsBean> mList;


    public static DynamicInfoFragment getInstance(String tag) {
        DynamicInfoFragment fragment = new DynamicInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("info", tag);
        fragment.setArguments(bundle);

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamicinfo, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mPresenter = new FlightdynamicPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setDay("today");
        ((FlightdynamicPresenter) mPresenter).flightdynamic(entity);

        info = getArguments().getString("info");
        Log.e("info", info);

        mList = new ArrayList<>();
        rlDynamic.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new DynamicInfoAdapter(mList, 1);
        rlDynamic.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void flightdynamicResult(FlightBean result) {
        if (null != result) {
            mList.addAll(result.getFlights());
            mAdapter.notifyDataSetChanged();
        }

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
