package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.UnloadPlaneEntity;

/**
 * 装机版本列表详情适配器
 */
public class UnloadPlaneDetailAdapter extends BaseQuickAdapter<UnloadPlaneEntity, BaseViewHolder> {
    UnloadPlaneDetailAdapter(@Nullable List<UnloadPlaneEntity> data) {
        super(R.layout.item_unload_plane_version_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UnloadPlaneEntity item) {
        helper.setText(R.id.tv_berth, item.getBerth()).setText(R.id.tv_board_number, String.valueOf(item.getBoardNumber())).setText(R.id.tv_uld_number, item.getUldNumber())
                .setText(R.id.tv_target_name, item.getTarget()).setText(R.id.tv_weight, String.valueOf(item.getWeight())).setText(R.id.tv_type, item.getType())
                .setText(R.id.tv_remark, item.getRemark());
    }
}
