package qx.app.freight.qxappfreight.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.FlightCabinInfo;
import qx.app.freight.qxappfreight.utils.StringUtil;

public class CargoCabinAdapter extends BaseQuickAdapter<FlightCabinInfo.AircraftNoRSBean.CargosBean, BaseViewHolder> {
    public CargoCabinAdapter(List<FlightCabinInfo.AircraftNoRSBean.CargosBean> data) {
        super(R.layout.item_cargo_cabin, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FlightCabinInfo.AircraftNoRSBean.CargosBean item) {
        helper.setText(R.id.tv_cabin_num,item.getPos()+"标准载量");
        String str =  "约"+item.getHldVol()+"m³  |  "+ item.getHldMaxWgt()+"KG";
        helper.setText(R.id.tv_content, str);
    }
}
