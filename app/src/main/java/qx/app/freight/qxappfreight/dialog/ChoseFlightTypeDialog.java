package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import qx.app.freight.qxappfreight.R;

/**
 * 装卸机推送弹窗
 */
public class ChoseFlightTypeDialog extends DialogFragment{
    private Context context;
    private View convertView;
    private OnDismissListener onDismissListener;

    public void setData(Context context, OnDismissListener onDismissListener) {
        this.context = context;
        this.onDismissListener = onDismissListener;
    }

    private void initViews() {
        TextView tvInter = convertView.findViewById(R.id.tv_inter_type);
        TextView tvInland = convertView.findViewById(R.id.tv_inland_type);
        tvInter.setOnClickListener(v->{
            super.dismiss();
            onDismissListener.refreshUI(false);
        });
        tvInland.setOnClickListener(v->{
            super.dismiss();
            onDismissListener.refreshUI(true);
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(context, R.style.dialog2);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_chose_flight_type);
        convertView = dialog.findViewById(R.id.content_view);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.anim_bottom_bottom);
        initViews();
        return dialog;
    }

    public interface OnDismissListener {
        void refreshUI(boolean isLocal);
    }
}
