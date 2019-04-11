package qx.app.freight.qxappfreight.adapter;

import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.listener.InportTallyInterface;
import qx.app.freight.qxappfreight.utils.TimeUtils;

public class InportTallyAdapter extends BaseQuickAdapter<TransportListBean, BaseViewHolder> {
    private InportTallyInterface listener;

    public InportTallyAdapter(List<TransportListBean> list) {
        super(R.layout.item_task_manifest, list);
    }

    public void setInportTallyListener(InportTallyInterface listener) {
        this.listener = listener;
    }


    @Override
    protected void convert(BaseViewHolder helper, TransportListBean item) {
        helper.setText(R.id.tv_flight_number, item.getFlightNo());
        helper.setText(R.id.tv_arrive_time, TimeUtils.date2Tasktime3(item.getEtd()) + "(" + TimeUtils.getDay(item.getEtd()) + ")");
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
