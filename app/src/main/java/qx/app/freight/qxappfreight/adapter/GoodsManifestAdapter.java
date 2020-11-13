package qx.app.freight.qxappfreight.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.widget.FlightInfoLayout;

/**
 * 使用服务器原始数据的装卸机代办适配器
 */
public class GoodsManifestAdapter extends BaseQuickAdapter<LoadAndUnloadTodoBean, BaseViewHolder> {
    private OnSlideStepListener onSlideStepListener;
    private OnFlightSafeguardListenner onFlightSafeguardListenner;
    private OnReOpenLoadTaskListener onReOpenLoadTaskListener;
    private boolean showReOpenBtn;

    public GoodsManifestAdapter(@Nullable List<LoadAndUnloadTodoBean> data) {
        super(R.layout.item_goods_manifest, data);
    }

    public GoodsManifestAdapter(@Nullable List<LoadAndUnloadTodoBean> data, boolean showReOpenBtn) {
        this(data);
        this.showReOpenBtn = showReOpenBtn;
    }

    @Override
    protected void convert(BaseViewHolder helper, LoadAndUnloadTodoBean item) {
        helper.setIsRecyclable(false);
//        LinearLayout llBg = helper.getView(R.id.ll_bg);
//        if (!item.isAcceptTask()) {
//            llBg.setBackgroundColor(Color.parseColor("#ffac00"));
//        } else {
//            llBg.setBackgroundColor(Color.parseColor("#ffffff"));
//        }
//        boolean isWidePlane = item.getWidthAirFlag() == 0;
//        helper.setText(R.id.tv_plane_type, isWidePlane ? "宽体机" : "窄体机");
        helper.setText(R.id.tv_plane_type, item.getAircraftType());
        ImageView ivType = helper.getView(R.id.iv_operate_type);
        TextView tvTime = helper.getView(R.id.tv_time);
        Button btnReopen = helper.getView(R.id.btn_reopen_load_task);
        if (showReOpenBtn) {//显示重新开启装机任务按钮，只有装机已办页面中有使用
            btnReopen.setVisibility(View.VISIBLE);
            btnReopen.setOnClickListener(v -> {
                if (onReOpenLoadTaskListener != null) {
                    onReOpenLoadTaskListener.onReOpenLoadTask(helper.getAdapterPosition());
                }
            });
        } else {
            btnReopen.setVisibility(View.GONE);
        }
        Button btnFS = helper.getView(R.id.btn_flight_safeguard);
        btnFS.setOnClickListener(v -> {
            onFlightSafeguardListenner.onFlightSafeguardClick(helper.getAdapterPosition());
        });
        Button btnClear = helper.getView(R.id.btn_seat_clear);
        btnClear.setVisibility(View.GONE);
        btnClear.setOnClickListener(v -> {
            onFlightSafeguardListenner.onClearClick(helper.getAdapterPosition());
        });
        tvTime.setText(item.getTimeForShow());
        Drawable drawableLeft = null;
        if (item.getMovement() == 1 || item.getMovement() == 4) {//装机
            ivType.setImageResource(R.mipmap.jin);//应该显示  ===进
        } else {
            ivType.setImageResource(R.mipmap.li);
        }
        switch (item.getTimeType()) {
            case Constants.TIME_TYPE_AUTUAL:
                drawableLeft = ContextCompat.getDrawable(mContext,R.mipmap.shi);
                break;
            case Constants.TIME_TYPE_EXCEPT:
                drawableLeft = ContextCompat.getDrawable(mContext,R.mipmap.yu);
                break;
            case Constants.TIME_TYPE_PLAN:
                drawableLeft = ContextCompat.getDrawable(mContext,R.mipmap.ji);
                break;
        }
        tvTime.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
        tvTime.setCompoundDrawablePadding(5);
        helper.setText(R.id.tv_plane_info, StringUtil.toText(item.getFlightNo()));
        helper.setText(R.id.tv_craft_number, StringUtil.toText(item.getAircraftno()));
        LinearLayout llLink = helper.getView(R.id.ll_link);
        //连班航班
        if (item.getMovement() == 4 && item.getRelateInfoObj() != null) {
            llLink.setVisibility(View.VISIBLE);
            ImageView ivTypeLink = helper.getView(R.id.iv_operate_type_link);
            helper.setText(R.id.tv_plane_type_link, item.getAircraftType());
            TextView tvTimeLink = helper.getView(R.id.tv_time_link);
            helper.setText(R.id.tv_plane_info_link, StringUtil.toText(item.getRelateInfoObj().getFlightNo()));
            helper.setText(R.id.tv_craft_number_link, StringUtil.toText(item.getRelateInfoObj().getAircraftno()));
            helper.setText(R.id.tv_seat_link, StringUtil.toText(item.getRelateInfoObj().getSeat()));
            tvTimeLink.setText(item.getRelateInfoObj().getTimeForShow());
            if (item.getRelateInfoObj().getMovement() == 1 || item.getRelateInfoObj().getMovement() == 4) {//装机
                ivTypeLink.setImageResource(R.mipmap.jin);//应该显示  ===进
            } else {
                ivTypeLink.setImageResource(R.mipmap.li);
            }
            Drawable drawableLeftLink = null;
            switch (item.getRelateInfoObj().getTimeType()) {
                case Constants.TIME_TYPE_AUTUAL:
                    drawableLeftLink = ContextCompat.getDrawable(mContext,R.mipmap.shi);
                    break;
                case Constants.TIME_TYPE_EXCEPT:
                    drawableLeftLink = ContextCompat.getDrawable(mContext,R.mipmap.yu);
                    break;
                case Constants.TIME_TYPE_PLAN:
                    drawableLeftLink = ContextCompat.getDrawable(mContext,R.mipmap.ji);
                    break;
            }
            tvTimeLink.setCompoundDrawablesWithIntrinsicBounds(drawableLeftLink, null, null, null);
            tvTimeLink.setCompoundDrawablePadding(5);
            LinearLayout containerLink = helper.getView(R.id.ll_flight_info_container_link);
            if (item.getRelateInfoObj().getFlightInfoList() != null) {
                FlightInfoLayout layoutLink = new FlightInfoLayout(mContext, item.getRelateInfoObj().getFlightInfoList());
                LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                containerLink.removeAllViews();
                containerLink.addView(layoutLink, paramsMain);
            }
        } else {
            llLink.setVisibility(View.GONE);
        }
        LinearLayout container = helper.getView(R.id.ll_flight_info_container);
        FlightInfoLayout layout = new FlightInfoLayout(mContext, item.getFlightInfoList());
        LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.removeAllViews();
        container.addView(layout, paramsMain);
        helper.setText(R.id.tv_seat, StringUtil.toText(item.getSeat()));
        ImageView ivDone = helper.getView(R.id.iv_done); //已办图片
        if (!StringUtil.isEmpty(item.getOperationStepObj().get(item.getOperationStepObj().size() - 1).getStepDoneDate())) {
            btnFS.setVisibility(View.GONE);
            ivDone.setVisibility(View.VISIBLE);
        } else {
            ivDone.setVisibility(View.GONE);
            btnFS.setVisibility(View.VISIBLE);
        }
    }

    public interface OnSlideStepListener {
        void onSlideStep(int bigPos, NewInstallEquipStepAdapter adapter, int smallPos);
    }

    public void setOnSlideStepListener(OnSlideStepListener onSlideStepListener) {
        this.onSlideStepListener = onSlideStepListener;
    }

    public interface OnFlightSafeguardListenner {
        void onFlightSafeguardClick(int position);

        void onClearClick(int position);
    }

    public void setOnFlightSafeguardListenner(OnFlightSafeguardListenner onFlightSafeguardListenner) {
        this.onFlightSafeguardListenner = onFlightSafeguardListenner;
    }

    public interface OnReOpenLoadTaskListener {
        void onReOpenLoadTask(int pos);
    }

    public void setOnReOpenLoadTaskListener(OnReOpenLoadTaskListener onReOpenLoadTaskListener) {
        this.onReOpenLoadTaskListener = onReOpenLoadTaskListener;
    }
}
