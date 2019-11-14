package qx.app.freight.qxappfreight.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.FlightCabinInfo;

public class BerthAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public BerthAdapter(List <String> list) {
        super(R.layout.item_cabin, list);
    }

    @Override
    protected void convert(BaseViewHolder helper,String item) {
        //名字
        helper.setText(R.id.tv_cabin, item);
        //文件
    }
}
