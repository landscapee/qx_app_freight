package qx.app.freight.qxappfreight.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.utils.StringUtil;

/**
 * 货物详情adapter
 *
 */
public class SingleItemInfoAdapter extends BaseQuickAdapter<TransportDataBase, BaseViewHolder> {

    SingleItemInfoAdapter(List<TransportDataBase> mDatas) {
        super(R.layout.item_single_info, mDatas);
    }

    @Override
    protected void convert(BaseViewHolder helper, TransportDataBase item) {
        if (helper.getAdapterPosition() % 2 == 0) {
            helper.itemView.setBackgroundColor(Color.WHITE);
        } else {
            helper.itemView.setBackgroundColor(Color.parseColor("#e7f5fb"));
        }
        //序号
        helper.setText(R.id.tv_num, helper.getAdapterPosition()+1+"");

        //品名
        helper.setText(R.id.tv_goods_name, item.getCargoCn());
        //件数
        helper.setText(R.id.tv_goods_number, StringUtil.formatStringDeleteDot(item.getTotalNumber()));
        //重量
        helper.setText(R.id.tv_weight, StringUtil.formatStringDeleteDot(item.getTotalWeight()));
        //体积
        helper.setText(R.id.tv_volume, item.getTotalVolume()+"");
        //包装类型
        helper.setText(R.id.tv_package, item.getPackagingType());

    }
}
