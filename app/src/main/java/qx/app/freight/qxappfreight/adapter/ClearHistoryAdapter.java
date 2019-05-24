package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.InventoryQueryBean;
import qx.app.freight.qxappfreight.utils.TimeUtils;

public class ClearHistoryAdapter extends BaseQuickAdapter<InventoryQueryBean.RecordsBean, BaseViewHolder> {

    public ClearHistoryAdapter(@Nullable List<InventoryQueryBean.RecordsBean> list) {
        super(R.layout.item_clearstorage_list1, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, InventoryQueryBean.RecordsBean item) {
        //序号
        helper.setText(R.id.tv_nub, helper.getAdapterPosition()+"");
        //清库类型  (0：鲜活清库；1：全仓清库)
        if (0 == item.getTaskType())
            helper.setText(R.id.clear_storage_type, "鲜活清库");
        else if (1 == item.getTaskType())
            helper.setText(R.id.clear_storage_type, "全仓清库");

        //发起人
        helper.setText(R.id.tv_originator, "发起人:" + item.getCreateUserName());
        //发起时间
        helper.setText(R.id.tv_time, "发起时间:" + TimeUtils.getTime2(item.getCreateTime()));
    }
}
