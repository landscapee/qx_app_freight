package qx.app.freight.qxappfreight.adapter;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.utils.MapValue;
import qx.app.freight.qxappfreight.utils.TimeUtils;

/**
 * created by swd
 * 2019/7/4 16:02
 */
public class AllocaaateHistoryAdapter extends BaseQuickAdapter<GetInfosByFlightIdBean, BaseViewHolder> {
    public AllocaaateHistoryAdapter(List<GetInfosByFlightIdBean> data) {
        super(R.layout.item_allocate_vehicles, data);
    }

    @SuppressLint("StringFormatMatches")
    @Override
    protected void convert(BaseViewHolder helper, GetInfosByFlightIdBean item) {
        String scooterType;
        switch (item.getReWeightFinish()) {
            case 0:
                scooterType = "待复重";
                break;
            case 1:
                scooterType = "完成";
                break;
            case 2:
                scooterType = "复重异常";
                break;
            default:
                scooterType = "待复重";
        }
        //板车类型~板车号
        helper.setText(R.id.tv_scooter_type, scooterType);
        //板车类型~板车号
        helper.setText(R.id.allocate_address,  MapValue.getCarTypeValue(item.getScooterType()+"")+item.getScooterCodeName());
        //件数~重量~体积
        helper.setText(R.id.allocate_info, String.format(mContext.getString(R.string.format_allocate_info), item.getTotal(), item.getWeight(), item.getVolume()));
        //航班号
        helper.setText(R.id.allocate_flightnumber,item.getFlightNo());
        //机位号
        helper.setText(R.id.allocate_machinenumber, item.getSeat());
        //复重完成时间
        helper.setText(R.id.tv_reweight_time, TimeUtils.getHMDay(item.getReWeighedTime()));

        //预计起飞时间~仓位
        //航班预计起飞时间
        String mString;
        if (TextUtils.isEmpty(item.getSuggestRepository())){
            mString ="-";
        }else {
            mString = item.getSuggestRepository();
        }
        helper.setText(R.id.allocate_time_info, String.format(mContext.getString(R.string.format_arrive_info), TimeUtils.date2Tasktime3(item.getScheduleTime()) ,  TimeUtils.getDay((item.getScheduleTime())))+" "+mString+"舱位");
//        helper.setText(R.id.allocate_time_info, item.getStd()+"   "+item.getSuggestRepository());
//        helper.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(mContext, AllocaaateScanActivity.class);
//            mContext.startActivity(intent);
//        });
    }
}