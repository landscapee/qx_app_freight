package com.qxkj.positionapp;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by zzq/guohao on 2019/5/29 11:43
 *
 * @title gps定位功能
 */
public class GPSService extends Service {

    static int GPS_SECONDS = 10000;

    boolean GPS_RUN = true;

    Handler handler = new Handler();

    LocationManager locationManager;

    static final String TAG = "GPS";

    /**
     * 启动service
     *
     * @param context
     */
    public static void startGPSService(Context context) {
        Intent intent = new Intent(context, GPSService.class);
        context.startService(intent);
    }

    /**
     * 停止服务
     * @param context
     */
    public static void stopGPSServer(Context context){
        Intent intent = new Intent(context, GPSService.class);
        context.stopService(intent);
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();

        Log.e(TAG, "==========================GPSService Start========================");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!isGpsEnabled(locationManager)) {
            Log.e(TAG, "ERROR: GPS导航未开启，请设置！");
            return;
        }

        String provider = locationManager.getBestProvider(GPSUtils.getInstance().getCriteria(), true);

        //开启位置监听
        locationManager.requestLocationUpdates(provider, 100, 1, new MyLocationListener());

        //开启
        handler.post(runnable);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "==========================GPSService Stop========================");
        GPS_RUN = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //间隔10秒更新数据
            if (System.currentTimeMillis() - GPSUtils.getInstance().getCurrentLocation().getTime() > 10000) {
                //直接获取
                LocationEntity locationEntity = GPSUtils.getInstance().getLastKnownLocation(GPSService.this);
                if (locationEntity != null) {
                    Log.e(TAG, "直接获取定位成功！");
                    GPSUtils.getInstance().updateLocationEntity(locationEntity.getLongitude(), locationEntity.getLatitude());
                } else {
                    //网络获取
                    if (GpsNetworkUtils.getSingleInstance().isNetworkGpsEnable(GPSService.this)) {
                        GpsNetworkUtils.getSingleInstance().getLocationBySIM(GPSService.this);
                    } else {
                        Log.e(TAG, "Final Error: 获取不到定位信息，等GPS自己更新吧");
                    }
                }
            }
            handler.postDelayed(this, 2000);
        }
    };

    /**
     * 检查gps是否开启
     *
     * @param locationManager
     * @return
     */
    public static boolean isGpsEnabled(LocationManager locationManager) {
        boolean isOpenGPS = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isOpenNetwork = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return isOpenGPS || isOpenNetwork;
    }

}
