package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.ListWaybillCodeBean;

public class WaybillQueryResultAdapter extends BaseQuickAdapter<ListWaybillCodeBean.DataBean, BaseViewHolder> {
    public WaybillQueryResultAdapter(@Nullable List<ListWaybillCodeBean.DataBean> data) {
        super(R.layout.item_search_result, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ListWaybillCodeBean.DataBean item) {
        helper.setText(R.id.tv_result, item.getWaybillCode());
    }
}
