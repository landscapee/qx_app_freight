package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.utils.MapValue;
import qx.app.freight.qxappfreight.utils.TimeUtils;

/**
 * 卸机页面扫描货物或行李的适配器
 */
public class ScanInfoAdapter extends BaseQuickAdapter<ScooterInfoListBean, BaseViewHolder> {
    private OnDeleteClickLister mDeleteClickListener;
    private String mInfo;
    private ScanInfoAdapter(@Nullable List<ScooterInfoListBean> data) {
        super(R.layout.item_scan_info,data);
    }
    public ScanInfoAdapter(@Nullable List<ScooterInfoListBean> data,String info) {
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
        helper.setText(R.id.tv_board_number, MapValue.getCarTypeValue(item.getScooterType()+"")+item.getScooterCode());
        String []infos=mInfo.split("\\*");
        helper.setText(R.id.tv_flight_type,infos[0]);
        helper.setText(R.id.tv_seat,infos[5]);
        helper.setText(R.id.tv_flight_arrive_time, TimeUtils.getHMDay(Long.valueOf(infos[6])));
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
