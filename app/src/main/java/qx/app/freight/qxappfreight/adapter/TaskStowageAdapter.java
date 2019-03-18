package qx.app.freight.qxappfreight.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;

public class TaskStowageAdapter extends BaseQuickAdapter<TransportListBean, BaseViewHolder> {
    public TaskStowageAdapter(List<TransportListBean> list) {
        super(R.layout.item_task_stowage, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, TransportListBean item) {
        helper.setText(R.id.tv_flight_number, item.getFlightNo());
        helper.setText(R.id.tv_arrive_time, StringUtil.format(mContext,R.string.format_arrive_info,TimeUtils.date2Tasktime3(item.getEtd()),TimeUtils.getDay(item.getEtd())));
        helper.setText(R.id.tv_waybill_count,"运单件数"+item.getTotalNumberPackages());
    }
}
