package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.BoardChoseBean;
import qx.app.freight.qxappfreight.widget.RoundCheckBox;

/**
 * 展示已经选择绑定的板车号列表的适配器
 */
public class EditBoardsAdapter extends BaseQuickAdapter<BoardChoseBean, BaseViewHolder> {

    public EditBoardsAdapter(@Nullable List<BoardChoseBean> list) {
        super(R.layout.item_check_board, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, BoardChoseBean item) {
        helper.setText(R.id.tv_board_name, item.getBoardName());
        RoundCheckBox rcbBoard = helper.getView(R.id.rcb_board);
        rcbBoard.setOnCheckedChangeListener((buttonView, isChecked) -> item.setChosed(isChecked));
    }
}
