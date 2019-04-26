package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.SortingAddActivity;
import qx.app.freight.qxappfreight.bean.CounterUbnormalGoods;
import qx.app.freight.qxappfreight.bean.InWaybillRecord;

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
                helper.setText(R.id.tv_remark, typeToString(typeTemp) + " ");//异常原因
            } else {
                helper.setText(R.id.tv_remark, "");//异常原因
            }
        } else {
            helper.setText(R.id.tv_remark, "");//异常原因
        }

        //点击事件：进入修改
        helper.getView(R.id.tv_remark).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, SortingAddActivity.class);
//                intent.putExtra("TYPE", "UPDATE");
//                intent.putExtra("DATA", item);
//                intent.putExtra("INDEX", helper.getAdapterPosition());
//                mContext.startActivity(intent);
            }
        });
        //左滑菜单, 要删除的啦
        helper.getView(R.id.rll_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInWaybillRecordDeleteListener.onDeleteListener(item);
            }
        });


    }

    //删除监听事件
    public void setOnInWaybillRecordDeleteListener(OnInWaybillRecordDeleteListener onInWaybillRecordDeleteListener) {
        this.onInWaybillRecordDeleteListener = onInWaybillRecordDeleteListener;
    }

    public interface OnInWaybillRecordDeleteListener {
        void onDeleteListener(InWaybillRecord inWaybillRecord);
    }

    private String typeToString(int type) {
        String result = "未知类型";
        switch (type) {
            case 1:
                result = "件数异常";
                break;
            case 2:
                result = "有单无货";
                break;
            case 3:
                result = "破损";
                break;
            case 4:
                result = "腐烂";
                break;
            case 5:
                result = "有货无单";
                break;
            case 6:
                result = "泄露";
                break;
            case 7:
                result = "错运";
                break;
            case 8:
                result = "扣货";
                break;
            case 9:
                result = "无标签";
                break;
            case 10:
                result = "对方未发报文";
                break;
            case 11:
                result = "其他";
                break;
            case 12:
                result = "死亡";
                break;
            case 13:
                result = "多收邮路单";
                break;
            case 14:
                result = "多收货运单";
                break;
            case 15:
                result = "多收邮袋";
                break;
            case 16:
                result = "有邮袋无邮路单";
                break;
            case 17:
                result = "少收货运单";
                break;
            case 18:
                result = "有邮路单无邮袋";
                break;
        }
        return result;
    }
}
