package qx.app.freight.qxappfreight.adapter;

import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Date;
import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.SmInventoryEntryandexit;
import qx.app.freight.qxappfreight.bean.response.SmInventorySummary;
import qx.app.freight.qxappfreight.utils.TimeUtils;

public class InventoryAdapter extends BaseQuickAdapter<SmInventorySummary, BaseViewHolder> {

    private OnDoitClickListener mOnDoitClickListener;

    public InventoryAdapter(List<SmInventorySummary> data) {
        super(R.layout.item_io_inventory, data);
    }

    public void setOnDoitClickListener(OnDoitClickListener listener) {
        this.mOnDoitClickListener = listener;
    }

    @Override
    protected void convert(BaseViewHolder helper, SmInventorySummary item) {

        helper.setText(R.id.tv_no,(helper.getAdapterPosition()+1)+"");
        helper.setText(R.id.tv_waybill_number,item.getWaybillCode());
        helper.setText(R.id.tv_num,item.getNumber()+"");
        helper.setText(R.id.tv_weight,item.getWeight()+"");
//        Log.e("时间间隔：",TimeUtils.getDuration(item.getExecTime(),TimeUtils.getTime())+"");
        helper.setText(R.id.tv_time,  TimeUtils.getMinToDay(TimeUtils.getDuration(TimeUtils.getTime(),item.getExecTime())));
//        View viewDoit = helper.getView(R.id.tv_do_it);
//        viewDoit.setTag(helper.getAdapterPosition());
//        if (!viewDoit.hasOnClickListeners()) {
//            viewDoit.setOnClickListener(v -> {
//                if (mOnDoitClickListener != null) {
//                    mOnDoitClickListener.onDoitClick(v, (Integer) v.getTag());
//                }
//            });
//        }
    }


    public interface OnDoitClickListener {
        void onDoitClick(View view, int position);
    }
}
