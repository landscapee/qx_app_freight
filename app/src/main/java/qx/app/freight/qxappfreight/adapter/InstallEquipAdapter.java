package qx.app.freight.qxappfreight.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.InstallEquipEntity;
import qx.app.freight.qxappfreight.widget.CollapsableLinearLayout;

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
        ImageView ivControl = helper.getView(R.id.iv_control);
        LinearLayout llBg = helper.getView(R.id.ll_bg);
        ImageView ivType = helper.getView(R.id.iv_operate_type);
        TextView tvTime = helper.getView(R.id.tv_time);
        boolean hasActualTime = TextUtils.isEmpty(item.getActualTime());
        tvTime.setText(hasActualTime ? item.getScheduleTime() : item.getActualTime());
        Drawable drawableLeft;
        if (item.getTaskTpye() == 1) {//装机
            ivType.setImageResource(R.mipmap.li);
            drawableLeft = mContext.getResources().getDrawable(R.mipmap.ji);
        } else {
            ivType.setImageResource(R.mipmap.jin);//应该显示  ===进
            if (hasActualTime) {
                drawableLeft = mContext.getResources().getDrawable(R.mipmap.shi);
            } else {
                drawableLeft = mContext.getResources().getDrawable(R.mipmap.ji);
            }
        }
        tvTime.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
        tvTime.setCompoundDrawablePadding(5);
        helper.setText(R.id.tv_plane_info, item.getFlightInfo());
        helper.setText(R.id.tv_craft_number, item.getAirCraftNo());
        ImageView ivTwoPlace = helper.getView(R.id.iv_two_place);
        if (TextUtils.isEmpty(item.getStartPlace())) {//只要出发地没有，则证明没有航线信息，全部展示view应该隐藏起来
            helper.getView(R.id.tv_start_place).setVisibility(View.GONE);
            helper.getView(R.id.tv_middle_place).setVisibility(View.GONE);
            ivTwoPlace.setVisibility(View.GONE);
            helper.getView(R.id.tv_end_place).setVisibility(View.GONE);
        } else {
            if (TextUtils.isEmpty(item.getMiddlePlace())) {//中转站名为空
                helper.getView(R.id.tv_start_place).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_start_place, item.getStartPlace());
                helper.getView(R.id.tv_middle_place).setVisibility(View.GONE);
                ivTwoPlace.setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_end_place).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_end_place, item.getEndPlace());
            } else {
                helper.getView(R.id.tv_start_place).setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_middle_place).setVisibility(View.VISIBLE);
                ivTwoPlace.setVisibility(View.GONE);
                helper.getView(R.id.tv_end_place).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_start_place, item.getStartPlace());
                helper.setText(R.id.tv_middle_place, item.getMiddlePlace());
                helper.setText(R.id.tv_end_place, item.getEndPlace());
            }
        }
        helper.setText(R.id.tv_seat, item.getSeat());
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
