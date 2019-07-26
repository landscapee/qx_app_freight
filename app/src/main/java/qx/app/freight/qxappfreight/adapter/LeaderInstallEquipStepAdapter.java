package qx.app.freight.qxappfreight.adapter;

import android.content.Intent;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.AssignInstallEquipMemberActivity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.widget.SlideLeftExecuteView;

/**
 * 装卸员滑动步骤列表适配器，领受点击可以跳转到分配装卸员人员的界面
 */
public class LeaderInstallEquipStepAdapter extends BaseMultiItemQuickAdapter<LoadAndUnloadTodoBean.OperationStepObjBean, BaseViewHolder> {
    private OnSlideListener onSlideListener;

    LeaderInstallEquipStepAdapter(List<LoadAndUnloadTodoBean.OperationStepObjBean> data) {
        super(data);
        addItemType(Constants.TYPE_STEP_OVER, R.layout.item_install_equip_step_over);
        addItemType(Constants.TYPE_STEP_NOW, R.layout.item_install_equip_step_now);
        addItemType(Constants.TYPE_STEP_NEXT, R.layout.item_install_equip_step_next);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoadAndUnloadTodoBean.OperationStepObjBean item) {
        helper.itemView.setOnClickListener(v -> {
            if (helper.getAdapterPosition() == 0) {
                if (onSlideListener != null) {//点击的时候会跳转两次页面，要改！！！！！！！！！！！！！！！
                    onSlideListener.onSlide(0);
                }
                Intent intent = new Intent(mContext, AssignInstallEquipMemberActivity.class);
                intent.putExtra("task_id", item.getTaskId());
                mContext.startActivity(intent);
            }
        });
        switch (helper.getItemViewType()) {
            case Constants.TYPE_STEP_OVER:
                helper.setText(R.id.tv_step_name, item.getOperationName());
                helper.setText(R.id.tv_step_date, item.getStepDoneDate());
                break;
            case Constants.TYPE_STEP_NOW:
                helper.setText(R.id.tv_step_name, item.getOperationName());
                SlideLeftExecuteView slideView = helper.getView(R.id.slev_do_task);
                slideView.setCanTouch(true);
                slideView.setLockListener(() -> {
                    int pos = helper.getAdapterPosition();
                    if (onSlideListener != null) {
                        onSlideListener.onSlide(pos);
                    }
                });
                break;
            case Constants.TYPE_STEP_NEXT:
                helper.setText(R.id.tv_step_name, item.getOperationName());
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
