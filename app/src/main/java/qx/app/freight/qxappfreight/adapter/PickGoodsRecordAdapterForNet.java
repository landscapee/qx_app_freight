package qx.app.freight.qxappfreight.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.ForkliftWorkingCostBean;
import qx.app.freight.qxappfreight.bean.response.PickGoodsRecordsBean;
import qx.app.freight.qxappfreight.utils.TimeUtils;

public class PickGoodsRecordAdapterForNet extends BaseQuickAdapter<PickGoodsRecordsBean, BaseViewHolder> {
    private OnDeleteClickLister mDeleteClickListener;
    public PickGoodsRecordAdapterForNet(List <PickGoodsRecordsBean> list) {
        super(R.layout.item_pick_goods, list);
    }

    @Override
    protected void convert(BaseViewHolder helper,PickGoodsRecordsBean item) {
        Button btnRevocation = helper.getView(R.id.btn_revocation);
        helper.setText(R.id.tv_no, (helper.getAdapterPosition()+1)+"");
        helper.setText(R.id.tv_count, item.getOutboundNumber()+"");
        helper.setText(R.id.tv_time, TimeUtils.getTimeForParam(item.getCreateTime()));
        if (item.getRevokeFlag() == 1)
            btnRevocation.setVisibility(View.INVISIBLE);
        else
            btnRevocation.setVisibility(View.VISIBLE);

        btnRevocation.setOnClickListener(v -> {
            mDeleteClickListener.onDeleteClick(v,helper.getAdapterPosition());
        });
    }

    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }
}
