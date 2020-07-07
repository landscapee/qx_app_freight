package qx.app.freight.qxappfreight.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.PagedIndexBean;

/**
 * Created by zzq On 2020/7/3 17:15 & Copyright (C), 青霄科技
 *
 * @文档说明: 翻页样式显示运单数据的指示器适配器
 */
public class PagedIndexAdapter extends BaseQuickAdapter<PagedIndexBean, BaseViewHolder> {
    public PagedIndexAdapter(@Nullable List<PagedIndexBean> data) {
        super(R.layout.item_paged_index, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PagedIndexBean item) {
        TextView tvIndex = helper.getView(R.id.tv_index_number);
        tvIndex.setText(item.getIndex());
        if (item.isChecked()) {
            tvIndex.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_circle_blue));
            tvIndex.setTextColor(Color.WHITE);
        } else {
            tvIndex.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_circle_gray_empty));
            tvIndex.setTextColor(Color.BLACK);
        }
    }
}
