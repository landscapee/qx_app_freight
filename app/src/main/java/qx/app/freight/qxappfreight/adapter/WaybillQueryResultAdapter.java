package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.ListWaybillCodeBean;
import qx.app.freight.qxappfreight.utils.Tools;

public class WaybillQueryResultAdapter extends BaseQuickAdapter<ListWaybillCodeBean.DataBean, BaseViewHolder> {
    public WaybillQueryResultAdapter(@Nullable List<ListWaybillCodeBean.DataBean> data) {
        super(R.layout.item_search_result, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ListWaybillCodeBean.DataBean item) {
        helper.setText(R.id.tv_waybill_code, item.getWaybillCode());
        helper.setText(R.id.tv_num, item.getTotalNumber()+"");
        helper.setText(R.id.tv_inventory_num, item.getStorageNum()+"");
        helper.setText(R.id.tv_status, Tools.getWaybillStatus(item.getWaybillStatus()));
    }
}
