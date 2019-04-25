package qx.app.freight.qxappfreight.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.request.ReturnGoodsEntity;

/**
 * 未收运记录列表适配器
 */
public class NotTransportListAdapter extends BaseQuickAdapter<ReturnGoodsEntity, BaseViewHolder> {
    private OnDeleteClickLister mDeleteClickListener;
    public NotTransportListAdapter(List<ReturnGoodsEntity> list) {
        super(R.layout.item_not_transport_list, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReturnGoodsEntity item) {
        helper.setText(R.id.tv_return_goods_info, mContext.getString(R.string.format_return_name_number, item.getGoodsName(), item.getReturnNumber()));
        helper.setText(R.id.tv_return_goods_reason, mContext.getString(R.string.format_return_reason, item.getReturnReason()));
        View viewDelete = helper.getView(R.id.ll_delete);
        viewDelete.setTag(helper.getAdapterPosition());
        viewDelete.setOnClickListener(v -> {
            if (mDeleteClickListener != null) {
                mDeleteClickListener.onDeleteClick((Integer) v.getTag());
            }
        });
    }
    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(int position);
    }
}
