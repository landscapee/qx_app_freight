package qx.app.freight.qxappfreight.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.utils.TimeUtils;

public class BaggerListAdapter extends BaseQuickAdapter<TransportTodoListBean, BaseViewHolder> {

    private OnDeleteClickLister mDeleteClickListener;

    public BaggerListAdapter(List<TransportTodoListBean> data) {
        super(R.layout.item_bagger_list, data);
    }

    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }



    @Override
    protected void convert(BaseViewHolder helper, TransportTodoListBean item) {
        String scooterName;
        if (item.getDtoType()==1){
            scooterName = "大板";
        }else {
            scooterName = "小板";
        }
        //板车类型~板车号
        helper.setText(R.id.allocate_address, String.format(mContext.getString(R.string.format_allocate_ddress_info), scooterName, item.getTpScooterCode()));

        helper.setText(R.id.allocate_flightnumber,item.getTpFlightNumber())
                .setText(R.id.allocate_machinenumber,item.getTpFlightLocate())
                .setText(R.id.tv_plan_time,String.format(mContext.getString(R.string.format_arrive_info), TimeUtils.date2Tasktime3(item.getTpFlightTime()) , TimeUtils.getDay((item.getTpFlightTime()))));



        View viewDelete = helper.getView(R.id.ll_delete);
        viewDelete.setTag(helper.getAdapterPosition());
        if (!viewDelete.hasOnClickListeners()) {
            viewDelete.setOnClickListener(v -> {
                if (mDeleteClickListener != null) {
                    mDeleteClickListener.onDeleteClick(v, (Integer) v.getTag());
                }
            });
        }
    }


    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }
}
