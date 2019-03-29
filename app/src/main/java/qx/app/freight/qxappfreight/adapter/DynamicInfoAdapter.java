package qx.app.freight.qxappfreight.adapter;

import android.graphics.Color;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.FlightBean;
import qx.app.freight.qxappfreight.utils.TimeUtils;

public class DynamicInfoAdapter extends BaseQuickAdapter<FlightBean.FlightsBean, BaseViewHolder> {
    @BindView(R.id.tv_flight_id)
    TextView tvFlightId;
    @BindView(R.id.tv_flight_route)
    TextView tvFlightRoute;
    @BindView(R.id.tv_plant_time)
    TextView tvPlantTime;
    @BindView(R.id.tv_actual_time)
    TextView tvActualTime;
    @BindView(R.id.tv_state)
    TextView tvState;
    private String mTag;

    public DynamicInfoAdapter(List<FlightBean.FlightsBean> list, String tag) {
        super(R.layout.item_dynamic_info, list);
        mTag = tag;
    }

    @Override
    protected void convert(BaseViewHolder helper, FlightBean.FlightsBean item) {
        //航线
        helper.setText(R.id.tv_flight_route, item.getRoutes());
        //计划起飞时间 scheduleTime
        helper.setText(R.id.tv_plant_time, TimeUtils.date2Tasktime3(item.getScheduleTime()));
        //实际起飞时间 //如果movement 是A（进港）  就是ata  |  是D （离岗） 就是atd
        if ("A".equals(item.getMovement())) {
            helper.setText(R.id.tv_actual_time, TimeUtils.date2Tasktime3(item.getAta()));
            //航班号
            helper.setText(R.id.tv_flight_id, item.getFlightNo());
            helper.setTextColor(R.id.tv_flight_id, Color.parseColor("#2E81FD"));
        } else if ("D".equals(item.getMovement())) {
            helper.setText(R.id.tv_actual_time, TimeUtils.date2Tasktime3(item.getAtd()));
            //航班号
            helper.setText(R.id.tv_flight_id, item.getFlightNo());
            helper.setTextColor(R.id.tv_flight_id, Color.parseColor("#1FC41E"));
        }
        //状态
        helper.setText(R.id.tv_state, item.getFlightStatus());

    }
}
