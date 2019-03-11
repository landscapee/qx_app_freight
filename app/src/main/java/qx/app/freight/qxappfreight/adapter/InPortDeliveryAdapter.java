package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;

public class InPortDeliveryAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public InPortDeliveryAdapter(@Nullable List<String> data) {
        super(R.layout.item_inport_delivery,data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String str) {

    }
}
