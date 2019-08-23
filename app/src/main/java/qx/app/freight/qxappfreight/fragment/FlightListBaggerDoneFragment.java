package qx.app.freight.qxappfreight.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ouyben.empty.EmptyLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.BaggageDoneListActivity;
import qx.app.freight.qxappfreight.activity.BaggageListActivity;
import qx.app.freight.qxappfreight.adapter.FlightListDoneAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.response.CargoReportHisBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.contract.BaggageSubHisContract;
import qx.app.freight.qxappfreight.presenter.BaggageSubHisPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;


/******
 * 行李上报已办页面***/
public class FlightListBaggerDoneFragment extends BaseFragment implements BaggageSubHisContract.baggageSubHisView, MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;

    FlightListDoneAdapter mAdapter;
    List<CargoReportHisBean> mList;  //筛选过后的数据
    List<CargoReportHisBean> mListTemp; //原始数据

    private int pageCurrent = 1;
    private String searchString = "";//条件搜索关键字
    private TaskDoneFragment mTaskFragment; //父容器fragment
    private SearchToolbar searchToolbar;//父容器的输入框
    private boolean isShow = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_bagger_done, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("dime", "Fragment: 行李上报已办");
        mTaskFragment = (TaskDoneFragment) getParentFragment();
        searchToolbar = mTaskFragment.getSearchView();
        initView();
//        loadData();
    }

    private void initView() {
        mPresenter = new BaggageSubHisPresenter(this);
        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvData.setRefreshListener(this);
        mMfrvData.setOnRetryLisenter(this);
        mList = new ArrayList<>();
        mListTemp = new ArrayList<>();
        mAdapter = new FlightListDoneAdapter(mList);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            startActivity(new Intent(getContext(), BaggageDoneListActivity.class).putExtra("flightBean",  mList.get(position)));
        });
        mMfrvData.setAdapter(mAdapter);
        setUserVisibleHint(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isShow = isVisibleToUser;
        if (isVisibleToUser) {
            if (mTaskFragment != null)
                mTaskFragment.setTitleText(mListTemp.size());
            if (searchToolbar != null) {
                searchToolbar.setHintAndListener("请输入板车号", text -> {
                    searchString = text;
                    seachWithNum();
                });
            }

        }
    }

    /**
     * 通过条件筛选数据
     */
    private void seachWithNum() {
        mList.clear();
        if (TextUtils.isEmpty(searchString)) {
            mList.addAll(mListTemp);
        } else {
            for (CargoReportHisBean itemData : mListTemp) {
                if (itemData.getFlightNo().toLowerCase().contains(searchString.toLowerCase())) {
                    mList.add(itemData);
                }
            }
        }
        if (mMfrvData != null) {
            mMfrvData.notifyForAdapter(mAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
//        BaseFilterEntity entity = new BaseFilterEntity();
//        entity.setMinutes("120");
        ((BaggageSubHisPresenter) mPresenter).baggageSubHis(UserInfoSingle.getInstance().getUserId());
    }

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
    public void baggageSubHisResult(List<CargoReportHisBean> cargoReportHisBeans) {
        //因为没有分页，不做分页判断
        mListTemp.clear();
        if (pageCurrent == 1) {
            mMfrvData.finishRefresh();
        } else {
            mMfrvData.finishLoadMore();
        }

//        for (int i = 0; i < cargoReportHisBeans.size(); i++) {
            mListTemp.addAll(cargoReportHisBeans);
//        }
//        mListTemp.addAll(cargoReportHisBeans);
        if (mTaskFragment != null) {
            if (isShow)
                mTaskFragment.setTitleText(mListTemp.size());
        }
        seachWithNum();
    }

//    @Override
//    public void getDepartureFlightByAndroidResult(List<FlightLuggageBean> flightLuggageBeans) {
//
//    }

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


}