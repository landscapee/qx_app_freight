package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.ouyben.empty.EmptyLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.AllocaaateHistoryAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.AllocaaateHitoryBean;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.GetHistoryBean;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.GetHistoryContract;
import qx.app.freight.qxappfreight.presenter.GetHistoryPresenter;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

public class AllocaaateHistoryActivity extends BaseActivity implements GetHistoryContract.getHistoryView, EmptyLayout.OnRetryLisenter, MultiFunctionRecylerView.OnRefreshListener {
    @BindView(R.id.mfrv_allocate_list)
    MultiFunctionRecylerView mMfrvAllocateList;

    private AllocaaateHistoryAdapter mAdapter;
    private List<GetInfosByFlightIdBean> list;
    private int pageCurrent = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_allocaaate_history;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        initView();
        getData();
    }

    private void initView() {

        setToolbarShow(View.VISIBLE);
        CustomToolbar toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "复重历史");

        list = new ArrayList<>();
        mAdapter = new AllocaaateHistoryAdapter(list);
        mMfrvAllocateList.setLayoutManager(new LinearLayoutManager(this));
        mMfrvAllocateList.setOnRetryLisenter(this);
        mMfrvAllocateList.setRefreshListener(this);
        mMfrvAllocateList.setAdapter(mAdapter);
    }

    private void getData() {
        mPresenter = new GetHistoryPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        AllocaaateHitoryBean bean = new AllocaaateHitoryBean();
        bean.setReWeighedUserId(UserInfoSingle.getInstance().getUserId());
        entity.setFilter(bean);
        entity.setCurrent(pageCurrent);
        entity.setSize(Constants.PAGE_SIZE);
        ((GetHistoryPresenter) mPresenter).getHistory(entity);
    }

    @Override
    public void getHistoryResult(GetHistoryBean getHistoryBean) {
        if (pageCurrent == 1) {
            list.clear();
            mMfrvAllocateList.finishRefresh();
        } else {
            mMfrvAllocateList.finishLoadMore();
        }
        list.addAll(getHistoryBean.getRecords());
        mMfrvAllocateList.notifyForAdapter(mAdapter);
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

    @Override
    public void onRetry() {
        showProgessDialog("正在加载数据……");
        new Handler().postDelayed(() -> {
            getData();
            dismissProgessDialog();
        }, 2000);
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
}
