package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.CargoReportHisBean;
import qx.app.freight.qxappfreight.utils.TimeUtils;

public class FlightListCargoDoneAdapter extends BaseQuickAdapter<CargoReportHisBean, BaseViewHolder> {
    public FlightListCargoDoneAdapter(@Nullable List<CargoReportHisBean> data) {
        super(R.layout.item_flight_list_bagger_done_2, data);
//        super(data);
//        addItemType(2, R.layout.item_flight_list_bagger_done_2);
//        addItemType(3, R.layout.item_flight_list_bagger_done_3);
//        addItemType(4, R.layout.item_flight_list_bagger_done_4);
    }

    @Override
    protected void convert(BaseViewHolder helper, CargoReportHisBean item) {
        helper.setText(R.id.flight_id, item.getFlightNo())
//                .setText(R.id.tv_flight_type, item.getTpFlightType() + "")
//                .setText(R.id.tv_flight_place, item.getBaggageSubTerminal() + "")
                .setText(R.id.tv_arrive_time, String.format(mContext.getString(R.string.format_arrive_info), TimeUtils.date2Tasktime3(item.getCreateTime()), TimeUtils.getDay((item.getCreateTime()))));

//        if (TextUtils.isEmpty(item.getLuggageScanningUser())){
//            helper.setGone(R.id.iv_lock,false);
//        }else {
//            helper.setGone(R.id.iv_lock,true);
////        }
        if (item.getFlightIndicator().equals("D")) {
            helper.setGone(R.id.iv_lock, false);
        } else {
            helper.setGone(R.id.iv_lock, true);
        }
//        if (item.getFlightCourseByAndroid() != null && item.getFlightCourseByAndroid().size() >1 ){
//            switch (helper.getItemViewType()){
//                case 2:
//                    helper.setText(R.id.tv_flight_1,item.getFlightIndicator())
//                            .setText(R.id.tv_flight_2,item.getFlightIndicator());
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
