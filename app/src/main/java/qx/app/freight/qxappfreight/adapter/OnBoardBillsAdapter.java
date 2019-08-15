package qx.app.freight.qxappfreight.adapter;

import android.graphics.Color;
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
            ((TextView)helper.getView(R.id.tv_bill_number)).setText(String.valueOf(item.getWaybillCode()));
            ((TextView)helper.getView(R.id.tv_bill_total)).setText(String.valueOf(item.getNumber()));
            ((TextView)helper.getView(R.id.tv_bill_weight)).setText(String.valueOf(item.getWeight()));
            ((TextView)helper.getView(R.id.tv_bill_special_code)).setText(TextUtils.isEmpty(item.getSpecialCode())?"- -":item.getSpecialCode());
            if (item.isHasLiveGoods()){
                helper.itemView.setBackgroundColor(Color.parseColor("#ee3f8e"));
            }
        }
    }
}
