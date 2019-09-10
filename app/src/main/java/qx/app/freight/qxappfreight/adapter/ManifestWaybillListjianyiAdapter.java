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
 * 装舱建议列表数据适配器
 */
public class ManifestWaybillListjianyiAdapter extends BaseQuickAdapter<ManifestScooterListBean, BaseViewHolder> {

    private int mWidthairflag;

    public ManifestWaybillListjianyiAdapter(@Nullable List<ManifestScooterListBean> list, int widthairflag) {
        super(R.layout.item_manifest_jianyi, list);
        mWidthairflag = widthairflag;
    }

    @Override
    protected void convert(BaseViewHolder helper, ManifestScooterListBean item) {
        if (1 == mWidthairflag) {
            helper.getView(R.id.tv_model).setVisibility(View.GONE);
            helper.getView(R.id.tv_volume).setVisibility(View.GONE);
            helper.setText(R.id.tv_suggestRepository, item.getSuggestRepository()!=null ? item.getSuggestRepository():"--")
                    .setText(R.id.tv_scooter_number, item.getScooterCode()!=null ? item.getScooterCode():"--")
                    .setText(R.id.tv_total, item.getTotal()!=null ? item.getTotal()+"":"--")
                    .setText(R.id.tv_weight,  item.getWeight()!=null ? item.getWeight()+"":"--")
                    .setText(R.id.tv_specialCode, item.getSpecialNumber()!=null ? item.getSpecialNumber():"--")
                    .setText(R.id.tv_mailtype, item.getMailType()!=null ? item.getMailType():"--");
            TextView tv1 = helper.getView(R.id.tv_scooter_number);
            TextView tv2 = helper.getView(R.id.tv_total);
            TextView tv3 = helper.getView(R.id.tv_weight);
            TextView tv4 = helper.getView(R.id.tv_volume);
            TextView tv5 = helper.getView(R.id.tv_suggestRepository);
            TextView tv6 = helper.getView(R.id.tv_specialCode);
            TextView tv7 = helper.getView(R.id.tv_mailtype);
//            TextView tv7 = helper.getView(R.id.tv_volume);
            TextView[] tvList = {tv1, tv2, tv3, tv4, tv5, tv6,tv7};
            if (helper.getAdapterPosition() == 0) {
                helper.itemView.setBackgroundColor(Color.parseColor("#2E81FD"));
                for (TextView tv : tvList) {
                    tv.setTextColor(Color.parseColor("#ffffff"));
                }
            } else {
                if (item.getSpecialNumber() != null && item.getSpecialNumber().equals("AVI")) {//活体颜色标注
                    helper.itemView.setBackgroundColor(Color.parseColor("#c68a9e"));
                    for (TextView tv : tvList) {
                        tv.setTextColor(Color.parseColor("#000000"));
                    }
                }
                else if (item.getSpecialNumber() != null && item.getSpecialNumber().equals("GUN")){//枪支
                    helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                    for (TextView tv : tvList) {
                        tv.setTextColor(Color.parseColor("#000000"));
                    }
                }else {
                    helper.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                    for (TextView tv : tvList) {
                        tv.setTextColor(Color.parseColor("#000000"));
                    }
                }
            }
        } else if (0 == mWidthairflag) {
            helper.getView(R.id.tv_suggestRepository).setVisibility(View.GONE);
            helper.getView(R.id.tv_model).setVisibility(View.VISIBLE);
            TextView tv1 = helper.getView(R.id.tv_model);
            TextView tv2 = helper.getView(R.id.tv_total);
            TextView tv3 = helper.getView(R.id.tv_weight);
            TextView tv4 = helper.getView(R.id.tv_volume);
            TextView tv5 = helper.getView(R.id.tv_specialCode);
            TextView tv6 = helper.getView(R.id.tv_mailtype);
            TextView[] tvList = {tv1, tv2, tv3, tv4, tv5,tv6};
            if (helper.getAdapterPosition() == 0) {
                helper.itemView.setBackgroundColor(Color.parseColor("#2E81FD"));
                for (TextView tv : tvList) {
                    tv.setTextColor(Color.parseColor("#ffffff"));
                }
                helper.setText(R.id.tv_model, "型号")
                        .setText(R.id.tv_scooter_number, "集装箱板号")
                        .setText(R.id.tv_total, item.getTotal())
                        .setText(R.id.tv_weight,  item.getWeight())
                        .setText(R.id.tv_specialCode, item.getSpecialNumber())
                        .setText(R.id.tv_mailtype, item.getMailType());
            } else {
                helper.setText(R.id.tv_model, item.getUldType()!=null?item.getUldType():item.getScooterCode()!=null ? "散货":"--")
                        .setText(R.id.tv_scooter_number, item.getUldCode()!=null?item.getUldCode():item.getScooterCode()!=null ? item.getScooterCode():"--")
                        .setText(R.id.tv_total, item.getTotal()!=null ? item.getTotal()+"":"--")
                        .setText(R.id.tv_weight,  item.getWeight()!=null ? item.getWeight()+"":"--")
                        .setText(R.id.tv_specialCode, item.getSpecialNumber()!=null ? item.getSpecialNumber():"--")
                        .setText(R.id.tv_mailtype, item.getMailType()!=null ? item.getMailType():"--");
                if (item.getSpecialNumber() != null && item.getSpecialNumber().equals("AVI")) {//活体颜色标注
                    helper.itemView.setBackgroundColor(Color.parseColor("#c68a9e"));
                    for (TextView tv : tvList) {
                        tv.setTextColor(Color.parseColor("#000000"));
                    }
                }
                else if (item.getSpecialNumber() != null && item.getSpecialNumber().equals("GUN")){//枪支
                    helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                    for (TextView tv : tvList) {
                        tv.setTextColor(Color.parseColor("#000000"));
                    }
                }
                else {
                    helper.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                    for (TextView tv : tvList) {
                        tv.setTextColor(Color.parseColor("#000000"));
                    }
                }
            }
        }
    }
}
