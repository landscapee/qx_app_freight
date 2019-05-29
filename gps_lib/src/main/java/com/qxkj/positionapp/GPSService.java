package com.qxkj.positionapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.InputType;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by zzq/guohao on 2019/5/29 11:43
 *
 * @title gps定位功能
 *
 */
public class GPSService extends Service {

    static int GPS_SECONDS = 5000;

    boolean GPS_RUN = true;

    Handler handler = new Handler();

    /**
     * 启动service
     * @param context
     */
    public static void startGPSService(Context context){
        Intent intent = new Intent(context, GPSService.class);
        context.startService(intent);
    }

    public static void startGPSService(Context context, int seconds){
        GPS_SECONDS = seconds;
        Intent intent = new Intent(context, GPSService.class);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("GPS", "==========================GPS Service create========================");

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!isGpsEnabled(locationManager)){
            Log.e("GPS", "ERROR: GPS导航未开启，请设置！");
            return;
        }
        handler.postDelayed(runnable, 5000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GPS_RUN = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            GetIdUtil.getSingleInstance().getLocationInfo(GPSService.this);
            if(GPS_RUN) {
                handler.postDelayed(this, GPS_SECONDS);
            }
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

    /**
     * 位置监听
     */
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //TODO: 位置改变回调

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //TODO： 状态改变回掉
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
