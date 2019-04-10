package qx.app.freight.qxappfreight.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.FtGroupScooter;
import qx.app.freight.qxappfreight.utils.StringUtil;

public class CargoHandlingWaybillAdapter extends BaseQuickAdapter<FtGroupScooter, BaseViewHolder> {

    private OnOneClickLister mOnOneClickLister;

    private OnTwoClickLister mOnTwoClickLister;


    public CargoHandlingWaybillAdapter(List<FtGroupScooter> data) {
        super(R.layout.item_cargo_handling_waybill, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FtGroupScooter item) {
        View viewSubPackage = helper.getView(R.id.rl_subpackage);
        viewSubPackage.setTag(helper.getAdapterPosition());
        if (!viewSubPackage.hasOnClickListeners()) {
            viewSubPackage.setOnClickListener(v -> {
                if (mOnOneClickLister != null) {
                    mOnOneClickLister.onSubpackageClick(v, (Integer) v.getTag());
                }
            });
        }
        View viewAll = helper.getView(R.id.rl_all);
        viewAll.setTag(helper.getAdapterPosition());
        if (!viewAll.hasOnClickListeners()) {
            viewAll.setOnClickListener(v -> {
                if (mOnTwoClickLister != null) {
                    mOnTwoClickLister.mAllClickListener(v, (Integer) v.getTag());
                }
            });
        }
        helper.setText(R.id.tv_waybill_number,item.getWaybillCode());
        helper.setText(R.id.tv_waybill_count,item.getNumber()+"");
        helper.setText(R.id.tv_weight,item.getWeight()+"");
        helper.setText(R.id.tv_volume,item.getVolume()+"");
//        helper.setText(R.id.tv_destination,item.get+"");//目的站
        if (!StringUtil.isEmpty(item.getRepName()))
            helper.setText(R.id.tv_warehouse_area,item.getRepName());
        else
            helper.setText(R.id.tv_warehouse_area,"/");

        helper.setText(R.id.tv_subpackage,"分装");
        helper.setText(R.id.tv_all,"全装");

        if (item.getGroupScooterStatus() == 0)
            helper.setText(R.id.tv_tp_type,"正常");
        else
            helper.setText(R.id.tv_tp_type,"拉货");


    }
    public void setOnSubpackageClickListener(OnOneClickLister listener) {
        this.mOnOneClickLister = listener;
    }
    public interface OnOneClickLister {
        void onSubpackageClick(View view, int position);
    }

    public void setOnAllClickListener(OnTwoClickLister listener) {
        this.mOnTwoClickLister = listener;
    }

    public interface OnTwoClickLister {
        void mAllClickListener(View view, int position);
    }

}
