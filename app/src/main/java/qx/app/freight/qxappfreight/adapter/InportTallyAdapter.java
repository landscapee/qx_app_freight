package qx.app.freight.qxappfreight.adapter;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.FFMActivity;
import qx.app.freight.qxappfreight.activity.InPortTallyActivity;
import qx.app.freight.qxappfreight.bean.InPortTallyEntity;
import qx.app.freight.qxappfreight.listener.InportTallyInterface;

public class InportTallyAdapter extends BaseQuickAdapter<InPortTallyEntity, BaseViewHolder> {
    private InportTallyInterface listener;

    public InportTallyAdapter(List<InPortTallyEntity> list) {
        super(R.layout.item_task_manifest, list);
    }

    public void setInportTallyListener(InportTallyInterface listener){
        this.listener = listener;
    }


    @Override
    protected void convert(BaseViewHolder helper, InPortTallyEntity item) {
        helper.setText(R.id.tv_flight_number, item.getFlightName()).setText(R.id.tv_arrive_time, String.format(mContext.getString(R.string.format_arrive_info), item.getDateHM(), item.getDateDay()));
        helper.setText(R.id.tv_type, "进港理货");
        TextView tvType = helper.getView(R.id.tv_type);
        Button btnFfm = helper.getView(R.id.btn_ffm);
        tvType.setOnClickListener(v -> {
            listener.toDetail(item);
        });
        btnFfm.setOnClickListener(v -> {
            listener.toFFM(item);
        });
    }
}
