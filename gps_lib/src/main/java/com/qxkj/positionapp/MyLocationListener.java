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
        Log.e("GPS", "LocationChanged");
        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setLongitude(location.getLongitude());
        locationEntity.setLatitude(location.getLatitude());
        GPSUtils.getInstance().notifyLocationUpdate(locationEntity);
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
