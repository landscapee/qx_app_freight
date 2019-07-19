package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.ouyben.empty.EmptyLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.MessageAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PageListEntity;
import qx.app.freight.qxappfreight.bean.response.MsMessageViewBean;
import qx.app.freight.qxappfreight.bean.response.PageListBean;
import qx.app.freight.qxappfreight.contract.MessageContract;
import qx.app.freight.qxappfreight.presenter.MessagePresenter;
import qx.app.freight.qxappfreight.presenter.TransportListCommitPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.MultiFunctionSlideRecylerView;

/**
 * 消息列表页面
 * Created by swd
 */
public class MessageActivity extends BaseActivity implements MessageContract.messageView ,MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {

    @BindView(R.id.mfrv_message)
    MultiFunctionRecylerView mfrvMessage;

    private CustomToolbar toolbar;
    private MessageAdapter mAdapter;
    private List<PageListBean.RecordsBean> list;
    private int pageCurrent =1;
    private int nowPosition;

    @Override
    public int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        initTitle();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    private void initTitle() {
        setToolbarShow(View.VISIBLE);
        toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "消息提醒");
    }

    private void initView() {
        mPresenter = new MessagePresenter(this);
        list = new ArrayList<>();
        mAdapter = new MessageAdapter(list);
        mfrvMessage.setLayoutManager(new LinearLayoutManager(this));
        mfrvMessage.setRefreshListener(this);
        mfrvMessage.setOnRetryLisenter(this);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (list.get(position).getReadingStatus()==0){
                readMessage(position);
                nowPosition = position;
            }

        });

        mfrvMessage.setAdapter(mAdapter);
    }
    //
    private void requestData(){
        BaseFilterEntity bean = new BaseFilterEntity();
        PageListEntity listBean =new PageListEntity();
        List<String> requestList = new ArrayList();
        requestList.add("create_time");
        listBean.setUserId(UserInfoSingle.getInstance().getUserId());
        listBean.setRole(UserInfoSingle.getInstance().getRoleRS().get(0).getRoleCode());
        bean.setSize(20);
        bean.setCurrent(pageCurrent);
        bean.setDesc(requestList);
        bean.setFilter(listBean);
        ((MessagePresenter)mPresenter).pageList(bean);
    }

    private void readMessage(int position){
        BaseFilterEntity bean = new BaseFilterEntity();
        bean.setMessageId(list.get(position).getId());
        bean.setUserId(UserInfoSingle.getInstance().getUserId());
        ((MessagePresenter)mPresenter).msMessageView(bean);
    }

    @Override
    public void onRefresh() {
        pageCurrent =1;
        requestData();
    }

    @Override
    public void onLoadMore() {
        requestData();
    }

    @Override
    public void onRetry() {
        showProgessDialog("加载数据中……");
        new Handler().postDelayed(() -> {
            requestData();
            dismissProgessDialog();
        }, 2000);
    }

    @Override
    public void pageListResult(PageListBean pageListBean) {

        if (pageCurrent == 1) {
            list.clear();
            mfrvMessage.finishRefresh();
        } else {
            mfrvMessage.finishLoadMore();
        }
        if (pageListBean.getPages()>=pageCurrent){
            pageCurrent++;
        }else {
            ToastUtil.showToast("没有更多数据！");
            return;
        }

        list.addAll(pageListBean.getRecords());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void msMessageViewResult(MsMessageViewBean msMessageViewBean) {
        list.get(nowPosition).setReadingStatus(1);
        mAdapter.notifyItemChanged(nowPosition);
    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("请求中......");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }
}
