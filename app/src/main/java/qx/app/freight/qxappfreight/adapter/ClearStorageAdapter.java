package qx.app.freight.qxappfreight.adapter;

import android.graphics.Color;
import android.util.Log;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.InventoryQueryBean;
import qx.app.freight.qxappfreight.utils.TimeUtils;

public class ClearStorageAdapter extends BaseMultiItemQuickAdapter<InventoryQueryBean, BaseViewHolder> {
    public ClearStorageAdapter(List<InventoryQueryBean> mDatas) {
        super(mDatas);
        addItemType(2, R.layout.item_clearstorage_list);
        addItemType(3, R.layout.item_clearstorage_list3);
        addItemType(4, R.layout.item_clearstorage_list4);
    }


    @Override
    protected void convert(BaseViewHolder helper, InventoryQueryBean item) {
        //运单号
        helper.setText(R.id.tv_order, item.getInWaybill().getWaybillCode());
        //航线
        if (null != item.getFlight().getFlightCourseByAndroid()) {
            switch (helper.getItemViewType()) {
                case 2:
                    helper.setText(R.id.tv_flight_1, item.getFlight().getFlightCourseByAndroid().get(0))
                            .setText(R.id.tv_flight_2, item.getFlight().getFlightCourseByAndroid().get(1));
                    break;
                case 3:
                    helper.setText(R.id.tv_flight_1, item.getFlight().getFlightCourseByAndroid().get(0))
                            .setText(R.id.tv_flight_2, item.getFlight().getFlightCourseByAndroid().get(1))
                            .setText(R.id.tv_flight_3, item.getFlight().getFlightCourseByAndroid().get(2));
                    break;
                case 4:
                    helper.setText(R.id.tv_flight_1, item.getFlight().getFlightCourseByAndroid().get(0))
                            .setText(R.id.tv_flight_2, item.getFlight().getFlightCourseByAndroid().get(1))
                            .setText(R.id.tv_flight_3, item.getFlight().getFlightCourseByAndroid().get(2))
                            .setText(R.id.tv_flight_4, item.getFlight().getFlightCourseByAndroid().get(3));
                    break;
            }
        }
        //航班号
        helper.setText(R.id.tv_store_info, item.getFlight().getFlightNo());
        //实际时间
        helper.setText(R.id.tv_time, TimeUtils.date2Tasktime3(item.getFlight().getActualTime()));
        //天  TimeUtils.getDay(item.getFlight().getActualTime())
        helper.setText(R.id.tv_time1, "");
        //货邮类型   AWBA 代表货物,AWBM代表邮件
        if (item.getInWaybill().getMailType().equals("AWBA"))
            helper.setImageResource(R.id.iv_type_img, R.mipmap.goods);
        else
            helper.setImageResource(R.id.iv_type_img, R.mipmap.mail);
        //库区
        helper.setText(R.id.tv_cargo, item.getInWaybill().getWarehouseArea() == null ? "- -|" : item.getInWaybill().getWarehouseArea());
        //库位
        helper.setText(R.id.tv_storehouse, "12");
        //是否是冷藏  冷藏 0 否 1 是
        if (0 == item.getInWaybill().getRefrigerated())
            helper.setText(R.id.tv_step_name, "冷藏:否");
        else
            helper.setText(R.id.tv_step_name, "冷藏:是");
        //是否异常
        if (0 == item.getInWaybill().getUbnormalNum()) {
            helper.setText(R.id.tv_old_waybill_code, "无");
        } else {
            helper.setText(R.id.tv_old_waybill_code, "查看");
            helper.setTextColor(R.id.tv_old_waybill_code, Color.RED);
        }
        //运单数
        helper.setText(R.id.tv_waybill_nub, "运单：13");
        //分练
        helper.setText(R.id.tv_fl_nub, "分练：12");
        //已提
        helper.setText(R.id.tv_yt_nub, "已提：10");
        //实际
        EditText view = helper.getView(R.id.et_clear_nub);
        String nub = view.getText().toString();
        Log.e("实际数量", nub);
    }
}
