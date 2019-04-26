package qx.app.freight.qxappfreight.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.SortingAddActivity;
import qx.app.freight.qxappfreight.bean.InWaybillRecord;

public class SortingInfoAdapter extends BaseQuickAdapter<InWaybillRecord, BaseViewHolder> {

    OnInWaybillRecordDeleteListener onInWaybillRecordDeleteListener;

    public SortingInfoAdapter(@Nullable List<InWaybillRecord> data) {
        super(R.layout.item_sorting_info, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, InWaybillRecord item) {
        helper.setText(R.id.tv_orderid, item.getWaybillCode())//运单号
                .setText(R.id.tv_goods_number, item.getTotalNumberPackages())//总件数
                .setText(R.id.tv_area, item.getWarehouseArea())//库区
                .setText(R.id.tv_postion, item.getWarehouseLocation())
                .setText(R.id.tv_remark, item.getCounterUbnormalGoodsList().get(0).getUbnormalType()[0] + "");//异常原因
        //点击事件：进入修改
        helper.getView(R.id.tv_remark).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SortingAddActivity.class);
                intent.putExtra("TYPE", "UPDATE");
                intent.putExtra("DATA", new Gson().toJson(item));
                mContext.startActivity(intent);
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
    public void setOnInWaybillRecordDeleteListener(OnInWaybillRecordDeleteListener onInWaybillRecordDeleteListener){
        this.onInWaybillRecordDeleteListener = onInWaybillRecordDeleteListener;
    }

    public interface OnInWaybillRecordDeleteListener{
        void onDeleteListener(InWaybillRecord inWaybillRecord);
    }
}
