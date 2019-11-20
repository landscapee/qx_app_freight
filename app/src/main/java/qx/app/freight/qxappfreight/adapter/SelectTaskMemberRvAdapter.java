package qx.app.freight.qxappfreight.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.SelectTaskMemberEntity;
import qx.app.freight.qxappfreight.widget.RoundCheckBox;

/**
 * 人员列表页adapter
 */
public class SelectTaskMemberRvAdapter extends BaseQuickAdapter <SelectTaskMemberEntity, BaseViewHolder> {
    private OnBoxClickListener onBoxClickListener;

    public SelectTaskMemberRvAdapter(List <SelectTaskMemberEntity> mDatas) {
        super(R.layout.item_select_task_member, mDatas);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectTaskMemberEntity item) {
        helper.setText(R.id.tv_member_name, item.getStaffName());
        RoundCheckBox checkBox = helper.getView(R.id.rcb_select);
        checkBox.setChecked(item.isSelected());
        checkBox.setOnClickListener(v -> {
            item.setSelected(!item.isSelected());
        });
    }
    public interface OnBoxClickListener {
        void onBoxClick(int pos);
    }

    public void setOnBoxClickListener(OnBoxClickListener onBoxClickListener) {
        this.onBoxClickListener = onBoxClickListener;
    }


}
