package qx.app.freight.qxappfreight.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.LnstallationInfoBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.utils.StringUtil;

/**
 * 货邮舱单列表数据适配器
 */
public class LnstallationListAdapter extends BaseQuickAdapter <LnstallationInfoBean.ScootersBean, BaseViewHolder> {

    private boolean showAdjust;

    public LnstallationListAdapter(@Nullable List <LnstallationInfoBean.ScootersBean> list) {
        super(R.layout.item_manifest_scooter, list);
    }

    public LnstallationListAdapter(@Nullable List <LnstallationInfoBean.ScootersBean> list, boolean showAdjust) {
        super(R.layout.item_manifest_scooter, list);
        this.showAdjust = showAdjust;
    }

    @Override
    protected void convert(BaseViewHolder helper, LnstallationInfoBean.ScootersBean item) {
//        String type = "邮件";
//        if ("C".equals(item.getType())) {
//            type = "货物";
//        } else if ("M".equals(item.getType())) {
//            type = "邮件";
//        } else if ("B".equals(item.getType())) {
//            type = "行李";
//        } else if ("T".equals(item.getType())) {
//            type = "转港行李";
//        } else if ("BY".equals(item.getType())) {
//            type = "Y舱行李";
//        } else if ("BT".equals(item.getType())) {
//            type = "过站行李";
//        } else if ("CT".equals(item.getType())) {
//            type = "过站货物";
//        } else if ("X".equals(item.getType())) {
//            type = "空集装箱";
//        }
        TextView tvAdjust = helper.getView(R.id.tv_adjust);
        if (showAdjust){
            tvAdjust.setVisibility(View.VISIBLE);
        }
        else
            tvAdjust.setVisibility(View.GONE);

        if (helper.getAdapterPosition() == 0) {

            helper.setText(R.id.tv_manifest, item.getCargoName() == null ? "- -" : item.getCargoName())
                    .setText(R.id.tv_adjust, "调整舱位")
                    .setText(R.id.tv_goods_position, item.getLocation())
                    .setText(R.id.tv_scooter_number, item.getScooterCode() == null ? "- -" : item.getScooterCode())
                    .setText(R.id.tv_uld_number, item.getSerialInd() == null ? "- -" : item.getSerialInd())
                    .setText(R.id.tv_to_city, item.getDestinationStation() == null ? "- -" : item.getDestinationStation())
                    .setText(R.id.tv_type, item.getType() == null ? "- -" : item.getType())
                    .setText(R.id.tv_weight, item.getWeight() == null ? "- -" : item.getWeight())
                    .setText(R.id.tv_total, item.getTotal() == null ? "- -" : item.getTotal())
                    .setText(R.id.tv_special_number, item.getSpecialCode() == null ? "- -" : item.getSpecialCode())
                    .setText(R.id.tv_pull_state, item.getExceptionFlag() == 1 ? "状态" : "- -");
        } else {

            helper.setText(R.id.tv_manifest, !StringUtil.isEmpty(item.getOldCargoName()) ? item.getOldCargoName(): StringUtil.isEmpty(item.getCargoName()) ? "- -" : item.getCargoName() )
                    .setText(R.id.tv_goods_position, StringUtil.isEmpty(item.getLocation()) ? "- -" : item.getLocation())
                    .setText(R.id.tv_adjust, item.isChange()||item.isSplit()? StringUtil.isEmpty(item.getCargoName()) ? "- -" : item.getCargoName():"- -")
                    .setText(R.id.tv_scooter_number, StringUtil.isEmpty(item.getScooterCode()) ? "- -" : item.getScooterCode())
                    .setText(R.id.tv_uld_number, StringUtil.isEmpty(item.getSerialInd()) ? "- -" : item.getSerialInd())
                    .setText(R.id.tv_to_city, StringUtil.isEmpty(item.getDestinationStation()) ? "- -" : item.getDestinationStation())
                    .setText(R.id.tv_type, StringUtil.isEmpty(item.getType()) ? "- -" : item.getType())
                    .setText(R.id.tv_weight, StringUtil.isEmpty(item.getWeight()) ? "- -" : item.getWeight())
                    .setText(R.id.tv_total, !StringUtil.isEmpty(item.getSpecialCode())&&item.getSpecialCode().contains("/") ? item.getSpecialCode().substring(0,item.getSpecialCode().indexOf("/")) : "--")
                    .setText(R.id.tv_special_number, StringUtil.isEmpty(item.getSpecialCode()) ? "- -" : item.getSpecialCode())
                    .setText(R.id.tv_pull_state, item.getExceptionFlag() == 1 ? "拉下" : item.isChange() ? "调舱" : item.isSplit()?item.getType().endsWith("BY")?"拆舱":"拆箱":"- -");
        }


        TextView tv1 = helper.getView(R.id.tv_manifest);
        TextView tv2 = helper.getView(R.id.tv_scooter_number);
        TextView tv3 = helper.getView(R.id.tv_uld_number);
        TextView tv4 = helper.getView(R.id.tv_to_city);
        TextView tv5 = helper.getView(R.id.tv_type);
        TextView tv6 = helper.getView(R.id.tv_weight);
        TextView tv7 = helper.getView(R.id.tv_total);
        TextView tv8 = helper.getView(R.id.tv_goods_position);
        TextView tv9 = helper.getView(R.id.tv_special_number);
        TextView tv10 = helper.getView(R.id.tv_pull_state);
        TextView[] tvList = {tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10,tvAdjust};


        if (helper.getAdapterPosition() == 0) {
//            helper.getView(R.id.sp_berth).setVisibility(View.GONE);
            helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.blue_2e8));
            for (TextView tv : tvList) {
                tv.setTextColor(mContext.getResources().getColor(R.color.login_txt));
            }
        } else {
            tv1.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);
            tv3.setVisibility(View.VISIBLE);
            if (item.getExceptionFlag() == 1) {
                helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                for (TextView tv : tvList) {
                    tv.setTextColor(mContext.getResources().getColor(R.color.login_txt));
                }
            } else if (item.isChange()) {
                helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.yellow));
                for (TextView tv : tvList) {
                    tv.setTextColor(mContext.getResources().getColor(R.color.black_3));
                }
            }
            else if (item.isSplit()) {
                helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.gray_cc));
                if (helper.getPosition() != 0){
                    tv1.setVisibility(View.INVISIBLE);
                    tv2.setVisibility(View.INVISIBLE);
                    tv3.setVisibility(View.INVISIBLE);
                }
                for (TextView tv : tvList) {
                    tv.setTextColor(mContext.getResources().getColor(R.color.black_3));
                }
            }
            else {
                if (item.getSpecialCode() != null && item.getSpecialCode().contains("AVI")) {//活体颜色标注
                    helper.itemView.setBackgroundColor(Color.parseColor("#c68a9e"));
                    for (TextView tv : tvList) {
                        tv.setTextColor(Color.parseColor("#000000"));
                    }
                } else if (item.getSpecialCode() != null && item.getSpecialCode().contains(Constants.DANGER)) {//枪支
                    helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.orangered));
                    for (TextView tv : tvList) {
                        tv.setTextColor(Color.parseColor("#000000"));
                    }
                }
                else if (item.getWaybillList() != null &&item.getWaybillList().size()>0&&item.getWaybillList().get(0)!=null&&item.getWaybillList().get(0).getCargoCn()!=null&& item.getWaybillList().get(0).getCargoCn().equals(Constants.YCS)){//压舱沙
                    helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.green));
                    for (TextView tv : tvList) {
                        tv.setTextColor(Color.parseColor("#000000"));
                    }
                }else {
                    helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                    for (TextView tv : tvList) {
                        tv.setTextColor(mContext.getResources().getColor(R.color.black_3));
                    }
                }
            }
        }
    }
}
