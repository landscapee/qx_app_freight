package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.utils.TimeUtils;

/**
 * 卸机页面扫描货物或行李的适配器
 */
public class ScanInfoAdapter extends BaseQuickAdapter<ScooterInfoListBean, BaseViewHolder> {
    private OnDeleteClickLister mDeleteClickListener;
    private String mInfo;
    public ScanInfoAdapter(@Nullable List<ScooterInfoListBean> data) {
        super(R.layout.item_scan_info,data);
    }
    public ScanInfoAdapter(@Nullable List<ScooterInfoListBean> data,String info) {
        super(R.layout.item_scan_info,data);
        mInfo=info;
    }

    @Override
    protected void convert(BaseViewHolder helper, ScooterInfoListBean item) {
        View viewDelete = helper.getView(R.id.ll_delete);
        String type;
        if(item.getScooterType()==1){
            type="大板";
        }else {
            type="小板";
        }
        helper.setText(R.id.tv_board_number,type+item.getScooterCode());
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
