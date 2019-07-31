package qx.app.freight.qxappfreight.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.ManifestScooterListBean;
import qx.app.freight.qxappfreight.bean.response.InventoryQueryBean;
import qx.app.freight.qxappfreight.utils.TimeUtils;

/**
 * 货邮舱单列表数据适配器
 */
public class ManifestScooterListAdapter extends BaseQuickAdapter<ManifestScooterListBean, BaseViewHolder> {
    private boolean mCanCommit;

    private ManifestScooterListAdapter(@Nullable List<ManifestScooterListBean> list) {
        super(R.layout.item_manifest_scooter, list);
    }

    public ManifestScooterListAdapter(@Nullable List<ManifestScooterListBean> list, boolean canCommit) {
        this(list);
        mCanCommit = canCommit;
    }

    @Override
    protected void convert(BaseViewHolder helper, ManifestScooterListBean item) {
        String type = "邮件";
        if ("C".equals(item.getMailType())) {
            type = "货物";
        } else if ("BY".equals(item.getMailType())) {
            type = "行李";
        } else if ("类型".equals(item.getMailType())) {
            type = "类型";
        }
        helper.setText(R.id.tv_manifest, item.getSuggestRepository()).setText(R.id.tv_goods_position, item.getGoodsPosition())
                .setText(R.id.tv_scooter_number, item.getScooterCode()).setText(R.id.tv_uld_number, item.getUldCode())
                .setText(R.id.tv_to_city, item.getToCity()).setText(R.id.tv_type, type)
                .setText(R.id.tv_weight, String.valueOf(item.getWeight())).setText(R.id.tv_total, String.valueOf(item.getTotal()))
                .setText(R.id.tv_special_number, item.getSpecialNumber());
        TextView tv1 = helper.getView(R.id.tv_manifest);
        TextView tv2 = helper.getView(R.id.tv_scooter_number);
        TextView tv3 = helper.getView(R.id.tv_uld_number);
        TextView tv4 = helper.getView(R.id.tv_to_city);
        TextView tv5 = helper.getView(R.id.tv_type);
        TextView tv6 = helper.getView(R.id.tv_weight);
        TextView tv7 = helper.getView(R.id.tv_total);
        TextView tv8 = helper.getView(R.id.tv_goods_position);
        TextView tv9 = helper.getView(R.id.tv_special_number);
        TextView[] tvList = {tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9};
        if (helper.getAdapterPosition() == 0) {
            helper.itemView.setBackgroundColor(Color.parseColor("#2E81FD"));
            for (TextView tv : tvList) {
                tv.setTextColor(Color.parseColor("#ffffff"));
            }
        } else {
            helper.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            for (TextView tv : tvList) {
                tv.setTextColor(Color.parseColor("#000000"));
            }
        }
        if (mCanCommit) {
            helper.getView(R.id.sp_berth).setVisibility(View.VISIBLE);
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(mContext, R.layout.item_spinner_loading_list_red, item.getManifestList());
            ((Spinner) helper.getView(R.id.sp_berth)).setAdapter(spinnerAdapter);
        } else {
            helper.getView(R.id.sp_berth).setVisibility(View.GONE);
        }
    }
}
