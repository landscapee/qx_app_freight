package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;

public class DeliveryDetailAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public DeliveryDetailAdapter(@Nullable List<String> data) {
        super(R.layout.item_delivery_detail,data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {

    }
}
