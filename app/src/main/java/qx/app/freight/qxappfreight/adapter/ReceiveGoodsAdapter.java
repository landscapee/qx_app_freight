package qx.app.freight.qxappfreight.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;

public class ReceiveGoodsAdapter extends BaseQuickAdapter<MyAgentListBean, BaseViewHolder> {
    private OnDeleteClick mOnDeleteClick;

    public ReceiveGoodsAdapter(List<MyAgentListBean> data) {
        super(R.layout.item_receive_good, data);
    }

    @SuppressLint("StringFormatMatches")
    @Override
    protected void convert(BaseViewHolder helper, MyAgentListBean item) {
        //板车号
        helper.setText(R.id.tv_receive_nb,item.getScooterCode());
        //板车自重
        helper.setText(R.id.tv_receive_weight,item.getScooterWeight()+"kg");
        //品名~件数~重量~体积
        helper.setText(R.id.tv_receive_info,String.format(mContext.getString(R.string.format_return_good_info)
                ,item.getCargoCn()
                ,item.getNumber()
                ,item.getWeight()
                ,item.getVolume()));
        //ULd编号~Uld体积
        helper.setText(R.id.tv_receive_uld,String.format(mContext.getString(R.string.format_receive_uld)
                ,item.getUldCode()
                ,item.getUldWeight()));
        //超重重量
        helper.setText(R.id.tv_receive_sw,"超重"+item.getOverWeight()+"kg");
        //库区
        helper.setText(R.id.tv_receice_repplace,item.getReservoirName());
       //删除按钮
//        helper.getView(R.id.btn_delete).setOnClickListener(v -> Toast.makeText(mContext, "当前删除：" + helper.getAdapterPosition(), Toast.LENGTH_SHORT).show());

        helper.getView(R.id.btn_delete).setOnClickListener(v -> mOnDeleteClick.onDeleteClick(v, helper.getAdapterPosition()));
    }

    public void setOnDeleteClickListener(OnDeleteClick mOnDeleteClick) {
        this.mOnDeleteClick = mOnDeleteClick;
    }

    public interface OnDeleteClick {
        void onDeleteClick(View view, int position);
    }
}
