package qx.app.freight.qxappfreight.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.utils.MapValue;
import qx.app.freight.qxappfreight.utils.TimeUtils;

public class HandcarBacklogTPAdapter extends BaseQuickAdapter<TransportTodoListBean, BaseViewHolder> {

    private HandcarBacklogTPAdapter.OnDeleteClickLister mDeleteClickListener;

    public HandcarBacklogTPAdapter(List<TransportTodoListBean> data) {
        super(R.layout.item_handcar_backlog_tp, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TransportTodoListBean item) {

        //板车类型 - 板车号
        helper.setText(R.id.tv_handcar_num,String.format(mContext.getString(R.string.format_company_info)
        ,item.getTpCargoType()
        ,item.getTpScooterCode()));
        //件数 - 重量 - 体积
        helper.setText(R.id.allocate_info,String.format(mContext.getString(R.string.format_store_number_goods_info)
        ,item.getTpCargoNumber()
        ,item.getTpCargoWeight()
        ,item.getTpCargoVolume()));
        //航班号
        helper.setText(R.id.allocate_flightnumber,item.getTpFlightNumber());
        //机位号
        helper.setText(R.id.allocate_machinenumber,item.getTpFlightLocate());
        // 时间
        helper.setText(R.id.tv_plan_time, TimeUtils.date2Tasktime3(item.getTpFlightTime()));
        //仓位
        helper.setText(R.id.allocate_time_info,item.getTpFregihtSpace());

        helper.setText(R.id.tv_origin, MapValue.getLocationValue(item.getTpStartLocate()));
        helper.setText(R.id.tv_destination,MapValue.getLocationValue(item.getTpDestinationLocate()));


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
    public void setOnDeleteClickListener(HandcarBacklogTPAdapter.OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }

}
