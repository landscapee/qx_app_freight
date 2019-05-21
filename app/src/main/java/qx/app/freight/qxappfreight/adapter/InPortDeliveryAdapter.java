package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.InportDeliveryDetailActivity;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;

public class InPortDeliveryAdapter extends BaseQuickAdapter<TransportDataBase, BaseViewHolder> {
    public InPortDeliveryAdapter(@Nullable List<TransportDataBase> data) {
        super(R.layout.item_inport_delivery,data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, TransportDataBase bean) {
        baseViewHolder.setText(R.id.serial_number,"流水号:"+bean.getSerialNumber());
        baseViewHolder.setText(R.id.allocate_flightnumber,bean.getConsignee());
        baseViewHolder.setText(R.id.complete_num,""+bean.getOutboundNumber());
        baseViewHolder.setText(R.id.total_num,""+bean.getWaybillCount());
    }
}
