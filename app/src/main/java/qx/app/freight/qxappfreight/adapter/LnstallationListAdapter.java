package qx.app.freight.qxappfreight.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.LnstallationInfoBean;

/**
 * 货邮舱单列表数据适配器
 */
public class LnstallationListAdapter extends BaseQuickAdapter<LnstallationInfoBean.ScootersBean, BaseViewHolder> {

    public LnstallationListAdapter(@Nullable List<LnstallationInfoBean.ScootersBean> list) {
        super(R.layout.item_manifest_scooter, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, LnstallationInfoBean.ScootersBean item) {
        String type = "邮件";
        if ("C".equals(item.getType())) {
            type = "货物";
        } else if ("M".equals(item.getType())) {
            type = "邮件";
        } else if ("B".equals(item.getType())) {
            type = "行李";
        } else if ("T".equals(item.getType())) {
            type = "转港行李";
        } else if ("BY".equals(item.getType())) {
            type = "Y舱行李";
        } else if ("BT".equals(item.getType())) {
            type = "过站行李";
        } else if ("CT".equals(item.getType())) {
            type = "过站货物";
        } else if ("X".equals(item.getType())) {
            type = "空集装箱";
        }
        helper.setText(R.id.tv_manifest, item.getCargoName())
                .setText(R.id.tv_goods_position, item.getGoodsPosition())
                .setText(R.id.tv_scooter_number, item.getScooterCode() == null ? "- -" : item.getScooterCode())
                .setText(R.id.tv_uld_number, item.getUldCode() == null ? "- -" : item.getUldCode())
                .setText(R.id.tv_to_city, item.getDestinationStation() == null ? "- -" : item.getDestinationStation())
                .setText(R.id.tv_type,type)
                .setText(R.id.tv_weight, item.getWeight() == null ? "- -" : item.getWeight())
                .setText(R.id.tv_total, item.getTotal() == null ? "-  -" : item.getTotal())
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
//            helper.getView(R.id.sp_berth).setVisibility(View.GONE);
            helper.itemView.setBackgroundColor(Color.parseColor("#2E81FD"));
            for (TextView tv : tvList) {
                tv.setTextColor(Color.parseColor("#ffffff"));
            }
        } else {
            helper.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            for (TextView tv : tvList) {
                tv.setTextColor(Color.parseColor("#000000"));
            }
//            tv1.setVisibility(View.GONE);
//            helper.getView(R.id.sp_berth).setVisibility(View.VISIBLE);
//            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(mContext, R.layout.item_spinner_loading_list_red, item.getManifestList());
//            ((Spinner) helper.getView(R.id.sp_berth)).setAdapter(spinnerAdapter);

        }

    }
}
