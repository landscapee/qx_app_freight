package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.CounterUbnormalGoods;
import qx.app.freight.qxappfreight.bean.InWaybillRecord;
import qx.app.freight.qxappfreight.utils.ExceptionUtils;

/**
 * 进港分拣 - 信息列表适配器
 *
 * create by guohao - 2019/4/25
 *
 */
public class SortingInfoAdapter extends BaseQuickAdapter<InWaybillRecord, BaseViewHolder> {

    OnInWaybillRecordDeleteListener onInWaybillRecordDeleteListener;

    public SortingInfoAdapter(@Nullable List<InWaybillRecord> data) {
        super(R.layout.item_sorting_info, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, InWaybillRecord item) {
        if (item.getWaybillCode() == "" || item.getWaybillCode() == null) {
            helper.setText(R.id.tv_orderid, "无");//运单号
        } else {
            helper.setText(R.id.tv_orderid, item.getWaybillCode());//运单号
        }
        helper.setText(R.id.tv_goods_number, item.getTotalNumberPackages() + "")//总件数
                .setText(R.id.tv_area, item.getWarehouseArea())//库区
                .setText(R.id.tv_postion, item.getWarehouseLocation());
        if (item.getCounterUbnormalGoodsList() != null && item.getCounterUbnormalGoodsList().size() > 0) {
            CounterUbnormalGoods counterUbnormalGoodsTemp = item.getCounterUbnormalGoodsList().get(0);
            if (counterUbnormalGoodsTemp.getUbnormalType() != null && counterUbnormalGoodsTemp.getUbnormalType().size() > 0) {
                int typeTemp = counterUbnormalGoodsTemp.getUbnormalType().get(0);
                helper.setText(R.id.tv_remark, ExceptionUtils.typeToString(typeTemp) + " ");//异常原因
            } else {
                helper.setText(R.id.tv_remark, "");//异常原因
            }
        } else {
            helper.setText(R.id.tv_remark, "");//异常原因
        }
        //左滑菜单, 要删除的啦
        helper.getView(R.id.rll_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInWaybillRecordDeleteListener.onDeleteListener(helper.getAdapterPosition());
            }
        });


    }

    //删除监听事件
    public void setOnInWaybillRecordDeleteListener(OnInWaybillRecordDeleteListener onInWaybillRecordDeleteListener) {
        this.onInWaybillRecordDeleteListener = onInWaybillRecordDeleteListener;
    }

    public interface OnInWaybillRecordDeleteListener {
        void onDeleteListener(int postion);
    }

}
