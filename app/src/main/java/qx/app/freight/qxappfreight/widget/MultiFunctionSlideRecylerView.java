package qx.app.freight.qxappfreight.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.ouyben.empty.EmptyLayout;

import qx.app.freight.qxappfreight.R;

/**
 * 支持上下拉刷新、左滑显示更多操作以及空数据显示的封装RecylerView
 */
public class MultiFunctionSlideRecylerView extends LinearLayout {
    private Context mContext;
    private PullToRefreshLayout mRefreshView;
    private EmptyLayout mElEmpty;
    private SlideRecyclerView mRvData;

    public MultiFunctionSlideRecylerView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public MultiFunctionSlideRecylerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_multi_slide_function, this);
        mRefreshView = view.findViewById(R.id.ptrl_refresh);
        mElEmpty = view.findViewById(R.id.layout_empty);
        mRvData = view.findViewById(R.id.rv_data);
    }

    public void setRefreshListener(OnRefreshListener refreshListener) {
        mRefreshView.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                refreshListener.onRefresh();
                mRefreshView.finishRefresh();
            }

            @Override
            public void loadMore() {
                refreshListener.onLoadMore();
                mRefreshView.finishLoadMore();
            }
        });
    }

    /**
     * 设置layoutManager
     *
     * @param manager layoutManager
     */
    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        mRvData.setLayoutManager(manager);
    }

    public void closeMenu(){
        mRvData.closeMenu();
    }

    public void finishRefresh(){
        mRefreshView.finishRefresh();
    }

    public void finishLoadMore(){

        mRefreshView.finishLoadMore();
    }
    /**
     * 数据发生改变 更新列表
     *
     * @param adapter 适配器
     */
    public void notifyForAdapter(RecyclerView.Adapter adapter) {
        if (adapter.getItemCount() == 0) {
            this.mRvData.setVisibility(GONE);
            mElEmpty.showEmpty();
        } else {
            this.mRvData.setVisibility(VISIBLE);
            mElEmpty.showSuccess();
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 关联适配器
     *
     * @param adapter 适配器
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        mRvData.setAdapter(adapter);
    }

    public void setOnRetryLisenter(EmptyLayout.OnRetryLisenter lisenter) {
        mElEmpty.setOnRetryLisenter(lisenter);
    }

    public interface OnRefreshListener {
        void onRefresh();

        void onLoadMore();
    }
}
