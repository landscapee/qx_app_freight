package qx.app.freight.qxappfreight.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.AddtionInvoicesBean;
import qx.app.freight.qxappfreight.bean.response.FlightCabinInfo;

public class CabinAdapter extends BaseQuickAdapter<FlightCabinInfo.AircraftNoRSBean.CargosBean, BaseViewHolder> {

    public CabinAdapter(List <FlightCabinInfo.AircraftNoRSBean.CargosBean> list) {
        super(R.layout.item_cabin, list);
    }

    @Override
    protected void convert(BaseViewHolder helper,FlightCabinInfo.AircraftNoRSBean.CargosBean item) {
        //名字
        helper.setText(R.id.tv_cabin, item.getPos());
        //文件
    }
}
