package qx.app.freight.qxappfreight.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

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
        if (data.size() != 0) {
            data.add(0, new UnloadPlaneEntity());
        }
    }

    UnloadPlaneDetailAdapter(List<UnloadPlaneEntity> data, boolean showGoodsPos) {
        this(data);
        this.showGoodsPos = showGoodsPos;
    }

    @Override
    protected void convert(BaseViewHolder helper, UnloadPlaneEntity item) {
        if (showGoodsPos) {
            helper.getView(R.id.tv_goods_position).setVisibility(View.VISIBLE);
            helper.getView(R.id.tv_divider).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.tv_goods_position).setVisibility(View.GONE);
            helper.getView(R.id.tv_divider).setVisibility(View.GONE);
        }
        TextView tvBerth = helper.getView(R.id.tv_berth);
        TextView tvGoodsPos = helper.getView(R.id.tv_goods_position);
        TextView tvBoardNumber = helper.getView(R.id.tv_board_number);
        TextView tvUldNumber = helper.getView(R.id.tv_uld_number);
        TextView tvTarget = helper.getView(R.id.tv_target_name);
        TextView tvWeight = helper.getView(R.id.tv_weight);
        TextView tvType = helper.getView(R.id.tv_type);
        TextView tvNumber = helper.getView(R.id.tv_number);
        TextView[] tvList = {tvBerth, tvBoardNumber, tvGoodsPos, tvTarget, tvNumber, tvUldNumber, tvWeight, tvType};
        if (helper.getAdapterPosition() == 0) {
            for (TextView tv : tvList) {
                tv.setTextColor(Color.parseColor("#e5e5e5"));
                tv.setBackgroundColor(Color.parseColor("#7b8a9b"));
            }
            helper.setText(R.id.tv_berth, "舱位").setText(R.id.tv_goods_position, "货位").setText(R.id.tv_board_number, "板号").setText(R.id.tv_uld_number, "ULD号")
                    .setText(R.id.tv_target_name, "目的地").setText(R.id.tv_weight, "重量").setText(R.id.tv_type, "类型").setText(R.id.tv_number, "件数");
        } else {
            for (TextView tv : tvList) {
                tv.setTextColor(Color.parseColor("#666666"));
                tv.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            helper.setText(R.id.tv_berth, item.getBerth()).setText(R.id.tv_goods_position, item.getGoodsPosition()).setText(R.id.tv_board_number, String.valueOf(item.getBoardNumber())).setText(R.id.tv_uld_number, "AKE9999CZ")
                    .setText(R.id.tv_target_name, item.getTarget()).setText(R.id.tv_weight, String.valueOf(item.getWeight())).setText(R.id.tv_type, item.getType()).setText(R.id.tv_number, String.valueOf(item.getNumber()));
        }
    }
}
