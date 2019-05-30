package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;
import qx.app.freight.qxappfreight.utils.StringUtil;

public class UnloadBillAdapter extends BaseQuickAdapter<LoadingListBean.DataBean.ContentObjectBean, BaseViewHolder> {
    public UnloadBillAdapter(@Nullable List<LoadingListBean.DataBean.ContentObjectBean> data) {
        super(R.layout.item_unload_bill_info,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoadingListBean.DataBean.ContentObjectBean item) {
        helper.setText(R.id.tv_berth, StringUtil.toText(item.getLocation())).setText(R.id.tv_uld,"-").setText(R.id.tv_target,StringUtil.toText(item.getDest())).setText(R.id.tv_type,"C".equals(item.getType()) ? "cargo" : "mail")
                .setText(R.id.tv_weight,StringUtil.toText(item.getActWgt()));
    }
}
