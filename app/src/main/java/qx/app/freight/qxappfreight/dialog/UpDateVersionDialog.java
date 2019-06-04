package qx.app.freight.qxappfreight.dialog;

import android.content.Context;
import android.view.View;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.widget.CommonDialog;

/**
 * created by swd
 * 2019/6/4 17:22
 */
public class UpDateVersionDialog extends CommonDialog {
    public UpDateVersionDialog(Context context) {
        super(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.negativeTv:
                if (listener != null) {
                    listener.onClick(this, false);
                }
                break;
            case R.id.positiveTv:
                if (listener != null) {
                    listener.onClick(this, true);
                }
                break;
        }
    }
}
