package qx.app.freight.qxappfreight.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 保障组变更广播接收器
 * Created by mm on 2016/10/31.
 */
public class ProtectReciver extends BroadcastReceiver {

    private Context mContext;

    public ProtectReciver(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        try {
//            if (Tools.getDeptcode().contains(Constant.DEPT_DENGJIQIAO)) {//TODO 登机桥部门屏蔽保障组通知广播
//                return;
//            }
//            T.ShowToast(context, "保障组变更");
//        } catch (NullPointerException np) {
//            T.ShowToast(context, "保障组变更");
//        }
    }
}
