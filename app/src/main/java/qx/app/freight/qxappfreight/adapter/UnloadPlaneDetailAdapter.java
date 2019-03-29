package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.UnloadPlaneEntity;

/**
 * 装机版本列表详情适配器
 */
public class UnloadPlaneDetailAdapter extends BaseQuickAdapter<UnloadPlaneEntity, BaseViewHolder> {
    private boolean showGoodsPos;
    private UnloadPlaneDetailAdapter(@Nullable List<UnloadPlaneEntity> data) {
        super(R.layout.item_unload_plane_version_detail, data);
    }
    UnloadPlaneDetailAdapter(List<UnloadPlaneEntity> data, boolean showGoodsPos){
        this(data);
        this.showGoodsPos=showGoodsPos;
    }
    @Override
    protected void convert(BaseViewHolder helper, UnloadPlaneEntity item) {
        if (showGoodsPos){
            helper.getView(R.id.tv_goods_position).setVisibility(View.VISIBLE);
            helper.getView(R.id.tv_divider).setVisibility(View.VISIBLE);
        }else {
            helper.getView(R.id.tv_goods_position).setVisibility(View.GONE);
            helper.getView(R.id.tv_divider).setVisibility(View.GONE);
        }
        helper.setText(R.id.tv_berth, item.getBerth()).setText(R.id.tv_board_number, String.valueOf(item.getBoardNumber())).setText(R.id.tv_uld_number, item.getUldNumber())
                .setText(R.id.tv_target_name, item.getTarget()).setText(R.id.tv_weight, String.valueOf(item.getWeight())).setText(R.id.tv_type, item.getType());
    }
}
