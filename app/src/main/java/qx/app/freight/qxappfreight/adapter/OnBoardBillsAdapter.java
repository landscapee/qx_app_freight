package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;

/**
 * 装机单位置板车上所有运单显示的适配器
 */
public class OnBoardBillsAdapter extends BaseQuickAdapter<LoadingListBean.DataBean.ContentObjectBean.ScooterBean.WaybillBean, BaseViewHolder> {
    public OnBoardBillsAdapter(@Nullable List<LoadingListBean.DataBean.ContentObjectBean.ScooterBean.WaybillBean> data) {
        super(R.layout.item_bill_list_rv, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoadingListBean.DataBean.ContentObjectBean.ScooterBean.WaybillBean item) {
        if (helper.getAdapterPosition()!=0){
            TextView tv1 =helper.getView(R.id.tv_bill_number);
            TextView tv2 =helper.getView(R.id.tv_bill_total);
            TextView tv3 =helper.getView(R.id.tv_bill_weight);
            TextView tv4 =helper.getView(R.id.tv_bill_special_code);

            tv1.setText(item.getWaybillCode());
            tv2.setText(TextUtils.isEmpty(String.valueOf(item.getNumber()))?"- -":String.valueOf(item.getNumber()));
            tv3.setText(TextUtils.isEmpty(String.valueOf(item.getWeight()))?"- -":String.valueOf(item.getWeight()));
            tv4.setText(TextUtils.isEmpty(item.getSpecialCode())?"- -":item.getSpecialCode());

            TextView[] tvList = {tv1, tv2, tv3, tv4};
            if (item.isHasLiveGoods()){//有活体运单时背景设为桃红色
                helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.red_avi));
                for (TextView tv : tvList) {
                    tv.setTextColor(mContext.getResources().getColor(R.color.white));
                }
            }
            else if ((item.isHasGUNGoods())){
                helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                for (TextView tv : tvList) {
                    tv.setTextColor(mContext.getResources().getColor(R.color.white));
                }
            }
            else{
                helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                for (TextView tv : tvList) {
                    tv.setTextColor(mContext.getResources().getColor(R.color.black_3));
                }
            }
        }
    }
}
