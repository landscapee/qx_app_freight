package qx.app.freight.qxappfreight.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.DynamicDetailsAcitvity;
import qx.app.freight.qxappfreight.adapter.DynamicInfoAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightBean;
import qx.app.freight.qxappfreight.bean.response.FlightEventBusBean;
import qx.app.freight.qxappfreight.contract.FlightdynamicContract;
import qx.app.freight.qxappfreight.presenter.FlightdynamicPresenter;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.utils.doubleClickUtil.ClickFilter;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

public class DynamicInfoFragment extends BaseFragment implements FlightdynamicContract.flightdynamicView {
    @BindView(R.id.rl_dynamic)
    RecyclerView rlDynamic;
    @BindView(R.id.search_toolbar)
    SearchToolbar searchToolbar;//父容器的输入框

    private String type;
    private String movement;
    private String day;
    private DynamicInfoAdapter mAdapter;
    private List<FlightBean.FlightsBean> mList;
    private List<FlightBean.FlightsBean> mListSearch = new ArrayList <>();

    private String strSearch="";

    public static DynamicInfoFragment getInstance(String type, String movement, String day) {
        DynamicInfoFragment fragment = new DynamicInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("movement", movement);
        bundle.putString("day", day);
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



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.e("chage", "123");
        if (mPresenter != null) {
            if (isVisibleToUser) {
                initData();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void initView() {
        EventBus.getDefault().register(this);
        searchToolbar.setHintAndListener("请输入航班号", text -> {
            strSearch = text;
            seachByText(strSearch);
        });

        type = getArguments().getString("type");
        movement = getArguments().getString("movement");
        day = getArguments().getString("day");
        mPresenter = new FlightdynamicPresenter(this);

        initData();
        Log.e("info", type + "");
        Log.e("movement", movement + "");
        Log.e("day", day + "");

        mList = new ArrayList<>();
        rlDynamic.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new DynamicInfoAdapter(mListSearch, type);
        rlDynamic.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) ->
                {
                    if (!Tools.isFastClick())
                        return;
                    DynamicDetailsAcitvity.startActivity(getActivity(), mList.get(position).getFlightId(), mList.get(position).getFlightNo());
                }
                );
    }

    private void seachByText(String str) {
        mListSearch.clear();
        for (FlightBean.FlightsBean flightsBean:mList){
            if (flightsBean.getFlightNo().contains(str)){
                mListSearch.add(flightsBean);
            }
        }
        mAdapter.notifyDataSetChanged();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FlightEventBusBean result) {
        String flight = result.getFlight();
        Log.e("航班刷新", flight);
       if (!TextUtils.isEmpty(day)&&day.equals(flight))
           initData();
    }

    public void initData() {

        BaseFilterEntity entity = new BaseFilterEntity();
        if ("today".equals(day))
            entity.setDay("today");
        else if ("yesterday".equals(day))
            entity.setDay("yesterday");
        else if ("tomorrow".equals(day))
            entity.setDay("tomorrow");
        ((FlightdynamicPresenter) mPresenter).flightdynamic(entity);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void flightdynamicResult(FlightBean result) {
        mList.clear();
        if (null != result) {
            if (TextUtils.isEmpty(type) && TextUtils.isEmpty(movement)) {
                mList.addAll(result.getFlights());
            } else if (TextUtils.isEmpty(type)) {
                //进港
                if ("A".equals(movement)) {
                    for (FlightBean.FlightsBean item : result.getFlights()) {
                        if ("A".equals(item.getMovement())) {
                            mList.add(item);
                        }
                    }
                } else {
                    //离港
                    for (FlightBean.FlightsBean item : result.getFlights()) {
                        if ("D".equals(item.getMovement())) {
                            mList.add(item);
                        }
                    }
                }
            } else {
                //备降
                if ("1".equals(type)) {
                    for (FlightBean.FlightsBean item : result.getFlights()) {
                        if ("1".equals(item.getMovement())) {
                            mList.add(item);
                        }
                    }
                } else {
                    //返航
                    for (FlightBean.FlightsBean item : result.getFlights()) {
                        if ("2".equals(item.getMovement())) {
                            mList.add(item);
                        }
                    }
                }
            }
            seachByText(strSearch);
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
