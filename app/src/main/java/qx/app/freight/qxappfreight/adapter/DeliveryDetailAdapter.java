package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;
import qx.app.freight.qxappfreight.utils.StringUtil;
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

        int waitPutCargoNum = bean.getTallyingTotal()-bean.getOutboundNumber();
        holder.setText(R.id.tv_put_num,"待出库: "+waitPutCargoNum+"件 / 已出库:"+bean.getOutboundNumber()+"件");

        holder.setText(R.id.total_info,"录单: "+bean.getTotalNumber()+"件 / 分拣:"+bean.getTallyingTotal()+"件");
        //录单 件数 - 重量
//        holder.setText(R.id.total_info, String.format(mContext.getString(R.string.format_goods_inport)
//                ,bean.getTotalNumberPackages()+""
//                ,bean.getTotalWeight()));
        //超重
        holder.setText(R.id.tv_overweight, String.format(mContext.getString(R.string.format_goods_inport)
                ,bean.getOverWieghtCount()+""
                ,bean.getOverWieght()+""));

        //分拣件数 - 重量
//        holder.setText(R.id.tallying_info,bean.getTallyingTotal()+"件");
        //预期费用
//        holder.setText(R.id.tv_cost,bean.getAmountOfMoney()+"元");
        //库区
        holder.setText(R.id.tv_kuqu, StringUtil.toText(bean.getRqName(),"-"));

//                .setText(R.id.consignee,bean.getConsignee())
//                .setText(R.id.consignee_phone,bean.getConsigneePhone())
//                .setText(R.id.consignee_card,bean.getConsigneeCarid());


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
        if (bean.getWaybillStatus() ==6||bean.getOverweightCharge() > 0||bean.getDefermentCharge() > 0){
//            holder.setVisible(R.id.tv_put_num,false);
            if (bean.getOverweightCharge() > 0){
                holder.setText(R.id.tv_status,"超重待补费");
                holder.setText(R.id.tv_outStorage,"");
            }
            else if (bean.getDefermentCharge() > 0){
                holder.setText(R.id.tv_status,"逾期待补费");
                holder.setText(R.id.tv_outStorage,"");
            }
            else
                holder.setText(R.id.tv_outStorage,"已出库");

            holder.setVisible(R.id.tv_outStorage,true);
            holder.setGone(R.id.btn_outStorage,false);
            holder.setGone(R.id.btn_overweight,false);
            holder.setGone(R.id.btn_forklift,false);

        }else {
            holder.setGone(R.id.tv_outStorage,false);
            holder.setVisible(R.id.btn_outStorage,true);
            holder.setGone(R.id.btn_overweight,true);
            holder.setGone(R.id.btn_forklift,true);
//            holder.setVisible(R.id.tv_put_num,true);
        }

//        if (bean.getDefermentCharge() > 0){
//            holder.setVisible(R.id.tv_overweight_money,true);
//            holder.setText(R.id.tv_overweight_money,"待收费:"+bean.getOverweightCharge()+"元");
//        }
//        else
            holder.setGone(R.id.tv_overweight_money,false);

        Button btnForklift = holder.getView(R.id.btn_forklift);
        btnForklift.setOnClickListener(v -> {
            listener.forkliftCost(holder.getAdapterPosition());
        });
        Button btnOutStorage = holder.getView(R.id.btn_outStorage);
        btnOutStorage.setOnClickListener(v ->
                listener.outStorage(holder.getAdapterPosition(),bean.getId(),bean.getOutStorageUser())
        );
        Button btnOverweight = holder.getView(R.id.btn_overweight);
        btnOverweight.setOnClickListener(v ->
                listener.inputOverWeight(holder.getAdapterPosition())
        );
    }

    public interface DeliveryDetailInterface{
        void outStorage(int position,String id,String outStorageUser);
        void inputOverWeight(int position);
        void forkliftCost(int position);
    }
}
