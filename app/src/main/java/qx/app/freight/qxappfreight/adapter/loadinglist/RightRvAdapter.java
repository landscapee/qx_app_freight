package qx.app.freight.qxappfreight.adapter.loadinglist;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.loadinglist.ScrollEntity;

/**
 * 装机单位置右边的适配器
 */
public class RightRvAdapter extends BaseQuickAdapter<ScrollEntity, BaseViewHolder> {
    public RightRvAdapter(@Nullable List<ScrollEntity> data) {
        super(R.layout.item_loading_list_right, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScrollEntity item) {
        TextView tvBoardNumber = helper.getView(R.id.tv_board_number);
        TextView tvUldNumber = helper.getView(R.id.tv_uld_number);
        TextView tvTarget = helper.getView(R.id.tv_target_name);
        TextView tvWeight = helper.getView(R.id.tv_weight);
        TextView tvType = helper.getView(R.id.tv_type);
        TextView tvNumber = helper.getView(R.id.tv_number);
        TextView[] tvList = {tvBoardNumber, tvUldNumber, tvTarget, tvType, tvWeight, tvNumber};
        ImageView ivPullDown=helper.getView(R.id.iv_pull_down);
        helper.setText(R.id.tv_board_number, String.valueOf(item.getBoardNumber())).setText(R.id.tv_uld_number, item.getUldNumber())
                .setText(R.id.tv_target_name, item.getTarget()).setText(R.id.tv_weight, String.valueOf(item.getWeight())).setText(R.id.tv_type, item.getType()).setText(R.id.tv_number, String.valueOf(item.getNumber()));
        if (helper.getAdapterPosition() == 0) {
            for (TextView tv : tvList) {
                tv.setTextColor(Color.parseColor("#e5e5e5"));
                tv.setBackgroundColor(Color.parseColor("#7b8a9b"));
            }
            ivPullDown.setVisibility(View.GONE);
        } else {
            if (item.isShowPull()){
                ivPullDown.setVisibility(View.VISIBLE);
                for (TextView tv : tvList) {
                    tv.setTextColor(Color.parseColor("#ee3f8e"));
                    tv.setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }else {
                ivPullDown.setVisibility(View.GONE);
                for (TextView tv : tvList) {
                    tv.setTextColor(Color.parseColor("#666666"));
                    tv.setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }
        }
    }
}
