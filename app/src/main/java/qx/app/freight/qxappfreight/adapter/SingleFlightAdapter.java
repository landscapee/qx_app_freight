package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.SearchFlightInfoBean;

/**
 * Created by zzq On 2020/6/10 17:19 & Copyright (C), 青霄科技
 *
 * @文档说明: 根据搜索关键字显示搜索结果列表的适配器
 */
public class SingleFlightAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public SingleFlightAdapter(@Nullable List<String> data) {
        super(R.layout.item_flight_info, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_flight, item);
    }
}
