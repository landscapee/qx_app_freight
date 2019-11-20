package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.UnloadBillAdapter;
import qx.app.freight.qxappfreight.bean.response.UnLoadListBillBean;
import qx.app.freight.qxappfreight.utils.StringUtil;

/**
 * 卸机单数据dialog
 */
public class UnloadBillInfoDialog extends DialogFragment {
    private Context context;
    private List<UnLoadListBillBean.DataBean.ContentObjectBean> list;

    public void setData(List<UnLoadListBillBean.DataBean.ContentObjectBean> mList, Context context) {
        this.list = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(context, R.style.dialog2);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_unload_bill);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER; // 紧贴底部
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.anim_bottom_bottom);
        RecyclerView rvUnloadBill = dialog.findViewById(R.id.rv_unload_bill);
        TextView tvClose = dialog.findViewById(R.id.tv_close_dialog);
        TextView tvStatistics =  dialog.findViewById(R.id.tv_statistics);
        TextView tvStatistics1 =  dialog.findViewById(R.id.tv_statistics_1);

        double c =0;
        double m= 0;
        double b = 0;
        double o = 0;
        for (UnLoadListBillBean.DataBean.ContentObjectBean objectBean:list){
            if (StringUtil.isDouble(objectBean.getActWgt())){
                if ("C".equals(objectBean.getType()))//货物
                    c+=Double.valueOf(objectBean.getActWgt());
                else if ("M".equals(objectBean.getType()))//邮件
                    m+=Double.valueOf(objectBean.getActWgt());
                else if ("BY".equals(objectBean.getType()))//行李
                    b+=Double.valueOf(objectBean.getActWgt());
                else if ("T".equals(objectBean.getType()))
                    b+=Double.valueOf(objectBean.getActWgt());
                else if ("BY".equals(objectBean.getType()))
                    b+=Double.valueOf(objectBean.getActWgt());
                else//其他
                    o+=Double.valueOf(objectBean.getActWgt());
            }
        }
        tvStatistics.setText("货物:"+c+"kg 邮件:"+m+"kg" );
        tvStatistics1.setText("行李:"+b+"kg 其他:"+o+"kg");
        rvUnloadBill.setLayoutManager(new LinearLayoutManager(context));
        UnloadBillAdapter adapter=new UnloadBillAdapter(list);
        rvUnloadBill.setAdapter(adapter);
        tvClose.setOnClickListener(v -> dismiss());
        return dialog;
    }
}
