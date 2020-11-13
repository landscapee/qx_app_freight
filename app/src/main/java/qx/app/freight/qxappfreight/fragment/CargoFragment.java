package qx.app.freight.qxappfreight.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ouyben.empty.EmptyLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.CargoListActivity;
import qx.app.freight.qxappfreight.adapter.FlightListAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightLuggageBean;
import qx.app.freight.qxappfreight.contract.LookLUggageScannigFlightContract;
import qx.app.freight.qxappfreight.presenter.LookLUggageScannigFlightPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.SearchToolbar;

/**
 * 货物上报Fragment
 * <p>
 * create by swd
 */
public class CargoFragment extends BaseFragment implements LookLUggageScannigFlightContract.lookLUggageScannigFlightView, MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_data)
    MultiFunctionRecylerView mMfrvData;
    private FlightListAdapter mAdapter;
    private List<FlightLuggageBean> mList;  //筛选过后的数据
    private List<FlightLuggageBean> mListTemp; //原始数据
    private int pageCurrent = 1;
    private String searchString = "";//条件搜索关键字
    private TaskFragment mTaskFragment; //父容器fragment
    private SearchToolbar searchToolbar;//父容器的输入框
    private boolean isShow = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_international_cargo, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTaskFragment = (TaskFragment) getParentFragment();
        searchToolbar = mTaskFragment.getSearchView();
        initView();
        setUserVisibleHint(true);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mPresenter = new LookLUggageScannigFlightPresenter(this);
        mMfrvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mMfrvData.setRefreshListener(this);
        mMfrvData.setOnRetryLisenter(this);
        mList = new ArrayList<>();
        mListTemp = new ArrayList<>();
        mAdapter = new FlightListAdapter(mList,getContext());
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            startActivity(new Intent(getContext(), CargoListActivity.class).putExtra("flightBean", mList.get(position)));
        });
        mMfrvData.setAdapter(mAdapter);
    }

    private void loadData() {
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setMinutes("120");
        ((LookLUggageScannigFlightPresenter) mPresenter).getDepartureFlightByAndroid(entity);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isShow = isVisibleToUser;
        if (isVisibleToUser) {
            if (mTaskFragment != null) {
                mTaskFragment.setTitleText(mListTemp.size());
            }
            if (searchToolbar != null) {
                searchToolbar.setHintAndListener("请输入航班号", text -> {
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
            for (FlightLuggageBean itemData : mListTemp) {
                if (itemData.getFlightNo()!=null&&itemData.getFlightNo().toLowerCase().contains(searchString.toLowerCase())) {
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

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(error);
        if (mMfrvData != null) {
            mMfrvData.finishLoadMore();
        }
        if (mMfrvData != null) {
            mMfrvData.finishRefresh();
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
        pageCurrent = 1;
        showProgessDialog("正在加载数据……");
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
        loadData();
    }

    @Override
    public void getDepartureFlightByAndroidResult(List<FlightLuggageBean> flightLuggageBeans) {
        if (pageCurrent == 1) {
            mListTemp.clear();
            mMfrvData.finishRefresh();
        } else {
            mMfrvData.finishLoadMore();
        }
        pageCurrent++;
        mListTemp.addAll(flightLuggageBeans);
        if (mTaskFragment != null) {
            if (isShow) {
                mTaskFragment.setTitleText(mListTemp.size());
            }
        }
        seachWithNum();
    }
}