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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.ChooseStoreroomAdapter;
import qx.app.freight.qxappfreight.adapter.UnloadBillAdapter;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;
import qx.app.freight.qxappfreight.listener.ChooseDialogInterface;
import qx.app.freight.qxappfreight.model.TestBean;
import qx.app.freight.qxappfreight.utils.ToastUtil;

/**
 * 卸机单数据dialog
 */
public class UnloadBillInfoDialog extends DialogFragment {
    private Context context;
    private List<LoadingListBean.DataBean.ContentObjectBean> list;

    public void setData(List<LoadingListBean.DataBean.ContentObjectBean> mList, Context context) {
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
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.anim_bottom_bottom);
        RecyclerView rvUnloadBill = dialog.findViewById(R.id.rv_unload_bill);
        TextView tvClose = dialog.findViewById(R.id.tv_close_dialog);
        rvUnloadBill.setLayoutManager(new LinearLayoutManager(context));
        UnloadBillAdapter adapter=new UnloadBillAdapter(list);
        rvUnloadBill.setAdapter(adapter);
        tvClose.setOnClickListener(v -> dismiss());
        return dialog;
    }
}
