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

        }


}
