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
import android.widget.Button;

import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.ChooseStoreroomAdapter;
import qx.app.freight.qxappfreight.adapter.ShowUldInfoAdapter;
import qx.app.freight.qxappfreight.bean.response.UldInfo;
import qx.app.freight.qxappfreight.listener.ChooseDialogInterface;
import qx.app.freight.qxappfreight.utils.ToastUtil;

/**
 * 选择异常类型dialog
 */
public class ShowUldInfoDialog extends DialogFragment {
    private RecyclerView mRecyclerView;
    private Button btnConfirm;

    private ShowUldInfoAdapter adapter;
    private List <UldInfo> mList;
    private Context context;

    public void setData(List <UldInfo> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(context, R.style.dialog2);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_show_uld_info);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.anim_bottom_bottom);
        mRecyclerView = dialog.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ShowUldInfoAdapter(mList);
        mRecyclerView.setAdapter(adapter);
        btnConfirm = dialog.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(v -> {
                dismiss();
        });
        return dialog;
    }


}
