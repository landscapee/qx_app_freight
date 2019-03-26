package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
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
import qx.app.freight.qxappfreight.bean.response.MsMessageViewBean;
import qx.app.freight.qxappfreight.bean.response.PageListBean;
import qx.app.freight.qxappfreight.contract.MessageContract;
import qx.app.freight.qxappfreight.presenter.TransportListCommitPresenter;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionSlideRecylerView;

/**
 * 消息列表页面
 * Created by swd
 */
public class MessageActivity extends BaseActivity implements MessageContract.messageView ,MultiFunctionSlideRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {

    @BindView(R.id.mfrv_message)
    MultiFunctionSlideRecylerView mfrvMessage;

    private CustomToolbar toolbar;
    private MessageAdapter mAdapter;
    private List<PageListBean.RecordsBean> list;
    private int pageCurrent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        initTitle();
        initView();
    }

    private void initTitle() {
        setToolbarShow(View.VISIBLE);
        toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "系统消息");
    }

    private void initView() {
        list = new ArrayList<>();
        mfrvMessage.setLayoutManager(new LinearLayoutManager(this));
        mfrvMessage.setRefreshListener(this);
        mfrvMessage.setOnRetryLisenter(this);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {

        });
        mAdapter.setOnDeleteClickListener((view, position) -> {
            if (list.size() != 0) {
                mfrvMessage.closeMenu();
//                Toast.makeText(ReceiveGoodsActivity.this, "当前删除：" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestData(){

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRetry() {

    }

    @Override
    public void pageListResult(PageListBean pageListBean) {

    }

    @Override
    public void msMessageViewResult(MsMessageViewBean msMessageViewBean) {

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
