package qx.app.freight.qxappfreight.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.DeclareItem;

public class DeliveryVerifyAdapter extends BaseQuickAdapter<DeclareItem, BaseViewHolder> {

    public DeliveryVerifyAdapter(List<DeclareItem> mList){
        super(R.layout.item_delivery_verify, mList);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeclareItem item) {
        helper.setText(R.id.tv_sort, helper.getAdapterPosition() + 1 + "")
                .setText(R.id.tv_name, item.getCargoCn())
                .setText(R.id.tv_number, item.getNumber() + "")
                .setText(R.id.tv_bigness, item.getVolume() + "")
                .setText(R.id.tv_type, item.getPackagingType()[0]);
    }
}
