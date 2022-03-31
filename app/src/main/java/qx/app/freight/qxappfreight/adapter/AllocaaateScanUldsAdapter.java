package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.bean.response.UnLoadListBillBean;
import qx.app.freight.qxappfreight.utils.StringUtil;

public class AllocaaateScanUldsAdapter extends BaseQuickAdapter<GetInfosByFlightIdBean, BaseViewHolder> {
    private  List getInfosByFlightIdBean;
    public AllocaaateScanUldsAdapter(List<GetInfosByFlightIdBean> data) {
        super(R.layout.item_allocaaate_scan_ulds,data);
        this.getInfosByFlightIdBean=data;
    }

    @Override
    protected void convert(BaseViewHolder helper, GetInfosByFlightIdBean item) {
        String mType, mCode, mIata;

        if (TextUtils.isEmpty(item.getUldType())) {
            mType = "-";
        } else {
            mType = item.getUldType();
        }
        if (TextUtils.isEmpty(item.getUldCode())) {
            mCode = "-";
        } else {
            mCode = item.getUldCode();
        }
        if (TextUtils.isEmpty(item.getIata())) {
            mIata = "-";
        } else {
            mIata = item.getIata();
        }
        if(getInfosByFlightIdBean.size()>1){
            helper.setText(R.id.tv_uld_label,  "ULD"+(helper.getAdapterPosition()+1) );
            helper.setText(R.id.tv_uld_self_label, "ULD"+(helper.getAdapterPosition()+1)+"自重");
        }
        helper.setText(R.id.tv_uld, mType + " " + mCode + " " + mIata);
        helper.setText(R.id.tv_uld_self, item.getUldWeight() + "kg");
     }
}
