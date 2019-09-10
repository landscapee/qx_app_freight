package qx.app.freight.qxappfreight.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.ManifestScooterListBean;

/**
 * 货邮舱单列表数据适配器（显示运单数据）
 */
public class ManifestWaybillListAdapter extends BaseQuickAdapter<ManifestScooterListBean.WaybillListBean, BaseViewHolder> {

    public ManifestWaybillListAdapter(@Nullable List<ManifestScooterListBean.WaybillListBean> list) {
        super(R.layout.item_manifest_waybill, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, ManifestScooterListBean.WaybillListBean item) {
        helper.setText(R.id.tv_waybill_number, item.getWaybillCode()).setText(R.id.tv_weight, String.valueOf(item.getWeight()))
                .setText(R.id.tv_routeen,item.getRouteEn())
                .setText(R.id.tv_specialCode, item.getSpecialCode())
                .setText(R.id.tv_mailtype, item.getCargoCn())
                .setText(R.id.tv_suggestRepository, item.getInfo())
                .setText(R.id.tv_total, String.valueOf(item.getNumber()))
//                .setText(R.id.tv_volume, String.valueOf(item.getVolume()))
        ;

        TextView tv1 = helper.getView(R.id.tv_waybill_number);
        TextView tv2 = helper.getView(R.id.tv_routeen);
        TextView tv3 = helper.getView(R.id.tv_total);
        TextView tv4 = helper.getView(R.id.tv_weight);
//        TextView tv5 = helper.getView(R.id.tv_volume);
        TextView tv5 = helper.getView(R.id.tv_specialCode);
        TextView tv6 = helper.getView(R.id.tv_mailtype);
        TextView tv7 = helper.getView(R.id.tv_suggestRepository);
        TextView[] tvList = {tv1, tv2, tv3, tv4, tv5, tv6, tv7};
        if (helper.getAdapterPosition() == 0) {
            helper.itemView.setBackgroundColor(Color.parseColor("#2E81FD"));
            for (TextView tv : tvList) {
                tv.setTextColor(Color.parseColor("#ffffff"));
            }
        } else {
            if (item.getSpecialCode() != null && item.getSpecialCode().equals("AVI")) {//活体颜色标注
                helper.itemView.setBackgroundColor(Color.parseColor("#c68a9e"));
                for (TextView tv : tvList) {
                    tv.setTextColor(Color.parseColor("#000000"));
                }
            } else {
                helper.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                for (TextView tv : tvList) {
                    tv.setTextColor(Color.parseColor("#000000"));
                }
            }
        }
    }
}
