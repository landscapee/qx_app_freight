package qx.app.freight.qxappfreight.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import java.util.Objects;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.bean.response.UpdateVersionBean2;

/*
 * 版本更新dialog
 */
public class AppUpdateDailog extends Dialog implements
        View.OnClickListener {
    private TextView vtime;
    private TextView vContent;
    private AppUpdateLinstener appUpdateLinstener;
    private Context context;

    public AppUpdateDailog(Context context) {
        super(context, R.style.AppUpdateDialog);
        this.context = context;
        initView();
    }

    private void initView() {
        this.setContentView(R.layout.app_update_layout);
        this.setCancelable(false);
        Window window = this.getWindow();
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        WindowManager.LayoutParams lp = Objects.requireNonNull(window).getAttributes();
        lp.height = LayoutParams.WRAP_CONTENT;
        int screenW = metrics.widthPixels;
        lp.width = (int) (0.8 * screenW);
        window.setGravity(Gravity.CENTER);
        vtime = this.findViewById(R.id.tv_app_new_version_time);
        vContent = this.findViewById(R.id.tv_app_new_version_desc);
        findViewById(R.id.app_cancel_btn).setOnClickListener(this);
        findViewById(R.id.app_confirm_btn).setOnClickListener(this);
    }

    public interface AppUpdateLinstener {
        void cancel();

        void sure();
    }

    public void setAppUpdateDialogData(UpdateVersionBean2 appVersion, AppUpdateLinstener appUpdateLinstener) {
        try {
            vtime.setText(appVersion.getData().getVersionCode());
            vContent.setText(appVersion.getData().getUpdateMsg());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.appUpdateLinstener = appUpdateLinstener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_cancel_btn:
                appUpdateLinstener.cancel();
                dismiss();
                break;
            case R.id.app_confirm_btn:
                appUpdateLinstener.sure();
                break;
        }
    }
}
