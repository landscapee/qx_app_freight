package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;
import qx.app.freight.qxappfreight.utils.TimeUtils;

public class DeliveryDetailAdapter extends BaseQuickAdapter<WaybillsBean, BaseViewHolder> {
    private DeliveryDetailInterface listener;
    public DeliveryDetailAdapter(@Nullable List<WaybillsBean> data) {
        super(R.layout.item_delivery_detail, data);
    }

    public void setDeliveryDetailInterface(DeliveryDetailInterface listener){
        this.listener = listener;
    }

    @Override
    protected void convert(BaseViewHolder holder, WaybillsBean bean) {
        holder.setText(R.id.waybill_code, bean.getWaybillCode());
                //件数 - 重量
        holder.setText(R.id.total_info, String.format(mContext.getString(R.string.format_goods_inport)
                ,bean.getTotalNumberPackages()
                ,bean.getTotalWeight()));
                //件数 - 重量
        holder.setText(R.id.tallying_info, String.format(mContext.getString(R.string.format_goods_inport)
                        , bean.getTallyingTotal()
                        ,bean.getTallyingWeight()))
                .setText(R.id.consignee,bean.getConsignee())
                .setText(R.id.consignee_phone,bean.getConsigneePhone())
                .setText(R.id.consignee_card,bean.getConsigneeCarid());
        if (bean.getOutStorageTime()>0){
            holder.setVisible(R.id.tv_complete_time,true);
            holder.setText(R.id.tv_complete_time, TimeUtils.date2Tasktime6(bean.getOutStorageTime()));
        }

        if (bean.getWaybillStatus()==5){
            holder.setGone(R.id.tv_outStorage,false);
            holder.setVisible(R.id.btn_outStorage,true);
        }else if (bean.getWaybillStatus() ==6){
            holder.setVisible(R.id.tv_outStorage,true);
            holder.setGone(R.id.btn_outStorage,false);
        }
        Button btnOutStorage = holder.getView(R.id.btn_outStorage);
        btnOutStorage.setOnClickListener(v ->
                listener.outStorage(holder.getAdapterPosition(),bean.getId(),bean.getOutStorageUser())
        );

    }

    public interface DeliveryDetailInterface{
        void outStorage(int position,String id,String outStorageUser);

    }
}
