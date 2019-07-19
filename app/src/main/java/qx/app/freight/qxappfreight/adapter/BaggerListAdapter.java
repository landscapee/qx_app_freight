package qx.app.freight.qxappfreight.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.utils.MapValue;
import qx.app.freight.qxappfreight.utils.StringUtil;
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
        //板车类型~板车号
        helper.setText(R.id.allocate_address, String.format(mContext.getString(R.string.format_allocate_ddress_info), MapValue.getCarTypeValue(item.getTpScooterType()), item.getTpScooterCode()));

        //件数~重量~体积
        helper.setText(R.id.allocate_info, String.format(mContext.getString(R.string.format_allocate_info), StringUtil.toText(item.getTpCargoNumber()+"","0") ,StringUtil.toText(item.getTpCargoWeight()+"","0"), StringUtil.toText(item.getTpCargoVolume()+"","0")));

        //是否隐藏国际图标
        if (item.getFlightIndicator().equals("I")){
            helper.setVisible(R.id.iv_international,true);
        }else {
            helper.setVisible(R.id.iv_international,false);
        }

        helper.setText(R.id.allocate_flightnumber,item.getFlightNo())
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
