package qx.app.freight.qxappfreight.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.model.WaybillsBean;

/**
 * created by swd
 * 2019/7/3 17:20
 */
public class RepeatWeightManifestAdapter extends BaseQuickAdapter<WaybillsBean, BaseViewHolder> {

    public RepeatWeightManifestAdapter(List<WaybillsBean> data) {
        super(R.layout.item_repeatweight_manifest, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WaybillsBean item) {
        helper.setText(R.id.waybill_code,item.getWaybillId())
                .setText(R.id.tv_name,item.getCargoCn())
                .setText(R.id.tv_num, item.getTotalnum()+"")
                .setText(R.id.tv_weight ,item.getTotalweight()+"")
                .setText(R.id.tv_volume,item.getTotalVolume()+"")
                .setText(R.id.tv_code,"")
                .setText(R.id.tv_mark,item.getRemark());
    }

}
