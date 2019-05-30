package com.qxkj.positionapp;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by guohao on 2019/5/29 21:06
 *
 * @title 位置监听类
 */
public class MyLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        Log.e("GPS", "LocationChanged: 位置更新了");
        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setLongitude(location.getLongitude());
        /**
         * 被动更新locationEntity信息
         * 优先级最高，立刻更新
         */
        GPSUtils.getInstance().updateLocationEntity(location.getLongitude(), location.getLatitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

        Log.e("GPS", "StatusChanged");
    }

    @Override
    public void onProviderEnabled(String provider) {


        Log.e("GPS", "ProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String provider) {

        Log.e("GPS", "ProviderDisabled");
    }
}
