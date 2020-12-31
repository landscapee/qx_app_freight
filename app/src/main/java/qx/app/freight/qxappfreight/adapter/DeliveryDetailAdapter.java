package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;

public class DeliveryDetailAdapter extends BaseQuickAdapter <WaybillsBean, BaseViewHolder> {
    private DeliveryDetailInterface listener;

    public DeliveryDetailAdapter(@Nullable List <WaybillsBean> data) {
        super(R.layout.item_delivery_detail, data);
    }

    public void setDeliveryDetailInterface(DeliveryDetailInterface listener) {
        this.listener = listener;
    }

    @Override
    protected void convert(BaseViewHolder holder, WaybillsBean bean) {
        holder.setText(R.id.waybill_code, bean.getWaybillCode());
        Button btnOutRecords = holder.getView(R.id.btn_records);

        holder.setText(R.id.tv_put_num, "待出库: " + bean.getStorageNumber() + "件 / 已出库:" + bean.getOutboundNumber() + "件");

        holder.setText(R.id.total_info, "录单: " + bean.getTotalNumber() + "件 / 分拣:" + bean.getTallyingTotal() + "件");
        //超重
        holder.setText(R.id.tv_overweight, String.format(mContext.getString(R.string.format_goods_inport)
                , bean.getOverWieghtCount() + ""
                , bean.getOverWieght() + ""));

        //库区
        holder.setText(R.id.tv_kuqu, StringUtil.toText(bean.getRqName(), "-"));


        if (bean.getOutStorageTime() > 0) {
            holder.setVisible(R.id.tv_complete_time, true);
            holder.setText(R.id.tv_complete_time, TimeUtils.date2Tasktime6(bean.getOutStorageTime()));
        } else {
            holder.setGone(R.id.tv_complete_time, false);
        }

        //运单状态
        switch (bean.getWaybillStatus()) {
            case 0:
                holder.setText(R.id.tv_status, "暂存数据运单");
                break;
            case 1:
                holder.setText(R.id.tv_status, "运单录入完成");
                break;
            case 2:
                holder.setText(R.id.tv_status, "舱单核对完成");
                break;
            case 3:
                holder.setText(R.id.tv_status, "理货入库完成");
                break;
            case 4:
                holder.setText(R.id.tv_status, "已提货通知");
                break;
            case 5:
                holder.setText(R.id.tv_status, "已缴费");
                break;
            case 6:
                holder.setText(R.id.tv_status, "已提货");
                break;
            case 7:
                holder.setText(R.id.tv_status, "逾期待补费");
                break;
        }
        //控制按钮显示
        if (bean.getWaybillStatus() == 6 || bean.getOverweightCharge() > 0 || bean.getDefermentCharge() > 0) {
//            holder.setVisible(R.id.tv_put_num,false);
            if (bean.getOverweightCharge() > 0) {
                holder.setText(R.id.tv_status, "超重待补费");
                holder.setText(R.id.tv_outStorage, "");
            } else if (bean.getDefermentCharge() > 0) {
                holder.setText(R.id.tv_status, "逾期待补费");
                holder.setText(R.id.tv_outStorage, "");
            } else {
                holder.setText(R.id.tv_outStorage, "已出库");
            }

            btnOutRecords.setVisibility(View.VISIBLE);
            holder.setVisible(R.id.tv_outStorage, true);
            holder.setGone(R.id.btn_outStorage, false);
            holder.setGone(R.id.btn_overweight, false);
            holder.setGone(R.id.btn_forklift, false);

        } else {
            if (bean.getOutboundNumber() > 0) {
                btnOutRecords.setVisibility(View.VISIBLE);
            } else {
                btnOutRecords.setVisibility(View.GONE);
            }
            holder.setGone(R.id.tv_outStorage, false);
            holder.setVisible(R.id.btn_outStorage, true);
            if (bean.getStorageNumber()>0){
                holder.setGone(R.id.btn_overweight, true);
            }else {
                holder.setGone(R.id.btn_overweight, false);
            }

            /**
             * 2020.12.9 与PC 保持一致 取消叉车费用 按钮 ——谭斌
             *     holder.setGone(R.id.btn_forklift,true);
             */
        }

        holder.setGone(R.id.tv_overweight_money, false);

        Button btnForklift = holder.getView(R.id.btn_forklift);
        btnForklift.setOnClickListener(v -> {
            listener.forkliftCost(holder.getAdapterPosition());
        });
        Button btnOutStorage = holder.getView(R.id.btn_outStorage);
        btnOutStorage.setOnClickListener(v ->
                listener.outStorage(holder.getAdapterPosition(), bean.getId(), bean.getOutStorageUser())
        );
        Button btnOverweight = holder.getView(R.id.btn_overweight);
        btnOverweight.setOnClickListener(v ->
                listener.inputOverWeight(holder.getAdapterPosition())
        );

        btnOutRecords.setOnClickListener(v ->
                listener.outStorageRecords(holder.getAdapterPosition())
        );
    }

    public interface DeliveryDetailInterface {
        void outStorage(int position, String id, String outStorageUser);

        void inputOverWeight(int position);

        void forkliftCost(int position);

        void outStorageRecords(int position);
    }
}
