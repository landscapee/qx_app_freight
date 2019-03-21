package qx.app.freight.qxappfreight.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.HandcarBacklogTPAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.contract.TransportTodoListContract;
import qx.app.freight.qxappfreight.presenter.TransportTodoListPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

public class DriverInBacklogActivity extends BaseActivity implements TransportTodoListContract.transportTodoListView {


    @BindView(R.id.slrv_car_backlog)
    SlideRecyclerView backlogSlideRecyclerView;
    @BindView(R.id.down_backlog)
    SlideRecyclerView mDownBackLog;
    private List<TransportTodoListBean> listBacklog;
    private List<TransportTodoListBean> mDownList;
    private HandcarBacklogTPAdapter mHandcarBacklogTPAdapterBacklog;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DriverInBacklogActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_driver_backlog;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        setToolbarShow(View.VISIBLE);
        CustomToolbar toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "待办列表");
        toolbar.setRightIconView(View.VISIBLE, R.mipmap.search, v -> ToastUtil.showToast( "搜索"));

        //正常货物
        backlogSlideRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listBacklog = new ArrayList<>();
        mHandcarBacklogTPAdapterBacklog = new HandcarBacklogTPAdapter(listBacklog);
        backlogSlideRecyclerView.setAdapter(mHandcarBacklogTPAdapterBacklog);

        //下拉货物
        mDownBackLog.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mDownList = new ArrayList<>();
        mHandcarBacklogTPAdapterBacklog = new HandcarBacklogTPAdapter(mDownList);
        mDownBackLog.setAdapter(mHandcarBacklogTPAdapterBacklog);
        getData();
    }

    private void getData() {
        mPresenter = new TransportTodoListPresenter(this);
        ((TransportTodoListPresenter)mPresenter).transportTodoList();
    }

    @Override
    public void transportTodoListResult(List<TransportTodoListBean> transportTodoListBeans) {
        if (transportTodoListBeans != null){
            listBacklog.clear();
            mDownList.clear();
            listBacklog.addAll(transportTodoListBeans);
            mDownList.addAll(transportTodoListBeans);
            mHandcarBacklogTPAdapterBacklog.notifyDataSetChanged();
        }
    }

    @Override
    public void toastView(String error) {
        Log.e("错误",error);
    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }
}
