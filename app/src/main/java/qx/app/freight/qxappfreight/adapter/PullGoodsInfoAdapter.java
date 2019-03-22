package qx.app.freight.qxappfreight.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.constant.Constants;

/**
 * 拉货上报信息列表适配器
 */
public class PullGoodsInfoAdapter extends BaseMultiItemQuickAdapter<TransportTodoListBean, BaseViewHolder> {

    private PullGoodsInfoAdapter.OnDeleteClickLister mDeleteClickListener;

    public PullGoodsInfoAdapter(List<TransportTodoListBean> data) {
        super(data);
        addItemType(Constants.TYPE_PULL_BOARD, R.layout.item_pull_goods_board_info);
        addItemType(Constants.TYPE_PULL_BILL, R.layout.item_pull_goods_bill_info);
    }

    @Override
    protected void convert(BaseViewHolder helper, TransportTodoListBean item) {
        if (item.getInfoType() == Constants.TYPE_PULL_BILL) {
            helper.setText(R.id.tv_bill_number, item.getBillNumber());
        }
        helper.setText(R.id.tv_board_number, item.getTpScooterId());
        View viewDelete = helper.getView(R.id.ll_delete);
        //件数 - 重量 - 体积
        helper.setText(R.id.tv_goods_info, String.format(mContext.getString(R.string.format_goods_info)
                , item.getTpCargoNumber()
                , item.getTpCargoWeight()
                , item.getTpCargoVolume()));
        //航班号
        helper.setText(R.id.tv_flight_number, item.getTpFlightNumber());
        //机位号
        helper.setText(R.id.tv_place_number, item.getTpFlightLocate());
        // 时间
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm(dd)", Locale.CHINESE);
        helper.setText(R.id.tv_plan_time, sdf.format(new Date(item.getTpFlightTime())));
        //仓位
        helper.setText(R.id.tv_cangwei_info, item.getTpFregihtSpace());
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
