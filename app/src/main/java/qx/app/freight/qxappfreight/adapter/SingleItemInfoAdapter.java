package qx.app.freight.qxappfreight.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Arrays;
import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.DeclareItem;
import qx.app.freight.qxappfreight.bean.response.MainListBean;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;

/**
 * 货物详情adapter
 *
 * @param <T>
 */
public class SingleItemInfoAdapter<T extends DeclareItem> extends BaseQuickAdapter<T, BaseViewHolder> {

    SingleItemInfoAdapter(List<T> mDatas) {
        super(R.layout.item_single_info, mDatas);
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
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
        helper.setText(R.id.tv_goods_number, String.valueOf(item.getNumber()));
        //重量
        helper.setText(R.id.tv_weight, String.valueOf(item.getWeight()));
        //体积
        helper.setText(R.id.tv_volume, String.valueOf(item.getVolume()));
        //包装类型
        helper.setText(R.id.tv_package, Arrays.toString(item.getPackagingType()));

    }
}
