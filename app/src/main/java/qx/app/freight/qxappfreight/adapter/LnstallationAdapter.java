package qx.app.freight.qxappfreight.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.utils.TimeUtils;

public class LnstallationAdapter extends BaseQuickAdapter<TransportDataBase, BaseViewHolder> {

    public LnstallationAdapter(List<TransportDataBase> list) {
        super(R.layout.item_task_lnstalla_tion, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, TransportDataBase item) {
        helper.setText(R.id.flight_id, item.getFlightNo());
        helper.setText(R.id.tv_flight_type, item.getSeat());
//        helper.setText(R.id.tv_flight_type, item.getVersion());
        helper.setText(R.id.tv_flight_task_type, item.getVersion());
        helper.setText(R.id.tv_arrive_time, String.format(mContext.getString(R.string.format_arrive_info), TimeUtils.date2Tasktime3(item.getEtd()), TimeUtils.getDay((item.getEtd()))));
        helper.setText(R.id.tv_flight_1, item.getFlightCourseByAndroid().get(0))
                .setText(R.id.tv_flight_2, item.getFlightCourseByAndroid().get(1));
    }
}
