package qx.app.freight.qxappfreight.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.WeightWayBillBean;
import qx.app.freight.qxappfreight.utils.StringUtil;

/**
 * created by swd
 * 2019/8/2 12:18
 */
public class WeightWayBillBeanAdapter extends BaseQuickAdapter<WeightWayBillBean, BaseViewHolder> {

    public WeightWayBillBeanAdapter(@Nullable List<WeightWayBillBean> data) {
        super(R.layout.item_weight_waybill, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WeightWayBillBean item) {


        String waybillCode = StringUtil.toText(item.getWaybillCode(),"-");
        Boolean isMail = false;
        if(waybillCode.indexOf("DN")==0){
            isMail = true;
        }

        helper.setText(R.id.tv_waybill_code,waybillCode)
                .setTextColor(R.id.tv_waybill_code, isMail? Color.parseColor("#FF0000"):Color.parseColor("#000000"))
                .setText(R.id.tv_cargo_name, StringUtil.toText(item.getCargoCn(),"-"))
                .setText(R.id.tv_num,item.getNumber()+"")
                .setText(R.id.tv_uld_name,item.getUldName())
                .setText(R.id.tv_weight,item.getWeight()+"");

    }
}
