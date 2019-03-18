package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;

public class FlightListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public FlightListAdapter(@Nullable List<String> data) {
        super(R.layout.item_flight_list_bagger, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
