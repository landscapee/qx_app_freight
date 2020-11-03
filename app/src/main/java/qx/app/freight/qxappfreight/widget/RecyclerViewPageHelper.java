package qx.app.freight.qxappfreight.widget;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Created by zzq On 2020/7/3 16:57 & Copyright (C), 青霄科技
 *
 * @文档说明: RecylerView翻页展示数据工具helper
 */
public class RecyclerViewPageHelper extends RecyclerView.OnScrollListener {
    private SnapHelper snapHelper;
    private OnPageChangeListener onPageChangeListener;
    private int oldPosition = -1;//防止同一Position多次触发

    public RecyclerViewPageHelper(SnapHelper snapHelper, OnPageChangeListener onPageChangeListener) {
        this.snapHelper = snapHelper;
        this.onPageChangeListener = onPageChangeListener;
    }

    @Override
    public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        int position = 0;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        //获取当前选中的itemView
        View view = snapHelper.findSnapView(layoutManager);
        if (view != null) {
            //获取itemView的position
            position = Objects.requireNonNull(layoutManager).getPosition(view);
        }
        if (onPageChangeListener != null) {
            //newState == RecyclerView.SCROLL_STATE_IDLE 当滚动停止时触发防止在滚动过程中不停触发
            if (newState == RecyclerView.SCROLL_STATE_IDLE && oldPosition != position) {
                oldPosition = position;
                onPageChangeListener.onPageSelected(position);
            }
        }
    }

    public interface OnPageChangeListener {
        void onPageSelected(int position);
    }
}
