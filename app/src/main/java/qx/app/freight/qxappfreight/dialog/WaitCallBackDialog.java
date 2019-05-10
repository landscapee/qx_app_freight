package qx.app.freight.qxappfreight.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Objects;

import qx.app.freight.qxappfreight.R;

/**
 * 调仓拉货等待dialog
 */
public class WaitCallBackDialog extends Dialog {
    private Context context;

    public WaitCallBackDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getWindow()).setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        View view = getLayoutInflater().inflate(R.layout.dialog_wait_callback, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);
    }

    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            ((Activity) context).finish();
        }
        return super.dispatchKeyEvent(event);
    }
}
