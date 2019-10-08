package qx.app.freight.qxappfreight.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.ForkliftWorkingCostBean;
import qx.app.freight.qxappfreight.bean.response.OverweightBean;

public class ForkliftRecordAdapterForNet extends BaseQuickAdapter<ForkliftWorkingCostBean, BaseViewHolder> {
    private OnDeleteClickLister mDeleteClickListener;
    public ForkliftRecordAdapterForNet(List <ForkliftWorkingCostBean> list) {
        super(R.layout.item_add_overweight, list);
    }

    @Override
    protected void convert(BaseViewHolder helper,ForkliftWorkingCostBean item) {
        helper.setText(R.id.tv_num, item.getNumber()+"");
        helper.setText(R.id.tv_weight, (item.getNumber()*50)+"");
    }

    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }
}
