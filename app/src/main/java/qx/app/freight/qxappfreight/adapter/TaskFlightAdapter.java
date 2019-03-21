package qx.app.freight.qxappfreight.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.AcceptTerminalTodoBean;
import qx.app.freight.qxappfreight.bean.response.OutFieldFlightBean;
import qx.app.freight.qxappfreight.bean.response.OutFieldTaskBean;
import qx.app.freight.qxappfreight.utils.MapValue;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;

public class TaskFlightAdapter extends BaseQuickAdapter<OutFieldTaskBean, BaseViewHolder> {

    public TaskFlightAdapter(List<OutFieldTaskBean> list) {
        super(R.layout.item_task_flight, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, OutFieldTaskBean item) {
        if (item!= null){
            helper.setText(R.id.tv_flight_number, item.getFlightNo());
            helper.setText(R.id.tv_flight_type, item.getFlights().getAircraftNo());
            helper.setText(R.id.tv_flight_place, item.getFlights().getSeat());
            helper.setText(R.id.tv_arrive_time, StringUtil.format(mContext,R.string.format_arrive_info, TimeUtils.date2Tasktime3(item.getFlights().getEtd()),TimeUtils.getDay(item.getFlights().getEtd())));
            helper.setText(R.id.tv_num,item.getNum()+"个"+ MapValue.getCarTypeValue(item.getTransfortType()));

        }


    }
}
