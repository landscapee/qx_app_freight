package qx.app.freight.qxappfreight.adapter;

import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.InPortTallyListModel;
import qx.app.freight.qxappfreight.utils.StringUtil;

public class InPortTallyListAdapter extends BaseQuickAdapter<InPortTallyListModel, BaseViewHolder> {
    public InPortTallyListAdapter(@Nullable List<InPortTallyListModel> data) {
        super(R.layout.item_inport_tally, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InPortTallyListModel item) {
        helper.setText(R.id.tv_flight_name,item.getFlightName()).setText(R.id.tv_start_place,item.getStartPlace()).setText(R.id.tv_middle_place,item.getMiddlePlace())
                .setText(R.id.tv_target_place,item.getEndPlace());
        String docText=String.format(mContext.getString(R.string.format_doc_arrive_info),(item.isDocArrived())?"Y|"+item.getDocName():"N");
        helper.setText(R.id.tv_doc_arrive_info, StringUtil.getAutoColorText(docText));
        helper.setText(R.id.tv_doc_number_info,String.format(mContext.getString(R.string.format_doc_number_info),item.getDocNumber(),item.getDocWeight()));
        String customsText=String.format(mContext.getString(R.string.format_customs_arrive_info),(item.isCustomsCont())?"Y":"N",(item.isTransport())?"Y":"N");
        helper.setText(R.id.tv_customs_info,StringUtil.getAutoColorText(customsText));
        helper.setText(R.id.tv_customs_number_info,String.format(mContext.getString(R.string.format_customs_number_info),item.getManifestNumber(),item.getManifestWeight()));
        String unpackageText=String.format(mContext.getString(R.string.format_unpackage_info),(item.isUnpackagePlate())?"Y":"N",(item.isChill())?"Y":"N");
        helper.setText(R.id.tv_unpackage_info,StringUtil.getAutoColorText(unpackageText));
        helper.setText(R.id.tv_unpackage_number_info,String.format(mContext.getString(R.string.format_unpackage_number_info),item.getTallyNumber(),item.getTallyWeight()));
        helper.setText(R.id.tv_store_info,String.format(mContext.getString(R.string.format_inport_tally_store_info),item.getStoreName(),item.getStoreNumber()));
        if (!"无异常".equals(item.getExceptionSituation())){
            String content = "<font color='red'>"+item.getExceptionSituation()+"</font>";
            helper.setText(R.id.tv_exception,Html.fromHtml(content));
        }
        TextView tvModify=helper.getView(R.id.tv_modify);
        tvModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
