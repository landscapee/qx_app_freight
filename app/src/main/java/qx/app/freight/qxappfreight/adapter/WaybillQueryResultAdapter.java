package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;

public class WaybillQueryResultAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public WaybillQueryResultAdapter(@Nullable List<String> data) {
        super(R.layout.item_search_result, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_result, item);
    }
}
