package qx.app.freight.qxappfreight.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.utils.StringUtil;

public class CargoCabinAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public CargoCabinAdapter(List<String> data) {
        super(R.layout.item_cargo_cabin, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_cabin_num,helper.getAdapterPosition()+1+"H标准载量");
        helper.setText(R.id.tv_content, StringUtil.format(mContext, R.string.format_cargo_cabin,"15", "30000", "集装箱"));
    }
}
