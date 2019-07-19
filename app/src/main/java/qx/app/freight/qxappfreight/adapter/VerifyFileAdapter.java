package qx.app.freight.qxappfreight.adapter;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.AddtionInvoicesBean;
import qx.app.freight.qxappfreight.utils.ToastUtil;

public class VerifyFileAdapter extends BaseQuickAdapter<AddtionInvoicesBean.AddtionInvoices, BaseViewHolder> {

    public VerifyFileAdapter(List<AddtionInvoicesBean.AddtionInvoices> list) {
        super(R.layout.item_verify_file, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddtionInvoicesBean.AddtionInvoices item) {
        //名字
        helper.setText(R.id.tv_name, item.getFileTypeName());
        //文件
        if (TextUtils.isEmpty(item.getFilePath())){
            helper.getView(R.id.iv_verify).setVisibility(View.GONE);
            helper.getView(R.id.tv_accessory).setVisibility(View.GONE);
        }else{
            helper.getView(R.id.iv_verify).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_accessory, "电子文件预览");
        }
    }
}
