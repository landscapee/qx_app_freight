package qx.app.freight.qxappfreight.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.ReceiveGoodsActivity;
import qx.app.freight.qxappfreight.bean.response.MainListBean;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.widget.CollapsableLinearLayout;

import static qx.app.freight.qxappfreight.app.MyApplication.getContext;

/**
 * 货品列表页adapter
 *
 * @param <T> 泛型传参
 */
public class MainListRvAdapter<T extends TransportListBean> extends BaseQuickAdapter<T, BaseViewHolder> {
    public MainListRvAdapter(List<T> mDatas) {
        super(R.layout.item_main_list, mDatas);
    }

    @SuppressLint("StringFormatMatches")
    @Override
    protected void convert(BaseViewHolder helper, T item) {
        if (item instanceof TransportListBean) {
            helper.getView(R.id.tv_step_name).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_step_name, "");
        } else {
            helper.getView(R.id.tv_step_name).setVisibility(View.GONE);
        }
        //运单号
        helper.setText(R.id.tv_order, item.getWaybillCode());
        //预交道口-预交时段
        String roadStr = item.getExpectedDeliveryGate()+" | "+TimeUtils.date2Tasktime3(item.getExpectedDeliveryTime())+"("+TimeUtils.getDay(item.getExpectedDeliveryTime())+")";
        helper.setText(R.id.tv_road_info, roadStr);
        //总件数-总体积-总重量
        helper.setText(R.id.tv_number_info, String.format(mContext.getString(R.string.format_number_info), item.getTotalNumberPackages(), item.getTotalVolume(), item.getTotalWeight()));
       String coldStr = "";
       switch (item.getColdStorage()){
           case "0":
               coldStr = "普通  ";
               break;
           case "1":
               coldStr = "贵重  ";
               break;
           case "2":
               coldStr = "危险  ";
               break;
           case "3":
               coldStr = "活体  ";
               break;
           case "4":
               coldStr = "冷藏 | "+ item.getRefrigeratedTemperature()+"℃  ";
               break;
       }
       if (item.getSpecialCargoCode() != null && !"".equals(item.getSpecialCargoCode())){
           coldStr = coldStr + "特货"+item.getSpecialCargoCode();
       }
        helper.setText(R.id.tv_store_info, coldStr);

//        helper.setText(R.id.tv_store_info, String.format(mContext.getString(R.string.format_store_info),item.getColdStorage().equals("0") ? "不冷藏" : "冷藏", 10, 321));
        //计费重量
        String weight = "计费重量:  <font color='#FF0000'>" + item.getBillingWeight() + "kg</font>";
        helper.setText(R.id.tv_weight_judge, Html.fromHtml(weight));
        //航班号
        helper.setText(R.id.tv_flight_number, item.getFlightNumber());
        //航班预计起飞时间
        helper.setText(R.id.tv_arrive_time, String.format(mContext.getString(R.string.format_arrive_info),TimeUtils.date2Tasktime3(item.getEtd()) , TimeUtils.getDay(item.getEtd())));
        //航空公司-代理公司
        helper.setText(R.id.tv_company_info, String.format(mContext.getString(R.string.format_company_info), "四川航空", "中外货运代理"));
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mContext);
        RecyclerView rvDetail = helper.getView(R.id.rv_detail_list);
        rvDetail.setLayoutManager(manager);
        SingleItemInfoAdapter adapter = new SingleItemInfoAdapter(item.getDeclareItem());
        rvDetail.setAdapter(adapter);

        CollapsableLinearLayout collView = helper.getView(R.id.coll_listview);
        ImageView view = helper.getView(R.id.iv_control_show);
        View llMore = helper.getView(R.id.ll_more);
        llMore.setTag(helper.getAdapterPosition());

        if (item.isExpand()) {
            llMore.setVisibility(View.VISIBLE);
            view.setImageResource(R.mipmap.unexpand);
            collView.expand();

        } else {
            llMore.setVisibility(View.GONE);
            view.setImageResource(R.mipmap.expand);
            collView.collapse();

        }
        view.setOnClickListener(v -> {
            if (item.isExpand()) {
                llMore.setVisibility(View.GONE);
                view.setImageResource(R.mipmap.expand);
                collView.collapse();
                item.setExpand(false);

            } else {
                llMore.setVisibility(View.VISIBLE);
                view.setImageResource(R.mipmap.unexpand);
                collView.expand();
                item.setExpand(true);

            }
        });
    }

}
