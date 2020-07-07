package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordSubmitNewEntity;

/**
 * Created by zzq On 2020/7/6 11:48 & Copyright (C), 青霄科技
 *
 * @文档说明: 翻页模式展示运单数据适配器
 */
public class InboundSortingAdapter extends BaseQuickAdapter<InWaybillRecordSubmitNewEntity.SingleLineBean, BaseViewHolder> {
    private OnChildClickListener onChildClickListener;

    public InboundSortingAdapter(@Nullable List<InWaybillRecordSubmitNewEntity.SingleLineBean> data) {
        super(R.layout.item_inbound_sorting_waybill, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InWaybillRecordSubmitNewEntity.SingleLineBean item) {
        helper.setText(R.id.tv_waybill_code, item.getWaybillCode())
                .setText(R.id.tv_number, String.valueOf(item.getTotalNumber()))
                .setText(R.id.tv_weight, String.valueOf(item.getTotalWeight()))
                .setText(R.id.tv_select_number, String.valueOf(item.getTallyingTotal()))
                .setText(R.id.tv_select_weight, String.valueOf(item.getTallyingWeight()))
                .setText(R.id.tv_store_area, TextUtils.isEmpty(item.getWarehouseAreaDisplay()) ? "--" : item.getWarehouseAreaDisplay())
                .setText(R.id.tv_wrong_transport, (item.getStray() == 0) ? "否" : "是")
                .setText(R.id.tv_goods_name, TextUtils.isEmpty(item.getCommodityName()) ? "--" : item.getCommodityName())
                .setText(R.id.tv_goods_special_code, TextUtils.isEmpty(item.getSpecialCode()) ? "--" : item.getSpecialCode())
                .setText(R.id.tv_goods_remark, item.getRemark())
                .setText(R.id.tv_accept_user, TextUtils.isEmpty(item.getConsignee()) ? "--" : item.getConsignee());
        Button btnArrival = helper.getView(R.id.btn_ticket_commit);
        if (item.getAllArrivedFlag() == 0) {
            btnArrival.setEnabled(true);
            btnArrival.setOnClickListener(v -> onChildClickListener.onChildClicked(helper.getAdapterPosition(), false));
        } else {
            btnArrival.setEnabled(false);
        }
        Button btnDelete = helper.getView(R.id.btn_delete);
        if (item.isCanModify()) {
            btnDelete.setEnabled(true);
            btnDelete.setOnClickListener(v -> onChildClickListener.onChildClicked(helper.getAdapterPosition(), true));
        } else {
            btnDelete.setEnabled(false);
        }
    }

    public interface OnChildClickListener {
        void onChildClicked(int pos, boolean isDelete);
    }

    public void setOnChildClickListener(OnChildClickListener onChildClickListener) {
        this.onChildClickListener = onChildClickListener;
    }
}
