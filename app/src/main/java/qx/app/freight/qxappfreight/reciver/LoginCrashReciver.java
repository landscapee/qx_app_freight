package qx.app.freight.qxappfreight.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import qx.app.freight.qxappfreight.utils.ToastUtil;

/**
 * 登录冲突广播接收器
 * Created by mm on 2016/10/31.
 */
public class LoginCrashReciver extends BroadcastReceiver {

    private Context mContext;

    public LoginCrashReciver(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ToastUtil.showToast("登录冲突广播接收器");
    }
}
