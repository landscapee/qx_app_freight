package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.UnLoadListBillBean;
import qx.app.freight.qxappfreight.utils.StringUtil;

public class UnloadBillAdapter extends BaseQuickAdapter<UnLoadListBillBean.DataBean.ContentObjectBean, BaseViewHolder> {
    public UnloadBillAdapter(@Nullable List<UnLoadListBillBean.DataBean.ContentObjectBean> data) {
        super(R.layout.item_unload_bill_info,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UnLoadListBillBean.DataBean.ContentObjectBean item) {
        helper.setText(R.id.tv_berth, StringUtil.toText(item.getPos())).setText(R.id.tv_uld,StringUtil.toText(item.getSerialInd())).setText(R.id.tv_target,StringUtil.toText(item.getDest())).setText(R.id.tv_type,StringUtil.toText(item.getType()))
                .setText(R.id.tv_weight,StringUtil.toText(item.getActWgt()));
    }
}
