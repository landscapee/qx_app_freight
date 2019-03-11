package qx.app.freight.qxappfreight.adapter;

import android.annotation.SuppressLint;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.utils.TimeUtils;

public class AllocateVehiclesAdapter extends BaseQuickAdapter<GetInfosByFlightIdBean, BaseViewHolder> {
    public AllocateVehiclesAdapter(List<GetInfosByFlightIdBean> data) {
        super(R.layout.item_allocate_vehicles, data);
    }

    @SuppressLint("StringFormatMatches")
    @Override
    protected void convert(BaseViewHolder helper, GetInfosByFlightIdBean item) {
        String scooterName;
        if (item.getScooterType()==1){
            scooterName = "大板";
        }else {
            scooterName = "小板";
        }
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
        helper.setText(R.id.allocate_address, String.format(mContext.getString(R.string.format_allocate_ddress_info), scooterName, item.getScooterCode()));
        //件数~重量~体积
        helper.setText(R.id.allocate_info, String.format(mContext.getString(R.string.format_allocate_info), item.getTotal(), item.getWeight(), item.getVolume()));
        //航班号
        helper.setText(R.id.allocate_flightnumber,item.getFlightNo());
        //机位号
        helper.setText(R.id.allocate_machinenumber, item.getAircraftNo());
        //预计起飞时间~仓位
        //航班预计起飞时间
        helper.setText(R.id.allocate_time_info, String.format(mContext.getString(R.string.format_arrive_info), TimeUtils.date2Tasktime3(item.getStd()) , 04)+" "+item.getSuggestRepository()+"舱位");
//        helper.setText(R.id.allocate_time_info, item.getStd()+"   "+item.getSuggestRepository());
//        helper.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(mContext, AllocaaateScanActivity.class);
//            mContext.startActivity(intent);
//        });
    }
}
