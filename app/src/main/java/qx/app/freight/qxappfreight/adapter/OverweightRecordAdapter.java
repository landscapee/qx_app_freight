package qx.app.freight.qxappfreight.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.RcInfoOverweight;

public class OverweightRecordAdapter extends BaseQuickAdapter<RcInfoOverweight, BaseViewHolder> {
    private OnDeleteClickLister mDeleteClickListener;
    public OverweightRecordAdapter(List <RcInfoOverweight> list) {
        super(R.layout.item_add_overweight, list);
    }

    @Override
    protected void convert(BaseViewHolder helper,RcInfoOverweight item) {
        helper.setText(R.id.tv_num, item.getCount()+"");
        helper.setText(R.id.tv_weight, item.getWeight()+"");
        helper.setText(R.id.tv_volume, item.getVolume()+"");
        helper.setText(R.id.tv_overweight, item.getOverWeight()+"");
        //删除按钮
        View viewDelete = helper.getView(R.id.rl_delete);
        viewDelete.setTag(helper.getAdapterPosition());
        if (!viewDelete.hasOnClickListeners()) {
            viewDelete.setOnClickListener(v -> {
                if (mDeleteClickListener != null) {
                    mDeleteClickListener.onDeleteClick(v, helper.getAdapterPosition());
                }
            });
        }
        //文件
    }

    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }
}
