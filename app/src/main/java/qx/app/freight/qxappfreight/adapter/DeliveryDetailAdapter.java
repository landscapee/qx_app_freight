package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.ArrivalDeliveryInfoBean;
import qx.app.freight.qxappfreight.bean.response.ItemBean;
import qx.app.freight.qxappfreight.utils.TimeUtils;

public class DeliveryDetailAdapter extends BaseQuickAdapter<ArrivalDeliveryInfoBean.WaybillsBean, BaseViewHolder> {
    private DeliveryDetailInterface listener;
    public DeliveryDetailAdapter(@Nullable List<ArrivalDeliveryInfoBean.WaybillsBean> data) {
        super(R.layout.item_delivery_detail, data);
    }

    public void setDeliveryDetailInterface(DeliveryDetailInterface listener){
        this.listener = listener;
    }

    @Override
    protected void convert(BaseViewHolder holder, ArrivalDeliveryInfoBean.WaybillsBean bean) {
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
            holder.setVisible(R.id.tv_outStorage,false);
            holder.setVisible(R.id.btn_outStorage,true);
        }else if (bean.getWaybillStatus() ==6){
            holder.setVisible(R.id.tv_outStorage,true);
            holder.setVisible(R.id.btn_outStorage,false);
        }
        Button btnOutStorage = holder.getView(R.id.btn_outStorage);
        btnOutStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.outStorage(bean.getId(),bean.getOutStorageUser());
            }
        });

    }

    public interface DeliveryDetailInterface{
        void outStorage(String id,String outStorageUser);

    }
}
