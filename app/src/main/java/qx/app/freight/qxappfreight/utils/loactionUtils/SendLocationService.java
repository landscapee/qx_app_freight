package qx.app.freight.qxappfreight.utils.loactionUtils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import qx.app.freight.qxappfreight.bean.LocationBean;
import qx.app.freight.qxappfreight.bean.PositionBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.utils.AppIPConfig;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.Tools;


/**
 * TODO: 发送位置服务
 */
public class SendLocationService extends Service {
    public static final String TAG = "SendLocationService";
    private static final int PORT = 12278;
    private static final int time = 1000;
    private static final int TimeOut = 3;
    private List <LocationBean> mLocationBeen = new ArrayList <>();
    private boolean isSend = true;
    private Context mContext;
    private Send mSend;
    // 保存文本文件
    private String path = "SendLocationService.txt";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (isSend) {
                mHandler.postDelayed(mRunnable, time);
            }
        }
    };

    /**
     * TODO: 发送当前的位置信息
     */
    private void sendLocation() {
        //获取gps信息(会过时)
        PositionBean gpsBean = Tools.getGPSPosition();
        // 获取基站信息（不会过时）
        BSLoactionUtil.BSLocationBean bean = Tools.getBSLoaction();
        // 基站没有信息，不提交任何东西
        if (bean == null) {
            mHandler.obtainMessage(0).sendToTarget();
            return;
        }
        final LocationBean locationBean = new LocationBean();
        // gps是有效的
        if (gpsBean != null && Tools.isValidForLocasition(gpsBean.getTime())) {
            locationBean.setLat(gpsBean.getLatitude());
            locationBean.setAlt(gpsBean.getAltitude());
            locationBean.setLon(gpsBean.getLongitude());
        }
        locationBean.setDeviceId(DeviceInfoUtil.getIMEI(mContext));
        locationBean.setTime(TimeUtils.getTimeT());

        locationBean.setLac(bean.getLac());
        locationBean.setCell(bean.getCid());
        locationBean.setRssi(bean.getRssi());
        locationBean.setLocaDir("0");
        locationBean.setLocaX("0");
        locationBean.setLocaY("0");
        locationBean.setUserId(UserInfoSingle.getInstance().getUserId());
        //        Tools.writeStr2Txt("\n数据: \n" + locationBean.toString(), path);
        Log.d(TAG, "数据: " + locationBean.toString());
        //发送位置信息
        mSend.SendToGisServer(locationBean.toString(), new Send.OnSendLisenter() {
            @Override
            public void onSendResult(boolean isSuc) {
                //                if (isSuc) {//发送成功，证明网络正常，发送历史位置信息
                //                    sendHistoryLocation();
                //                    Tools.writeStr2Txt("success\n", path);
                //                } else {//发送失败，加入在内存里面，在网络正常的时候，发送
                //                    mLocationBeen.add(locationBean);
                //                    Tools.writeStr2Txt("fail\n", path);
                //                }
                //通知handler 发送完成（不判断成功或者失败）
                mHandler.obtainMessage(0).sendToTarget();
            }
        });
    }

    /**
     * TODO: 发送历史的 位置信息
     */
    private void sendHistoryLocation() {
        //如果存在历史位置数据
        if (mLocationBeen.size() > 0 && mLocationBeen.get(0) != null) {
            Log.d(TAG, "sendHistoryLocation: 发送历史");
            mSend.SendToGisServer(mLocationBeen.get(0).toString(), new Send.OnSendLisenter() {
                @Override
                public void onSendResult(boolean isSuc) {
                    if (isSuc) {// 成功，移除发送的历史位置信息，并且继续发送历史位置数据
                        mLocationBeen.remove(0);
                        sendHistoryLocation();
                        Log.d(TAG, "历史--位置信息发送成功");
                    }
                }
            });

        }
    }

    //发送位置信息
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            sendLocation();
        }
    };

    /**
     * TODO： 启动服务
     *
     * @param context
     */
    public static void startServcie(Context context) {
        Intent intent = new Intent(context, SendLocationService.class);
        context.startService(intent);
    }

    /**
     * TODO:关闭服务
     *
     * @param context
     */
    public static void stopService(Context context) {
        Intent intent = new Intent(context, SendLocationService.class);
        context.stopService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mSend = new Send(AppIPConfig.getHostIP(), PORT, TimeOut);
//        mSend = new Send(HttpConstant.PUSH_IP, PORT, TimeOut);
        Log.d(TAG, "onCreate: IP-" + AppIPConfig.getHostIP() + ":" + PORT);
        isSend = true;
        mHandler.postDelayed(mRunnable, time);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isSend = false;
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
