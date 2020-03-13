package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;

/**
 * 板车号搜索结果适配器
 */
public class UldAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private OnItemClickListener onItemClickListener;

    public UldAdapter(@Nullable List<String> data) {
        super(R.layout.item_search_board_rv, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_board_result, item);
        helper.itemView.setOnClickListener(v -> onItemClickListener.onItemClicked(item, helper.getAdapterPosition()));
    }

    public interface OnItemClickListener {
        void onItemClicked(String result, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
