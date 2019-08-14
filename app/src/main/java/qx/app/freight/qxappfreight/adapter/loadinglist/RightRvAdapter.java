package qx.app.freight.qxappfreight.adapter.loadinglist;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.loadinglist.ScrollEntity;
import qx.app.freight.qxappfreight.utils.StringUtil;

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
        TextView[] tvList = {tvBoardNumber, tvUldNumber, tvTarget, tvType, tvWeight};
        TextView tvPullDownTitle = helper.getView(R.id.tv_pull_title);
        TextView tvPullDown = helper.getView(R.id.tv_pull);
        helper.setText(R.id.tv_board_number, StringUtil.toText(String.valueOf(item.getBoardNumber()))).setText(R.id.tv_uld_number, StringUtil.toText(item.getUldNumber()))
                .setText(R.id.tv_target_name, StringUtil.toText(item.getTarget())).setText(R.id.tv_weight, StringUtil.toText(String.valueOf(item.getWeight()))).setText(R.id.tv_type, StringUtil.toText(item.getType()));
        if (helper.getAdapterPosition() == 0) {//title栏显示拉下文字，
            for (TextView tv : tvList) {
                tv.setTextColor(Color.parseColor("#e5e5e5"));
                tv.setBackgroundColor(Color.parseColor("#7b8a9b"));
            }
            tvPullDownTitle.setVisibility(View.VISIBLE);
            tvPullDown.setVisibility(View.GONE);
        } else {
            tvPullDownTitle.setVisibility(View.GONE);
            tvPullDown.setVisibility(View.VISIBLE);
                for (TextView tv : tvList) {
                    tv.setTextColor(Color.parseColor("#666666"));
                    tv.setBackgroundColor(Color.parseColor("#ffffff"));
            }
                tvPullDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       if (item.isPull()){
                           item.setPull(false);
                           tvPullDown.setTextColor(Color.parseColor("#666"));
                       }else {
                           item.setPull(true);
                           tvPullDown.setTextColor(Color.parseColor("#ee3f8e"));
                       }
                    }
                });
        }
    }
}
