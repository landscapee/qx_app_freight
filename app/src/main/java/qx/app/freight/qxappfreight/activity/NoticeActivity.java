package qx.app.freight.qxappfreight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.ouyben.empty.EmptyLayout;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.MessageAdapter;
import qx.app.freight.qxappfreight.adapter.NoticeAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PageListEntity;
import qx.app.freight.qxappfreight.bean.response.NoticeBean;
import qx.app.freight.qxappfreight.bean.response.NoticeViewBean;
import qx.app.freight.qxappfreight.bean.response.PageListBean;
import qx.app.freight.qxappfreight.contract.FindUserNoticeByPageContract;
import qx.app.freight.qxappfreight.presenter.FindUserNoticeByPagePresenter;
import qx.app.freight.qxappfreight.presenter.MessagePresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;
import qx.app.freight.qxappfreight.widget.MultiFunctionSlideRecylerView;

public class NoticeActivity extends BaseActivity implements FindUserNoticeByPageContract.findUserNoticeByPageView, MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_notice)
    MultiFunctionRecylerView mfrvNotice;

    private CustomToolbar toolbar;
    private NoticeAdapter mAdapter;
    private List<NoticeBean.RecordsBean> list;
    private int pageCurrent =1;
    private int nowPosition;
    @Override
    public int getLayoutId() {
        return R.layout.activity_notice;
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
        mPresenter = new FindUserNoticeByPagePresenter(this);
        list = new ArrayList<>();
        mAdapter = new NoticeAdapter(list);
        mfrvNotice.setLayoutManager(new LinearLayoutManager(this));
        mfrvNotice.setRefreshListener(this);
        mfrvNotice.setOnRetryLisenter(this);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
                readMessage(position);
                nowPosition = position;


        });
//        mAdapter.setOnDeleteClickListener((view, position) -> {
//            if (list.size() != 0) {
//                mfrvNotice.closeMenu();
////                Toast.makeText(ReceiveGoodsActivity.this, "当前删除：" + position, Toast.LENGTH_SHORT).show();
//            }
//        });
        mfrvNotice.setAdapter(mAdapter);
    }

    private void requestData(){
        BaseFilterEntity bean = new BaseFilterEntity();
        NoticeViewBean viewBean = new NoticeViewBean();
        viewBean.setCreateUser(UserInfoSingle.getInstance().getUserId());
        bean.setSize(20);
        bean.setCurrent(pageCurrent);
        bean.setFilter(viewBean);
        ((FindUserNoticeByPagePresenter)mPresenter).findUserNoticeByPage(bean);
    }

    /**查看详情
     *
     * @param position
     */
    private void readMessage(int position) {
        BaseFilterEntity bean = new BaseFilterEntity();
        bean.setUserId(UserInfoSingle.getInstance().getUserId());
        bean.setNoticeId(list.get(position).getId());
        ((FindUserNoticeByPagePresenter)mPresenter).noticeView(bean);
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
    public void noticeResult(NoticeBean result) {
        if (pageCurrent == 1) {
            list.clear();
            mfrvNotice.finishRefresh();
        } else {
            mfrvNotice.finishLoadMore();
        }
        if (result.getPages()>=pageCurrent){
            pageCurrent++;
        }else {
            ToastUtil.showToast("没有更多数据！");
            return;
        }

        list.addAll(result.getRecords());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void noticeViewResult(NoticeViewBean result) {
        startActivity(new Intent(this,NoticeDetailActivity.class).putExtra("NoticeViewBean", result));
        if (list.get(nowPosition).getReadingStatus()==0){
            list.get(nowPosition).setReadingStatus(1);
            mAdapter.notifyItemChanged(nowPosition);
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
