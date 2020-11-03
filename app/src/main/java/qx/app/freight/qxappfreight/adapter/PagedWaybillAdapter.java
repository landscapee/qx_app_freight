package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;

/**
 * Created by zzq On 2020/7/3 17:04 & Copyright (C), 青霄科技
 *
 * @文档说明: 翻页样式显示运单数据适配器
 */
public class PagedWaybillAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public PagedWaybillAdapter(@Nullable List<String> data) {
        super(R.layout.item_paged_waybill,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_info,item);
    }
}
