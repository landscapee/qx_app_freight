package qx.app.freight.qxappfreight.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.ModifyInportInfoActivity;
import qx.app.freight.qxappfreight.bean.InPortTallyListEntity;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.widget.FlightInfoLayout;

public class InPortTallyListAdapter extends BaseQuickAdapter<InPortTallyListEntity, BaseViewHolder> {
    private OnModifyListener onModifyListener;

    public InPortTallyListAdapter(@Nullable List<InPortTallyListEntity> data) {
        super(R.layout.item_inport_tally, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InPortTallyListEntity item) {
        LinearLayout container = helper.getView(R.id.ll_flight_info_container);
        FlightInfoLayout layout = new FlightInfoLayout(mContext, item.getFlightInfoList());
        LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.removeAllViews();
        container.addView(layout, paramsMain);
        helper.setText(R.id.tv_way_bill, item.getWaybill());
        String docText = String.format(mContext.getString(R.string.format_doc_arrive_info), (item.isDocArrived()) ? "Y|" + item.getDocName() : "N");
        helper.setText(R.id.tv_doc_arrive_info, StringUtil.getAutoColorText(docText));
        helper.setText(R.id.tv_doc_number_info, String.format(mContext.getString(R.string.format_doc_number_info), item.getDocNumber(), (int) item.getDocWeight()));
        String customsText = String.format(mContext.getString(R.string.format_customs_arrive_info), (item.isCustomsCont()) ? "Y" : "N", (item.isTransport()) ? "Y" : "N");
        helper.setText(R.id.tv_customs_info, StringUtil.getAutoColorText(customsText));
        helper.setText(R.id.tv_customs_number_info, String.format(mContext.getString(R.string.format_customs_number_info), item.getManifestNumber(), (int) item.getManifestWeight()));
        String unpackageText = String.format(mContext.getString(R.string.format_unpackage_info), (item.isUnpackagePlate()) ? "Y" : "N", (item.isChill()) ? "Y" : "N");
        helper.setText(R.id.tv_unpackage_info, StringUtil.getAutoColorText(unpackageText));
        helper.setText(R.id.tv_unpackage_number_info, String.format(mContext.getString(R.string.format_unpackage_number_info), item.getTallyNumber(), (int) item.getTallyWeight()));
        helper.setText(R.id.tv_store_info, String.format(mContext.getString(R.string.format_inport_tally_store_info), item.getStoreName(), item.getStoreNumber()));
        if (!"无异常".equals(item.getExceptionSituation())) {
            String content = "<font color='red'>" + item.getExceptionSituation() + "</font>";
            helper.setText(R.id.tv_exception, Html.fromHtml(content));
        }
        TextView tvModify = helper.getView(R.id.tv_modify);
        tvModify.setOnClickListener(v -> {
            if (onModifyListener != null) {
                onModifyListener.onModify(helper.getAdapterPosition());
            }
            Intent intent = new Intent(mContext, ModifyInportInfoActivity.class);
            intent.putExtra("data", item);
            ((Activity) mContext).startActivityForResult(intent, 333);
        });
    }

    public interface OnModifyListener {
        void onModify(int pos);
    }

    public void setOnModifyListener(OnModifyListener onModifyListener) {
        this.onModifyListener = onModifyListener;
    }
}
