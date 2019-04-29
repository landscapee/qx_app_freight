package qx.app.freight.qxappfreight.adapter.loadinglist;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.loadinglist.RegularEntity;

/**
 * 装机单位置左边的适配器
 */
public class LeftRvAdapter extends BaseQuickAdapter<RegularEntity, BaseViewHolder> {
    public LeftRvAdapter(@Nullable List<RegularEntity> data) {
        super(R.layout.item_loading_list_left, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RegularEntity item) {
        Spinner spBerth = helper.getView(R.id.sp_berth);
        Spinner spGoodsPos = helper.getView(R.id.sp_goods_position);
        if (helper.getAdapterPosition() == 0) {//第一条数据是title，下拉view都隐藏
            ((TextView) helper.getView(R.id.tv_berth)).setTextColor(Color.parseColor("#e5e5e5"));
            helper.getView(R.id.tv_berth).setBackgroundColor(Color.parseColor("#7b8a9b"));
            ((TextView) helper.getView(R.id.tv_goods_position)).setTextColor(Color.parseColor("#e5e5e5"));
            helper.getView(R.id.tv_goods_position).setBackgroundColor(Color.parseColor("#7b8a9b"));
            if (TextUtils.isEmpty(item.getGoodsPosition())) {
                helper.getView(R.id.tv_goods_position).setVisibility(View.GONE);
            } else {
                helper.getView(R.id.tv_goods_position).setVisibility(View.VISIBLE);
            }
            spBerth.setVisibility(View.GONE);
            spGoodsPos.setVisibility(View.GONE);
            helper.setText(R.id.tv_berth, item.getBerth());
            helper.setText(R.id.tv_goods_position, item.getGoodsPosition());
        } else {//真实数据
            helper.getView(R.id.tv_berth).setVisibility(View.GONE);
            helper.getView(R.id.tv_goods_position).setVisibility(View.GONE);
            spBerth.setVisibility(View.VISIBLE);
            List<String> berthList = new ArrayList<>();
            berthList.add("请选择");
            berthList.add("1H");
            berthList.add("2H");
            berthList.add("3H");
            berthList.add("4H");
            ArrayAdapter<String> spinnerAdapter;
            if (item.isShowPull()) {
                spinnerAdapter = new ArrayAdapter<>(mContext, R.layout.item_spinner_loading_list_red, berthList);
            } else {
                spinnerAdapter = new ArrayAdapter<>(mContext, R.layout.item_spinner_loading_list_normal, berthList);
            }
            spBerth.setAdapter(spinnerAdapter);
            spBerth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0) {
                        item.setBerth(berthList.get(position));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            if (TextUtils.isEmpty(item.getGoodsPosition())) {//有货位数据
                spGoodsPos.setVisibility(View.GONE);
            } else {
                spGoodsPos.setVisibility(View.VISIBLE);
                List<String> goodsPosList = new ArrayList<>();
                goodsPosList.add("请选择");
                goodsPosList.add("55R");
                goodsPosList.add("66R");
                goodsPosList.add("77R");
                goodsPosList.add("88R");
                ArrayAdapter<String> spGoodsAdapter;
                if (item.isShowPull()) {
                    spGoodsAdapter = new ArrayAdapter<>(mContext, R.layout.item_spinner_loading_list_red, goodsPosList);
                } else {
                    spGoodsAdapter = new ArrayAdapter<>(mContext, R.layout.item_spinner_loading_list_normal, goodsPosList);
                }
                spGoodsPos.setAdapter(spGoodsAdapter);
                spGoodsPos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0) {
                            item.setGoodsPosition(goodsPosList.get(position));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }
}
