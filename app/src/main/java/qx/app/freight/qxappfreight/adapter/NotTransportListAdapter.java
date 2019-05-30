package qx.app.freight.qxappfreight.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.request.SecurityCheckResult;
import qx.app.freight.qxappfreight.utils.StringUtil;

/**
 * 未收运记录列表适配器
 */
public class NotTransportListAdapter extends BaseQuickAdapter<SecurityCheckResult, BaseViewHolder> {
    private OnDeleteClickLister mDeleteClickListener;

    public NotTransportListAdapter(List<SecurityCheckResult> list) {
        super(R.layout.item_not_transport_list, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, SecurityCheckResult item) {
        helper.setText(R.id.tv_return_goods_info, mContext.getString(R.string.format_return_name_number, StringUtil.toText(item.getCommodity()),item.getPiece()));
        helper.setText(R.id.tv_return_goods_reason, mContext.getString(R.string.format_return_reason, StringUtil.toText(item.getReason())));
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
