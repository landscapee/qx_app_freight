package qx.app.freight.qxappfreight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import qx.app.freight.qxappfreight.R;

/**
 * 装卸机推送弹窗
 */
public class ProgressDialog extends DialogFragment {
    private Context context;
    private View convertView;
    private OnDismissListener onDismissListener;

    TextView msgTv;
    ProgressBar pb;

    public void setData(Context context, OnDismissListener onDismissListener) {
        this.context = context;
        this.onDismissListener = onDismissListener;
    }

    private void initViews() {
        msgTv = convertView.findViewById(R.id.dialog_tv_msg);
        pb = convertView.findViewById(R.id.dialog_pb);
        msgTv.setOnClickListener(v -> {
            super.dismiss();
            onDismissListener.refreshUI(false);
        });
        pb.setOnClickListener(v -> {
            super.dismiss();
            onDismissListener.refreshUI(true);
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_progress_download);
        convertView = dialog.findViewById(R.id.content_view);
        // 外部点击不取消
        dialog.setCanceledOnTouchOutside(false);
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        // 紧贴底部
        lp.gravity = Gravity.CENTER;
        // 宽度持平
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.anim_bottom_bottom);
        initViews();
        return dialog;
    }

    public interface OnDismissListener {
        void refreshUI(boolean isLocal);
    }

    public void setProgress(int progress){
        pb.setProgress(progress);
        msgTv.setText("下载中：" + progress + "%");
    }
}
