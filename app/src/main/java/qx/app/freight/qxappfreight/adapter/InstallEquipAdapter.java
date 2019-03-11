package qx.app.freight.qxappfreight.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

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
        ImageView ivType = helper.getView(R.id.iv_operate_type);
        if (item.getTaskTpye()==1){
            ivType.setImageResource(R.mipmap.li);
        }else {
            ivType.setImageResource(R.mipmap.jin);//应该显示  ===进
        }
        helper.setText(R.id.tv_plane_info, item.getFlightInfo());
        helper.setText(R.id.tv_craft_number, item.getAirCraftNo());
        helper.setText(R.id.tv_start_place, item.getStartPlace());
        helper.setText(R.id.tv_middle_place, item.getMiddlePlace());
        helper.setText(R.id.tv_end_place, item.getEndPlace());
        helper.setText(R.id.tv_seat, item.getSeat());
        helper.setText(R.id.tv_time, item.getScheduleTime());
        RecyclerView rvStep = helper.getView(R.id.rv_step);
        rvStep.setLayoutManager(new LinearLayoutManager(mContext));
        InstallEquipStepAdapter adapter = new InstallEquipStepAdapter(item.getList());
        rvStep.setAdapter(adapter);
        adapter.setOnSlideListener(pos -> {
            if (onSlideStepListener!=null){
                onSlideStepListener.onSlideStep(helper.getAdapterPosition(), adapter,pos);
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
    }
    public interface OnSlideStepListener{
        void onSlideStep(int bigPos,InstallEquipStepAdapter adapter,int smallPos);
    }

    public void setOnSlideStepListener(OnSlideStepListener onSlideStepListener) {
        this.onSlideStepListener = onSlideStepListener;
    }
}
