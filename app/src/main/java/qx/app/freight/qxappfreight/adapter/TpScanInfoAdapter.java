package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.OutFieldFlightBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.utils.MapValue;
import qx.app.freight.qxappfreight.utils.TimeUtils;

/**
 * 卸机页面扫描货物或行李的适配器
 */
public class TpScanInfoAdapter extends BaseQuickAdapter<ScooterInfoListBean, BaseViewHolder> {
    private OnDeleteClickLister mDeleteClickListener;
    private OutFieldFlightBean mInfo;
    private TpScanInfoAdapter(@Nullable List<ScooterInfoListBean> data) {
        super(R.layout.item_scan_info,data);
    }
    public TpScanInfoAdapter(@Nullable List<ScooterInfoListBean> data, OutFieldFlightBean info) {
        this(data);
        mInfo=info;
    }

    @Override
    protected void convert(BaseViewHolder helper, ScooterInfoListBean item) {
        View viewDelete = helper.getView(R.id.ll_delete);
        if (item.getFlightType().equals("I")){
            helper.getView(R.id.iv_type_inter).setVisibility(View.VISIBLE);
        }else {
            helper.getView(R.id.iv_type_inter).setVisibility(View.INVISIBLE);
        }
        helper.setText(R.id.tv_board_number, MapValue.getCarTypeValue(item.getScooterType()) +item.getScooterCode());
        helper.setText(R.id.tv_flight_type,mInfo.getFlightNo());
        helper.setText(R.id.tv_seat,mInfo.getSeat());
        helper.setText(R.id.tv_flight_arrive_time, TimeUtils.getHMDay(Long.valueOf(mInfo.getScheduleTime())));
        viewDelete.setTag(helper.getAdapterPosition());
        if (!viewDelete.hasOnClickListeners()) {
            viewDelete.setOnClickListener(v -> {
                if (mDeleteClickListener != null) {
                    mDeleteClickListener.onDeleteClick(v, (Integer) v.getTag());
                }
            });
        }
    }
    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }
}
