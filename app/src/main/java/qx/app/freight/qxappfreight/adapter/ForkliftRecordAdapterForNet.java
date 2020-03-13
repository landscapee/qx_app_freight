package qx.app.freight.qxappfreight.adapter;

import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.ForkliftWorkingCostBean;

public class ForkliftRecordAdapterForNet extends BaseQuickAdapter<ForkliftWorkingCostBean, BaseViewHolder> {
    private OnDeleteClickLister mDeleteClickListener;
    public ForkliftRecordAdapterForNet(List <ForkliftWorkingCostBean> list) {
        super(R.layout.item_add_overweight, list);
    }

    @Override
    protected void convert(BaseViewHolder helper,ForkliftWorkingCostBean item) {
        RelativeLayout rlDelete = helper.getView(R.id.rl_delete);
        rlDelete.setVisibility(View.GONE);
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
