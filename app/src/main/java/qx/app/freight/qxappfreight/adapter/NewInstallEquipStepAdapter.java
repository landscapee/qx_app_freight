package qx.app.freight.qxappfreight.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.widget.SlideLeftExecuteView;

/**
 * 装机步骤列表适配器
 */
public class NewInstallEquipStepAdapter extends BaseMultiItemQuickAdapter<LoadAndUnloadTodoBean.OperationStepObjBean, BaseViewHolder> {
    private OnSlideListener onSlideListener;

    NewInstallEquipStepAdapter(List<LoadAndUnloadTodoBean.OperationStepObjBean> data) {
        super(data);
        addItemType(Constants.TYPE_STEP_OVER, R.layout.item_install_equip_step_over);
        addItemType(Constants.TYPE_STEP_NOW, R.layout.item_install_equip_step_now);
        addItemType(Constants.TYPE_STEP_NEXT, R.layout.item_install_equip_step_next);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoadAndUnloadTodoBean.OperationStepObjBean item) {
        helper.setText(R.id.tv_step_plan_date, item.getPlanTime());
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
