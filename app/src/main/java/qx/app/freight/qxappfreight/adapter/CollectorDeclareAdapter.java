package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.DeclareItem;

public class CollectorDeclareAdapter extends BaseQuickAdapter<DeclareItem, BaseViewHolder> {
    private OnDeleteClickLister mDeleteClickListener;
    public CollectorDeclareAdapter(@Nullable List<DeclareItem> data) {
        super(R.layout.item_collector_declare, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeclareItem item) {
        helper.setText(R.id.tv_name,item.getCargoCn())
                .setText(R.id.tv_number,item.getNumber()+"")
                .setText(R.id.tv_weight,item.getWeight()+"")
                .setText(R.id.tv_volume,item.getVolume()+"")
                .setText(R.id.tv_type,item.getType()+"");

        ImageView viewDelete = helper.getView(R.id.iv_delete);
        ImageView viewEdit = helper.getView(R.id.iv_edit);


        viewDelete.setOnClickListener(v -> {
            mDeleteClickListener.onDeleteClick(v,helper.getAdapterPosition());
        });
        viewEdit.setOnClickListener(v -> {
            mDeleteClickListener.onEditClick(v,helper.getAdapterPosition());
        });

    }
    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }



    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
        void onEditClick(View view, int position);
    }
}
