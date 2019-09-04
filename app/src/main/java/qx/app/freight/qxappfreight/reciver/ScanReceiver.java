package qx.app.freight.qxappfreight.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;

import qx.app.freight.qxappfreight.app.MyApplication;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.ScanLaserData;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.utils.Tools;

/**
 * 扫码数据接受器
 */
public class ScanReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Constants.ACTION.equals(intent.getAction())) {
            ScanDataBean scanLaserData = new ScanDataBean();

            Bundle mBundle = intent.getExtras();
            String str = mBundle.getString(Constants.BROAD_STRING);
            //当前view
            scanLaserData.setFunctionFlag(MyApplication.currentView);
            scanLaserData.setData(str);
            scanLaserData.setLaser(true);
            EventBus.getDefault().post(scanLaserData);
            Tools.startShortVibrator(context.getApplicationContext());
        }

    }
}
