package qx.app.freight.qxappfreight.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.widget.CollapsableLinearLayout;

/**
 * 货品列表页adapter
 */
public class MainListRvAdapter extends BaseQuickAdapter<TransportDataBase, BaseViewHolder> {
    public MainListRvAdapter(List<TransportDataBase> mDatas) {
        super(R.layout.item_main_list, mDatas);
    }

    @SuppressLint("StringFormatMatches")
    @Override
    protected void convert(BaseViewHolder helper, TransportDataBase item) {

        //运单号
        helper.setText(R.id.tv_order, item.getWaybillCode());
        //预交道口-预交时段
//        String roadStr = item.getExpectedDeliveryGate()+" | "+TimeUtils.date2Tasktime3(item.getExpectedDeliveryTime())+"("+TimeUtils.getDay(item.getExpectedDeliveryTime())+")";
//        helper.setText(R.id.tv_road_info, roadStr);
        //总件数-总体积-总重量
        helper.setText(R.id.tv_number_info, String.format(mContext.getString(R.string.format_number_info), StringUtil.formatStringDeleteDot(item.getTotalNumber()), item.getTotalVolume()+"", StringUtil.formatStringDeleteDot(item.getTotalWeight())));

        TextView tvStatusName = helper.getView(R.id.tv_step_name);
        ImageView ivFlag = helper.getView(R.id.iv_flag);
        TextView tvOldWayBillCode = helper.getView(R.id.tv_old_waybill_code);

        tvOldWayBillCode.setVisibility(View.GONE);
        helper.getView(R.id.ll_collection).setVisibility(View.VISIBLE);
        ivFlag.setVisibility(View.GONE);
        switch (item.getTaskTypeCode()) {
//            case "changeApply":
//                tvStatusName.setTextColor(mContext.getResources().getColor(R.color.black_3));
//                tvStatusName.setText("换单审核");
//                ivFlag.setVisibility(View.VISIBLE);
//                ivFlag.setImageResource(R.mipmap.collect_switch);
//                tvOldWayBillCode.setVisibility(View.VISIBLE);
//                tvOldWayBillCode.setText(item.getExchangeWaybillBefore());
//                break;
            case "collection":
                tvStatusName.setTextColor(mContext.getResources().getColor(R.color.blue_2e8));
                break;
            case "reCollection":
                tvStatusName.setTextColor(mContext.getResources().getColor(R.color.blue_2e8));
                break;
            case "RR_collectReturn":
                ivFlag.setVisibility(View.VISIBLE);
                ivFlag.setImageResource(R.mipmap.collect_wait);
                tvStatusName.setTextColor(mContext.getResources().getColor(R.color.orange_D67));
                break;
            case "receive":
                tvStatusName.setTextColor(mContext.getResources().getColor(R.color.black_3));
                break;
            case "reReceive":
                tvStatusName.setTextColor(mContext.getResources().getColor(R.color.black_3));
                break;
            case "changeCollection":
                tvStatusName.setTextColor(mContext.getResources().getColor(R.color.red));
                break;
            case "borrowReceive":
                tvStatusName.setTextColor(mContext.getResources().getColor(R.color.black_3));
                break;
            case "borrowCollection":
                tvStatusName.setTextColor(mContext.getResources().getColor(R.color.black_3));
                break;

        }
        tvStatusName.setText(item.getTaskType());


        String coldStr = "";
        if (!TextUtils.isEmpty(item.getColdStorage())) {
            switch (item.getColdStorage()) {
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
                    coldStr = "冷藏 | " + item.getRefrigeratedTemperature() + "℃  ";
                    break;
            }
        }
        if (item.getSpecialCargoCode() != null && !"".equals(item.getSpecialCargoCode())) {
            coldStr = coldStr + "特货" + item.getSpecialCargoCode();
        }
        helper.setText(R.id.tv_store_info, coldStr);

//        helper.setText(R.id.tv_store_info, String.format(mContext.getString(R.string.format_store_info),item.getColdStorage().equals("0") ? "不冷藏" : "冷藏", 10, 321));
        //计费重量
        String weight = "计费重量:  <font color='#FF0000'>" + item.getBillingWeight() + "kg</font>";
        helper.setText(R.id.tv_weight_judge, Html.fromHtml(weight));
        helper.getView(R.id.tv_weight_judge).setVisibility(View.GONE);
        //航班号
        helper.setText(R.id.tv_flight_number, item.getFlightNo());
        //航班预计起飞时间
        helper.setText(R.id.tv_arrive_time, String.format(mContext.getString(R.string.format_arrive_info), TimeUtils.date2Tasktime3(item.getFlightDate()), TimeUtils.getDay(item.getFlightDate())));
        //航空公司-代理公司
        helper.setText(R.id.tv_company_info, String.format(mContext.getString(R.string.format_company_info), item.getFlightName(), item.getFreightName()));
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mContext);
        RecyclerView rvDetail = helper.getView(R.id.rv_detail_list);
        rvDetail.setLayoutManager(manager);
        List<TransportDataBase> itemOneList = new ArrayList<>();
        itemOneList.add(item);
        SingleItemInfoAdapter adapter = new SingleItemInfoAdapter(itemOneList);
        rvDetail.setAdapter(adapter);

        CollapsableLinearLayout collView = helper.getView(R.id.coll_listview);
        ImageView view = helper.getView(R.id.iv_control_show);
        RelativeLayout rlExpand = helper.getView(R.id.rl_expand);
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
        rlExpand.setOnClickListener(v -> {
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
