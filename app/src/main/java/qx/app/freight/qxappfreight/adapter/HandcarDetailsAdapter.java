package qx.app.freight.qxappfreight.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.FtGroupScooter;

public class HandcarDetailsAdapter extends BaseQuickAdapter<FtGroupScooter, BaseViewHolder> {

    private OnDeleteClickLister mDeleteClickListener;

    public HandcarDetailsAdapter(List<FtGroupScooter> data) {
        super(R.layout.item_handcar_details, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FtGroupScooter item) {
        View viewDelete = helper.getView(R.id.rl_delete);
        viewDelete.setTag(helper.getAdapterPosition());
        if (!viewDelete.hasOnClickListeners()) {
            viewDelete.setOnClickListener(v -> {
                if (mDeleteClickListener != null) {
                    mDeleteClickListener.onDeleteClick(v, (Integer) v.getTag());
                }
            });
        }

        helper.setText(R.id.tv_num,helper.getAdapterPosition()+"");
        helper.setText(R.id.tv_waybill_number,item.getWaybillCode()+"");
        helper.setText(R.id.tv_waybill_count,item.getNumber()+"");
        helper.setText(R.id.tv_weight,item.getWeight()+"");
        ImageView imgDelete = helper.getView(R.id.iv_delete);
        //不是本航班
        if (item.getInFlight()!=null && item.getInFlight() == 1){
            helper.setTextColor(R.id.tv_num,mContext.getResources().getColor(R.color.red));
            helper.setTextColor(R.id.tv_waybill_number,mContext.getResources().getColor(R.color.red));
            helper.setTextColor(R.id.tv_waybill_count,mContext.getResources().getColor(R.color.red));
            helper.setTextColor(R.id.tv_weight,mContext.getResources().getColor(R.color.red));
            imgDelete.setImageResource(R.mipmap.delete);

        }//不是本航段的
        else if(item.getInFlightCourse()!=null && item.getInFlightCourse() == 0){
            helper.setTextColor(R.id.tv_num,mContext.getResources().getColor(R.color.yellow));
            helper.setTextColor(R.id.tv_waybill_number,mContext.getResources().getColor(R.color.yellow));
            helper.setTextColor(R.id.tv_waybill_count,mContext.getResources().getColor(R.color.yellow));
            helper.setTextColor(R.id.tv_weight,mContext.getResources().getColor(R.color.yellow));
            imgDelete.setImageResource(R.mipmap.drop_down);
        }
        else
        {
            helper.setTextColor(R.id.tv_num,mContext.getResources().getColor(R.color.text_color));
            helper.setTextColor(R.id.tv_waybill_number,mContext.getResources().getColor(R.color.text_color));
            helper.setTextColor(R.id.tv_waybill_count,mContext.getResources().getColor(R.color.text_color));
            helper.setTextColor(R.id.tv_weight,mContext.getResources().getColor(R.color.text_color));
            imgDelete.setImageResource(R.mipmap.drop_down);
        }

    }

    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }
}
