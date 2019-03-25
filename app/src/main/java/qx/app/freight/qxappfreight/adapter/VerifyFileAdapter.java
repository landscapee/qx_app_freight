package qx.app.freight.qxappfreight.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.AddtionInvoicesBean;

public class VerifyFileAdapter extends BaseQuickAdapter<AddtionInvoicesBean.AddtionInvoices, BaseViewHolder> {

    public VerifyFileAdapter(List<AddtionInvoicesBean.AddtionInvoices> list) {
        super(R.layout.item_verify_file, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddtionInvoicesBean.AddtionInvoices item) {
        //名字
        helper.setText(R.id.tv_name, item.getFileTypeName());
        //文件
        helper.setText(R.id.tv_accessory, item.getFileName());



    }
}
