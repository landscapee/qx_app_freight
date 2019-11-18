package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;

public class ChooseFlightAdapter extends BaseQuickAdapter<GetInfosByFlightIdBean, BaseViewHolder> {

    public ChooseFlightAdapter(@Nullable List<GetInfosByFlightIdBean> data) {
        super(R.layout.item_choose_flight, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetInfosByFlightIdBean item) {
        helper.setText(R.id.tv_name,item.getFlightNo());
    }
}
