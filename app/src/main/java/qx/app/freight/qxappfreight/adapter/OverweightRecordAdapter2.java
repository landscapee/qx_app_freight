package qx.app.freight.qxappfreight.adapter;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.RcInfoOverweight;

/**
 * created by swd
 * 2019/5/23 13:09
 */

public class OverweightRecordAdapter2 extends BaseMultiItemQuickAdapter<RcInfoOverweight, BaseViewHolder> {
    private OnDeleteClickLister mDeleteClickListener;
    public OverweightRecordAdapter2(List <RcInfoOverweight> list) {
        super(list);
        addItemType(0, R.layout.item_add_overweight);
        addItemType(1, R.layout.item_empty);
    }

    @Override
    protected void convert(BaseViewHolder helper,RcInfoOverweight item) {

        switch (helper.getItemViewType()) {
            case 0:
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
                break;
            case 1:

                break;
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
