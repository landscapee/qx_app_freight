package qx.app.freight.qxappfreight.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.InstallEquipEntity;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.widget.CollapsableLinearLayout;
import qx.app.freight.qxappfreight.widget.FlightInfoLayout;

/**
 * 装机列表适配器
 */
public class InstallEquipAdapter extends BaseQuickAdapter<InstallEquipEntity, BaseViewHolder> {
    private OnSlideStepListener onSlideStepListener;

    public InstallEquipAdapter(List<InstallEquipEntity> data) {
        super(R.layout.item_install_equip, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InstallEquipEntity item) {
        helper.setIsRecyclable(false);
        if (!item.isAcceptTask()) {
            Log.e("tagTest","任务未领受，黄色");
            helper.itemView.setBackgroundColor(Color.parseColor("#FFAC00"));
        }else {
            Log.e("tagTest","任务领受，白色");
            helper.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        helper.setText(R.id.tv_plane_type, (item.isWidePlane()) ? "宽体机" : "窄体机");
        ImageView ivControl = helper.getView(R.id.iv_control);
        LinearLayout llBg = helper.getView(R.id.ll_bg);
        ImageView ivType = helper.getView(R.id.iv_operate_type);
        TextView tvTime = helper.getView(R.id.tv_time);
        tvTime.setText(item.getTimeForShow());
        Drawable drawableLeft = null;
        if (item.getTaskType() == 1) {//装机
            ivType.setImageResource(R.mipmap.li);
        } else {
            ivType.setImageResource(R.mipmap.jin);//应该显示  ===进
        }
        switch (item.getTimeType()) {
            case Constants.TIME_TYPE_AUTUAL:
                drawableLeft = mContext.getResources().getDrawable(R.mipmap.shi);
                break;
            case Constants.TIME_TYPE_EXCEPT:
                drawableLeft = mContext.getResources().getDrawable(R.mipmap.yu);
                break;
            case Constants.TIME_TYPE_PLAN:
                drawableLeft = mContext.getResources().getDrawable(R.mipmap.ji);
                break;
        }
        tvTime.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
        tvTime.setCompoundDrawablePadding(5);
        helper.setText(R.id.tv_plane_info, StringUtil.toText(item.getFlightInfo()));
        helper.setText(R.id.tv_craft_number, StringUtil.toText(item.getAirCraftNo()));
        LinearLayout container = helper.getView(R.id.ll_flight_info_container);
        FlightInfoLayout layout = new FlightInfoLayout(mContext, item.getFlightInfoList());
        LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.removeAllViews();
        container.addView(layout, paramsMain);
        helper.setText(R.id.tv_seat, StringUtil.toText(item.getSeat()));
        RecyclerView rvStep = helper.getView(R.id.rv_step);
        rvStep.setLayoutManager(new LinearLayoutManager(mContext));
        InstallEquipStepAdapter adapter = new InstallEquipStepAdapter(item.getList());
        rvStep.setAdapter(adapter);
        adapter.setOnSlideListener(pos -> {
            if (onSlideStepListener != null) {
                onSlideStepListener.onSlideStep(helper.getAdapterPosition(), adapter, pos);
            }
        });
        CollapsableLinearLayout collView = helper.getView(R.id.coll_listview);
        if (item.isShowDetail()) {
            rvStep.setVisibility(View.VISIBLE);
            ivControl.setImageResource(R.mipmap.up);
            collView.expand();
        } else {
            rvStep.setVisibility(View.GONE);
            ivControl.setImageResource(R.mipmap.down);
            collView.collapse();
        }
        ivControl.setOnClickListener(v -> {
            item.setShowDetail(!item.isShowDetail());
            if (item.isShowDetail()) {
                rvStep.setVisibility(View.VISIBLE);
                ivControl.setImageResource(R.mipmap.up);
                collView.expand();
            } else {
                rvStep.setVisibility(View.GONE);
                ivControl.setImageResource(R.mipmap.down);
                collView.collapse();
            }
        });
        llBg.setOnClickListener(v -> {
            item.setShowDetail(!item.isShowDetail());
            if (item.isShowDetail()) {
                rvStep.setVisibility(View.VISIBLE);
                ivControl.setImageResource(R.mipmap.up);
                collView.expand();
            } else {
                rvStep.setVisibility(View.GONE);
                ivControl.setImageResource(R.mipmap.down);
                collView.collapse();
            }
        });
    }

    public interface OnSlideStepListener {
        void onSlideStep(int bigPos, InstallEquipStepAdapter adapter, int smallPos);
    }

    public void setOnSlideStepListener(OnSlideStepListener onSlideStepListener) {
        this.onSlideStepListener = onSlideStepListener;
    }
}
