package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.request.InventoryDetailEntity;

public class InventoryInfoAdapter extends BaseQuickAdapter<InventoryDetailEntity, BaseViewHolder> {
    private InventoryInfoListener listener;
    private int num;
    public InventoryInfoAdapter(@Nullable List<InventoryDetailEntity> data,int num) {
        super(R.layout.item_inventory_info, data);
        this.num = num;
    }

    @Override
    protected void convert(BaseViewHolder helper, InventoryDetailEntity item) {


        helper.setText(R.id.tv_order_number,(helper.getAdapterPosition()+1)+"")
                .setText(R.id.tv_waybill_code,item.getWaybillCode())
                .setText(R.id.tv_goods_number,item.getInventoryNumber());
        TextView tvLook = helper.getView(R.id.tv_look);
        RelativeLayout rvDelete = helper.getView(R.id.rl_delete);
        if (num ==1){
            rvDelete.setVisibility(View.VISIBLE);
        }else if (num == 2){
            rvDelete.setVisibility(View.GONE);
        }
        tvLook.setOnClickListener(v -> {
            listener.onLook(helper.getAdapterPosition());
        });
        rvDelete.setOnClickListener(v -> {
            listener.onDelete(helper.getAdapterPosition());
        });
    }

    public void setInventoryInfoListener(InventoryInfoListener listener){
        this.listener = listener;
    }

    public interface InventoryInfoListener {
        void onLook(int position);
        void onDelete(int position);
    }
}
