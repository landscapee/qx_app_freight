package qx.app.freight.qxappfreight.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.widget.FlightInfoLayout;

public class AllocateVehiclesAdapter extends BaseQuickAdapter<TransportDataBase, BaseViewHolder> {
    private Context context;
    public AllocateVehiclesAdapter(List<TransportDataBase> data, Context context) {
        super(R.layout.item_flight_list_bagger_2, data);
        this.context = context;
    }

    @SuppressLint("StringFormatMatches")
    @Override
    protected void convert(BaseViewHolder helper, TransportDataBase item) {
        helper.setText(R.id.flight_id,item.getFlightNo())
                .setText(R.id.tv_flight_type,item.getAircraftNo())
                .setText(R.id.tv_flight_place,item.getSeat())
                .setText(R.id.tv_arrive_time,String.format(mContext.getString(R.string.format_arrive_info), TimeUtils.date2Tasktime3(item.getEtd()) , TimeUtils.getDay((item.getEtd()))));

//        TextView arriveTime = helper.getView(R.id.tv_arrive_time);
//        Drawable img =context.getResources().getDrawable(R.mipmap.yu);
//        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
//        arriveTime.setCompoundDrawables(null, null, img, null);
        LinearLayout container = helper.getView(R.id.ll_flight_info_container);
        FlightInfoLayout layout = new FlightInfoLayout(mContext, item.getFlightCourseByAndroid());
        LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.removeAllViews();
        container.addView(layout, paramsMain);
//        if (item.getFlightCourseByAndroid() != null && item.getFlightCourseByAndroid().size() >1 ){
//            switch (helper.getItemViewType()){
//                case 2:
//                    helper.setText(R.id.tv_flight_1,item.getFlightCourseByAndroid().get(0))
//                            .setText(R.id.tv_flight_2,item.getFlightCourseByAndroid().get(1));
//                    break;
//                case 3:
//                    helper.setText(R.id.tv_flight_1,item.getFlightCourseByAndroid().get(0))
//                            .setText(R.id.tv_flight_2,item.getFlightCourseByAndroid().get(1))
//                            .setText(R.id.tv_flight_3,item.getFlightCourseByAndroid().get(2));
//                    break;
//                case 4:
//                    helper.setText(R.id.tv_flight_1,item.getFlightCourseByAndroid().get(0))
//                            .setText(R.id.tv_flight_2,item.getFlightCourseByAndroid().get(1))
//                            .setText(R.id.tv_flight_3,item.getFlightCourseByAndroid().get(2))
//                            .setText(R.id.tv_flight_4,item.getFlightCourseByAndroid().get(3));
//                    break;
//            }
//        }
    }

}
