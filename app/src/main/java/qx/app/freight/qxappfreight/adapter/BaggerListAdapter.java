package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;

public class BaggerListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private OnDeleteClickLister mDeleteClickListener;

    public BaggerListAdapter(List<String> data) {
        super(R.layout.item_bagger_list, data);
    }

    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }



    @Override
    protected void convert(BaseViewHolder helper, String item) {



        View viewDelete = helper.getView(R.id.ll_delete);
        viewDelete.setTag(helper.getAdapterPosition());
        if (!viewDelete.hasOnClickListeners()) {
            viewDelete.setOnClickListener(v -> {
                if (mDeleteClickListener != null) {
                    mDeleteClickListener.onDeleteClick(v, (Integer) v.getTag());
                }
            });
        }
    }


    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }
}
