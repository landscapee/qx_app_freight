package qx.app.freight.qxappfreight.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.utils.TimeUtils;

/**
 * 拉货上报信息列表适配器
 */
public class PullGoodsInfoAdapter extends BaseQuickAdapter<TransportTodoListBean, BaseViewHolder> {

    private PullGoodsInfoAdapter.OnDeleteClickLister mDeleteClickListener;

    public PullGoodsInfoAdapter(List<TransportTodoListBean> data) {
        super(R.layout.item_pull_goods_info, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TransportTodoListBean item) {
        helper.setText(R.id.tv_board_number, "大板 000"+helper.getAdapterPosition());
        View viewDelete = helper.getView(R.id.ll_delete);
        //件数 - 重量 - 体积
        helper.setText(R.id.tv_goods_info,String.format(mContext.getString(R.string.format_goods_info)
        ,item.getTpCargoNumber()
        ,item.getTpCargoWeight()
        ,item.getTpCargoVolume()));
        //航班号
        helper.setText(R.id.tv_flight_number,item.getTpFlightNumber());
        //机位号
        helper.setText(R.id.tv_place_number,item.getTpFlightLocate());
        // 时间
        helper.setText(R.id.tv_plan_time, TimeUtils.date2Tasktime3(item.getTpFlightTime()));
        //仓位
        helper.setText(R.id.tv_cangwei_info,item.getTpFregihtSpace());

        viewDelete.setTag(helper.getAdapterPosition());
        if (!viewDelete.hasOnClickListeners()) {
            viewDelete.setOnClickListener(v -> {
                if (mDeleteClickListener != null) {
                    mDeleteClickListener.onDeleteClick(v, (Integer) v.getTag());
                }
            });
        }
    }
    public void setOnDeleteClickListener(PullGoodsInfoAdapter.OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }

}
