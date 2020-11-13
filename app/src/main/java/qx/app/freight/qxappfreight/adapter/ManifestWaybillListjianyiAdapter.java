package qx.app.freight.qxappfreight.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.ManifestScooterListBean;
import qx.app.freight.qxappfreight.constant.Constants;

/**
 * 装舱建议列表数据适配器
 */
public class ManifestWaybillListjianyiAdapter extends BaseQuickAdapter <ManifestScooterListBean, BaseViewHolder> {

    private int mWidthairflag;

    private boolean notShowPull;

    public ManifestWaybillListjianyiAdapter(@Nullable List <ManifestScooterListBean> list, int widthairflag) {
        super(R.layout.item_manifest_jianyi, list);
        this.mWidthairflag = widthairflag;
    }

    public ManifestWaybillListjianyiAdapter(@Nullable List <ManifestScooterListBean> list, int widthairflag, boolean notShowPull) {
        super(R.layout.item_manifest_jianyi, list);
        this.mWidthairflag = widthairflag;
        this.notShowPull = notShowPull;
    }

    @Override
    protected void convert(BaseViewHolder helper, ManifestScooterListBean item) {
        if (1 == mWidthairflag) { //窄体机
            helper.getView(R.id.tv_model).setVisibility(View.GONE);
//            helper.getView(R.id.tv_volume).setVisibility(View.GONE);
            helper.setText(R.id.tv_suggestRepository, item.getSuggestRepository() != null ? item.getSuggestRepository() : "--")
                    .setText(R.id.tv_scooter_number, item.getScooterCode() != null ? item.getScooterCode() : "--")
                    .setText(R.id.tv_total, item.getTotal() != null ? item.getTotal() + "" : "--")
                    .setText(R.id.tv_volume, item.getVolume() != null ? item.getVolume() + "" : "--")
                    .setText(R.id.tv_weight, item.getWeight() != null ? item.getWeight() + "" : "--")
                    .setText(R.id.tv_specialCode, item.getSpecialNumber() != null ? item.getSpecialNumber() : "--");
            if (item.getWaybillList() != null && item.getWaybillList().size() > 0) {
                helper.setText(R.id.tv_mailtype, item.getWaybillList()!=null&&item.getWaybillList().size()>0&&item.getWaybillList().get(0).getWaybillCode().contains("xxx") ? "X" : item.getMailType() != null ? item.getMailType() : "--");
            } else {
                helper.setText(R.id.tv_mailtype, item.getMailType() != null ? item.getMailType() : "--");
            }
            Button btnPull = helper.getView(R.id.tv_pull);
            if (notShowPull){
                btnPull.setVisibility(View.VISIBLE);
                //设置 拉下的状态
                btnPull.setOnClickListener(v -> {
                    item.setPull(!item.isPull());
                    if (item.isPull()){
                        btnPull.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                    }
                    else{
                        btnPull.setTextColor(ContextCompat.getColor(mContext,R.color.black_3));
                    }
                });
                if (item.isPull()){
                    btnPull.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                }
                else{
                    btnPull.setTextColor(ContextCompat.getColor(mContext,R.color.black_3));
                }
            }
            else {
                btnPull.setVisibility(View.GONE);
            }


            TextView tv1 = helper.getView(R.id.tv_scooter_number);
            TextView tv2 = helper.getView(R.id.tv_total);
            TextView tv3 = helper.getView(R.id.tv_weight);
            TextView tv4 = helper.getView(R.id.tv_volume);
            TextView tv5 = helper.getView(R.id.tv_suggestRepository);
            TextView tv6 = helper.getView(R.id.tv_specialCode);
            TextView tv7 = helper.getView(R.id.tv_mailtype);
//            TextView tv7 = helper.getView(R.id.tv_volume);
            TextView[] tvList = {tv1, tv2, tv3, tv4, tv5, tv6, tv7};
            if (helper.getAdapterPosition() == 0) {
                btnPull.setVisibility(View.GONE);
                helper.itemView.setBackgroundColor(Color.parseColor("#2E81FD"));
                for (TextView tv : tvList) {
                    tv.setTextColor(Color.parseColor("#ffffff"));
                }
            } else {
                if (item.getSpecialNumber() != null && "AVI".equals(item.getSpecialNumber())) {//活体颜色标注
                    helper.itemView.setBackgroundColor(Color.parseColor("#c68a9e"));
                    for (TextView tv : tvList) {
                        tv.setTextColor(Color.parseColor("#000000"));
                    }
                } else if (item.getSpecialNumber() != null && item.getSpecialNumber().equals(Constants.DANGER)) {//枪支
                    helper.itemView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.orangered));
                    for (TextView tv : tvList) {
                        tv.setTextColor(Color.parseColor("#000000"));
                    }
                } else if (item.getWaybillList()!=null&&item.getWaybillList().size()>0&&item.getWaybillList().get(0).getCargoCn() != null && item.getWaybillList().get(0).getCargoCn().equals(Constants.YCS)) {//压舱沙
                    helper.itemView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.green));
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
        } else if (0 == mWidthairflag) {//宽体机
            helper.getView(R.id.tv_suggestRepository).setVisibility(View.GONE);
            helper.getView(R.id.tv_model).setVisibility(View.VISIBLE);
            TextView tv1 = helper.getView(R.id.tv_model);
            TextView tv2 = helper.getView(R.id.tv_total);
            TextView tv3 = helper.getView(R.id.tv_weight);
            TextView tv4 = helper.getView(R.id.tv_volume);
            TextView tv5 = helper.getView(R.id.tv_specialCode);
            TextView tv6 = helper.getView(R.id.tv_mailtype);
            TextView tv7 = helper.getView(R.id.tv_scooter_number);
            Button btnPull = helper.getView(R.id.tv_pull);
            if (notShowPull){
                btnPull.setVisibility(View.VISIBLE);
                //设置 拉下的状态
                btnPull.setOnClickListener(v -> {
                    if (item.isPull()){
                        btnPull.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                    }
                    else{
                        btnPull.setTextColor(ContextCompat.getColor(mContext,R.color.black_3));
                    }
                    item.setPull(!item.isPull());

                });
                if (item.isPull()){
                    btnPull.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                }
                else{
                    btnPull.setTextColor (ContextCompat.getColor(mContext,R.color.black_3));
                }
            }
            else {
                btnPull.setVisibility(View.GONE);
            }



            TextView[] tvList = {tv1, tv2, tv3, tv4, tv5, tv6, tv7};
            if (helper.getAdapterPosition() == 0) {
                btnPull.setVisibility(View.GONE);
                helper.itemView.setBackgroundColor(Color.parseColor("#2E81FD"));
                for (TextView tv : tvList) {
                    tv.setTextColor(Color.parseColor("#ffffff"));
                }
                helper.setText(R.id.tv_model, "型号")
                        .setText(R.id.tv_scooter_number, "集装箱板号")
                        .setText(R.id.tv_total, item.getTotal())
                        .setText(R.id.tv_volume, item.getVolume())
                        .setText(R.id.tv_weight, item.getWeight())
                        .setText(R.id.tv_specialCode, item.getSpecialNumber())
                        .setText(R.id.tv_mailtype, item.getMailType());
            } else {
                helper.setText(R.id.tv_model, item.getUldType() != null ? item.getUldType() : item.getScooterCode() != null ? "散货" : "--")
                        .setText(R.id.tv_scooter_number, item.getUldCode() != null ? item.getUldCode() + item.getIata() : item.getScooterCode() != null ? item.getScooterCode() : "--")
                        .setText(R.id.tv_total, item.getTotal() != null ? item.getTotal() + "" : "--")
                        .setText(R.id.tv_volume, item.getVolume() != null ? item.getVolume() + "" : "--")
                        .setText(R.id.tv_weight, item.getWeight() != null ? item.getWeight() + "" : "--")
                        .setText(R.id.tv_specialCode, item.getSpecialNumber() != null ? item.getSpecialNumber() : "--")
                        .setText(R.id.tv_mailtype, item.getMailType() != null ? item.getMailType() : "--");
                if (item.getSpecialNumber() != null && "AVI".equals(item.getSpecialNumber())) {//活体颜色标注
                    helper.itemView.setBackgroundColor(Color.parseColor("#c68a9e"));
                    for (TextView tv : tvList) {
                        tv.setTextColor(Color.parseColor("#000000"));
                    }
                } else if (item.getSpecialNumber() != null && item.getSpecialNumber().equals(Constants.DANGER)) {//枪支
                    helper.itemView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.red));
                    for (TextView tv : tvList) {
                        tv.setTextColor(Color.parseColor("#000000"));
                    }
                }
                else if (item.getWaybillList()!=null&&item.getWaybillList().size()>0&&item.getWaybillList().get(0).getCargoCn() != null && item.getWaybillList().get(0).getCargoCn().equals(Constants.YCS)) {//压舱沙
                    helper.itemView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.green));
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
