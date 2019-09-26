package qx.app.freight.qxappfreight.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.SmInventoryEntryandexit;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.utils.MapValue;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;

public class IOManifestAdapter extends BaseQuickAdapter<SmInventoryEntryandexit, BaseViewHolder> {

    private OnDoitClickListener mOnDoitClickListener;

    public IOManifestAdapter(List<SmInventoryEntryandexit> data) {
        super(R.layout.item_io_manifest, data);
    }

    public void setOnDeleteClickListener(OnDoitClickListener listener) {
        this.mOnDoitClickListener = listener;
    }

    @Override
    protected void convert(BaseViewHolder helper, SmInventoryEntryandexit item) {

        helper.setText(R.id.tv_no,(helper.getAdapterPosition()+1)+"");
        helper.setText(R.id.tv_waybill_number,item.getWaybillCode());
        helper.setText(R.id.tv_num,item.getNumber()+"");
        helper.setText(R.id.tv_weight,item.getWeight()+"");

        View viewDoit = helper.getView(R.id.tv_do_it);
        viewDoit.setTag(helper.getAdapterPosition());
        if (!viewDoit.hasOnClickListeners()) {
            viewDoit.setOnClickListener(v -> {
                if (mOnDoitClickListener != null) {
                    mOnDoitClickListener.onDoitClick(v, (Integer) v.getTag());
                }
            });
        }
    }


    public interface OnDoitClickListener {
        void onDoitClick(View view, int position);
    }
}
