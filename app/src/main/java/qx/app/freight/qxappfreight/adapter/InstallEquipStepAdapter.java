package qx.app.freight.qxappfreight.adapter;

import android.content.Intent;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.LoadPlaneActivity;
import qx.app.freight.qxappfreight.activity.UnloadPlaneActivity;
import qx.app.freight.qxappfreight.bean.MultiStepEntity;
import qx.app.freight.qxappfreight.widget.SlideLeftExecuteView;

/**
 * 装机步骤列表适配器
 */
public class InstallEquipStepAdapter extends BaseMultiItemQuickAdapter<MultiStepEntity, BaseViewHolder> {
    private List<MultiStepEntity> mList;
    private OnSlideListener onSlideListener;

    InstallEquipStepAdapter(List<MultiStepEntity> data) {
        super(data);
        mList = data;
        addItemType(MultiStepEntity.TYPE_STEP_OVER, R.layout.item_install_equip_step_over);
        addItemType(MultiStepEntity.TYPE_STEP_NOW, R.layout.item_install_equip_step_now);
        addItemType(MultiStepEntity.TYPE_STEP_NEXT, R.layout.item_install_equip_step_next);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiStepEntity item) {
        switch (helper.getItemViewType()) {
            case MultiStepEntity.TYPE_STEP_OVER:
                helper.setText(R.id.tv_step_name, item.getStepName());
                helper.setText(R.id.tv_step_date, item.getStepDoneDate());
                break;
            case MultiStepEntity.TYPE_STEP_NOW:
                helper.setText(R.id.tv_step_name, item.getStepName());
                SlideLeftExecuteView slideView = helper.getView(R.id.slev_do_task);
                slideView.setCanTouch(true);
                slideView.setLockListener(() -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINESE);
                    int pos = helper.getAdapterPosition();
                    if (pos != 3) {
                        item.setItemType(MultiStepEntity.TYPE_STEP_OVER);
                    }
                    if (pos != 4 && pos != 3) {
                        mList.get(pos + 1).setItemType(MultiStepEntity.TYPE_STEP_NOW);
                    }
                    if (onSlideListener != null) {
                        onSlideListener.onSlide(pos);
                    }
                    if (pos == 3) {
                        mList.get(pos).setStepDoneDate(sdf.format(new Date()) + "-");
                        Intent intent;
                        if (item.getLoadUnloadType() == 1) {
                            intent = new Intent(mContext, LoadPlaneActivity.class);
                        } else {
                            intent = new Intent(mContext, UnloadPlaneActivity.class);
                        }
                        intent.putExtra("plane_info", mList.get(pos).getPlaneInfo());
                        mContext.startActivity(intent);
                    } else {
                        mList.get(pos).setStepDoneDate(sdf.format(new Date()));
                    }
                });
                break;
            case MultiStepEntity.TYPE_STEP_NEXT:
                helper.setText(R.id.tv_step_name, item.getStepName());
                break;
        }
    }

    public interface OnSlideListener {
        void onSlide(int pos);
    }

    public void setOnSlideListener(OnSlideListener onSlideListener) {
        this.onSlideListener = onSlideListener;
    }
}
