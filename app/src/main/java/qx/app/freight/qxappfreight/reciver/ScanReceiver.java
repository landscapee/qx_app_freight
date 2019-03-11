package qx.app.freight.qxappfreight.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;

import qx.app.freight.qxappfreight.constant.Constants;

/**
 * 扫码数据接受器
 */
public class ScanReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Constants.ACTION.equals(intent.getAction())) {

            Bundle mBundle = intent.getExtras();
            String str = mBundle.getString(Constants.BROAD_STRING);
            EventBus.getDefault().post(str);
        }

    }
}
