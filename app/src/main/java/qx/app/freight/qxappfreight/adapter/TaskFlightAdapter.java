package qx.app.freight.qxappfreight.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.OutFieldTaskBean;
import qx.app.freight.qxappfreight.utils.MapValue;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.widget.FlightInfoLayout;

public class TaskFlightAdapter extends BaseQuickAdapter<OutFieldTaskBean, BaseViewHolder> {

    public TaskFlightAdapter(List<OutFieldTaskBean> list) {
        super(R.layout.item_task_flight, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, OutFieldTaskBean item) {
        if (item != null) {
            helper.setText(R.id.tv_flight_number, item.getFlightNo());
            helper.setText(R.id.tv_flight_type, item.getFlights().getAircraftNo());
            helper.setText(R.id.tv_flight_place, item.getFlights().getSeat());
            helper.setText(R.id.tv_arrive_time, StringUtil.format(mContext, R.string.format_arrive_info, TimeUtils.date2Tasktime3(item.getFlights().getScheduleTime()), TimeUtils.getDay(item.getFlights().getScheduleTime())));
            helper.setText(R.id.tv_num, item.getNum() + "个" + MapValue.getCarTypeValue(item.getTransfortType()));
            List<String> routes = item.getFlights().getRoute();
            FlightInfoLayout layout = new FlightInfoLayout(mContext, routes);
            LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout container = helper.getView(R.id.ll_flight_info_container);
            container.addView(layout, paramsMain);
            helper.setText(R.id.tv_begin, MapValue.getLocationValue(item.getBeginAreaType()) + item.getBeginAreaId());
            helper.setText(R.id.tv_end, MapValue.getLocationValue(item.getEndAreaType()) + item.getEndAreaId());
            ImageView ivFlag = helper.getView(R.id.iv_flag);
            if ("D".equals(item.getFlights().getMovement()))
                ivFlag.setImageResource(R.mipmap.li);
            else if ("A".equals(item.getFlights().getMovement()))
                ivFlag.setImageResource(R.mipmap.jin);

        }


    }
}
