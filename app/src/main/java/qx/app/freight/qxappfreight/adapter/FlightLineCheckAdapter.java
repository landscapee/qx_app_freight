package qx.app.freight.qxappfreight.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.FlightLineBean;

/**
 * Created by zzq On 2020/6/30 18:18 & Copyright (C), 青霄科技
 *
 * @文档说明: 进港理货页面展示航段的Tab适配器
 */
public class FlightLineCheckAdapter extends BaseQuickAdapter<FlightLineBean, BaseViewHolder> {
    public FlightLineCheckAdapter(@Nullable List<FlightLineBean> data) {
        super(R.layout.item_flight_line, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FlightLineBean item) {
        TextView textView = helper.getView(R.id.tv_flight_line);
        textView.setText(item.getLine());
        if (item.isCheck()) {
            textView.setBackgroundColor(Color.RED);
            textView.setTextColor(Color.WHITE);
        } else {
            textView.setBackgroundColor(Color.GRAY);
            textView.setTextColor(Color.BLACK);
        }
    }
}
