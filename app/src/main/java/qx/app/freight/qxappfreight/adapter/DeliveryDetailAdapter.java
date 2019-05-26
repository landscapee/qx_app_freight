package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
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
                //录单 件数 - 重量
        holder.setText(R.id.total_info, String.format(mContext.getString(R.string.format_goods_inport)
                ,bean.getTotalNumberPackages()+""
                ,bean.getTotalWeight()));
        //超重
        holder.setText(R.id.tv_overweight, String.format(mContext.getString(R.string.format_goods_inport)
                ,bean.getOverWieghtCount()+""
                ,bean.getOverWieght()));

        //分拣件数 - 重量
        holder.setText(R.id.tallying_info,bean.getTallyingTotal()+"件");
        //逾期费用
        holder.setText(R.id.tv_cost,bean.getAmountOfMoney()+"元");
        //库区
        if (TextUtils.isEmpty(bean.getWarehouseArea())){
            holder.setText(R.id.tv_kuqu,"-");
        }else {
            holder.setText(R.id.tv_kuqu,bean.getWarehouseArea());
        }
//                .setText(R.id.consignee,bean.getConsignee())
//                .setText(R.id.consignee_phone,bean.getConsigneePhone())
//                .setText(R.id.consignee_card,bean.getConsigneeCarid());
        int waitPutCargoNum = bean.getTallyingTotal()-bean.getOutboundNumber();
        holder.setText(R.id.tv_put_num,"已提货: "+bean.getOutboundNumber()+"件 / 待提货:"+waitPutCargoNum+"件");
        if (bean.getOutStorageTime()>0){
            holder.setVisible(R.id.tv_complete_time,true);
            holder.setText(R.id.tv_complete_time, TimeUtils.date2Tasktime6(bean.getOutStorageTime()));
        }
        else
            holder.setGone(R.id.tv_complete_time,false);

        //运单状态
        switch (bean.getWaybillStatus()){
            case 0:
                holder.setText(R.id.tv_status,"暂存数据运单");
                break;
            case 1:
                holder.setText(R.id.tv_status,"运单录入完成");
                break;
            case 2:
                holder.setText(R.id.tv_status,"舱单核对完成");
                break;
            case 3:
                holder.setText(R.id.tv_status,"理货入库完成");
                break;
            case 4:
                holder.setText(R.id.tv_status,"已提货通知");
                break;
            case 5:
                holder.setText(R.id.tv_status,"已缴费");
                break;
            case 6:
                holder.setText(R.id.tv_status,"已提货");
                break;
            case 7:
                holder.setText(R.id.tv_status,"逾期待补费");
                break;
        }
        //控制按钮显示
        if (bean.getWaybillStatus() ==6){
            holder.setVisible(R.id.tv_put_num,false);
            holder.setVisible(R.id.tv_outStorage,true);
            holder.setGone(R.id.btn_outStorage,false);
        }else {
            holder.setGone(R.id.tv_outStorage,false);
            holder.setVisible(R.id.btn_outStorage,true);
            holder.setVisible(R.id.tv_put_num,true);
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
