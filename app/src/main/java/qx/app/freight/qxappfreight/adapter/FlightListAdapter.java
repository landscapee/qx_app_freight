package qx.app.freight.qxappfreight.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.FlightLuggageBean;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.widget.FlightInfoLayout;

public class FlightListAdapter extends BaseQuickAdapter<FlightLuggageBean, BaseViewHolder> {
    private Context mContext;

    public FlightListAdapter(@Nullable List<FlightLuggageBean> data, Context context) {
        super(R.layout.item_flight_list_bagger_2, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, FlightLuggageBean item) {
        helper.setText(R.id.flight_id, item.getFlightNo())
                .setText(R.id.tv_flight_type, item.getAircraftNo())
                .setText(R.id.tv_flight_place, item.getSeat())
                .setText(R.id.tv_arrive_time, String.format(mContext.getString(R.string.format_arrive_info), TimeUtils.date2Tasktime3(item.getScheduleTime()), TimeUtils.getDay((item.getScheduleTime()))));

        if (item.getFlightIndicator().equals("D")) {
            helper.setGone(R.id.iv_lock, false);
        } else {
            helper.setGone(R.id.iv_lock, true);
        }
        FlightInfoLayout layout = new FlightInfoLayout(mContext, item.getFlightCourseByAndroid());
        LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout llContainer = helper.getView(R.id.ll_flight_info_container);
        llContainer.removeAllViews();
        llContainer.addView(layout, paramsMain);


    }

}
