package qx.app.freight.qxappfreight.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;

public class TaskPutCargoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public TaskPutCargoAdapter(List<String> list) {
        super(R.layout.item_task_put_cargo, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_flight_number, item);
    }
}
