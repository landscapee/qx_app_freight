package qx.app.freight.qxappfreight.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.utils.MapValue;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;

/**
 * 卸机页面扫描货物或行李的适配器
 */
public class ScanInfoAdapter extends BaseQuickAdapter<ScooterInfoListBean, BaseViewHolder> {
    private OnDeleteClickLister mDeleteClickListener;
    private LoadAndUnloadTodoBean mInfo;

    private ScanInfoAdapter(@Nullable List<ScooterInfoListBean> data) {
        super(R.layout.item_scan_info, data);
    }

    public ScanInfoAdapter(@Nullable List<ScooterInfoListBean> data, LoadAndUnloadTodoBean info) {
        this(data);
        mInfo = info;
    }

    @Override
    protected void convert(BaseViewHolder helper, ScooterInfoListBean item) {
        View viewDelete = helper.getView(R.id.ll_delete);
        if ("I".equals(item.getFlightType())) {
            helper.getView(R.id.iv_type_inter).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.iv_type_inter).setVisibility(View.INVISIBLE);
        }
        helper.setText(R.id.tv_board_number, MapValue.getCarTypeValue(item.getScooterType()) + item.getScooterCode());
        helper.setText(R.id.tv_flight_type, mInfo.getFlightNo());
        helper.setText(R.id.tv_seat, mInfo.getSeat());
        TextView tvTime = helper.getView(R.id.tv_flight_arrive_time);
        String time;
        Drawable drawableLeft;
        if (!StringUtil.isTimeNull(String.valueOf(mInfo.getAta()))) {
            time = TimeUtils.getHMDay(mInfo.getAta());
            drawableLeft = ContextCompat.getDrawable(mContext,R.mipmap.shi);
        } else if (!StringUtil.isTimeNull(String.valueOf(mInfo.getEta()))) {
            time = TimeUtils.getHMDay(mInfo.getEta());
            drawableLeft = ContextCompat.getDrawable(mContext,R.mipmap.yu);
        } else {
            time = TimeUtils.getHMDay(mInfo.getSta());
            drawableLeft = ContextCompat.getDrawable(mContext,R.mipmap.ji);
        }
        tvTime.setText(time);
        tvTime.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
        tvTime.setCompoundDrawablePadding(5);
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
