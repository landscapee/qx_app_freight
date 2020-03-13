package qx.app.freight.qxappfreight.adapter;

import android.annotation.SuppressLint;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.utils.MapValue;

/**
 * created by swd
 * 2019/7/2 15:14
 */
public class RepeatWeightScooterAdapter extends BaseQuickAdapter<GetInfosByFlightIdBean, BaseViewHolder> {
        public RepeatWeightScooterAdapter(List<GetInfosByFlightIdBean> data) {
            super(R.layout.item_repeatweight_scooter, data);
        }

        @SuppressLint("StringFormatMatches")
        @Override
        protected void convert(BaseViewHolder helper, GetInfosByFlightIdBean item) {
            //板车类型~板车号
            helper.setText(R.id.allocate_address,  MapValue.getCarTypeValue(item.getScooterType()+"")+item.getScooterCode());
            //件数~重量~体积
            helper.setText(R.id.allocate_info, String.format(mContext.getString(R.string.format_allocate_info), item.getTotal(), item.getWeight(), item.getVolume()));

            String scooterType;
            helper.setTextColor(R.id.tv_scooter_type, mContext.getResources().getColor(R.color.gray_8f));
            switch (item.getReWeightFinish()) {
                case 0:
                    scooterType = "待复重";
                    break;
                case 1:
                    scooterType = "完成";
                    helper.setTextColor(R.id.tv_scooter_type, mContext.getResources().getColor(R.color.green));
                    break;
                case 2:
                    scooterType = "复重异常";
                    helper.setTextColor(R.id.tv_scooter_type, mContext.getResources().getColor(R.color.red));
                    break;
                default:
                    scooterType = "待复重";
            }
            //板车类型~板车号
            helper.setText(R.id.tv_scooter_type, scooterType);

        }


}
