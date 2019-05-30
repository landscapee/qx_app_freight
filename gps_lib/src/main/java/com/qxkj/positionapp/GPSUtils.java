package com.qxkj.positionapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.qxkj.positionapp.observer.LocationObservable;
import com.qxkj.positionapp.observer.MyLocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by guohao on 2019/5/29 22:13
 *
 * 获取定位有三种方式
 * 1，通过service的位置移动监听
 * 2，通过位置管理器的
 * 3，通过网络请求获取位置，但是必须要有Sim卡
 */

public class GPSUtils {

    /**
     * 单例
     */
    private static GPSUtils GpsInstance = new GPSUtils();

    /**
     * 观察者列表
     */
    static List<LocationObservable> obableList = new ArrayList<>();

    /**
     * 位置信息
     */
    private static LocationEntity locationEntity = new LocationEntity();

    /**
     * 位置管理器
     */
    LocationManager locationManager;

    /**
     * provider请求参数
     */
    Criteria criteria;


    private GPSUtils() {

    }

    //

    /**
     * 获取单例
     * @return GPSUtils
     */
    public static GPSUtils getInstance() {
        return GpsInstance;
    }

    /**
     * 获取位置实体
     * @return LocationEntity
     */
    public LocationEntity getLocationEntity() {
        return locationEntity;
    }


    /**
     * 主动获取位置信息
     *   官方api
     * @param context
     * @return LocationEntity
     */
    @SuppressLint("MissingPermission")
    public LocationEntity getLastKnownLocation(Context context) {
        if (locationManager == null) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        if(!isGpsEnabled(locationManager)){
            Log.e("GPS", "ERROR: GPS导航未开启，请设置！");
            return null;
        }
        String provide = locationManager.getBestProvider(getCriteria(), true);
        Location location = locationManager.getLastKnownLocation(provide);
        if(location == null){
            return null;
        }
        return new LocationEntity(location.getLongitude(), location.getLatitude());
    }

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
     * 获取Criteria
     *
     * @return
     */
    public Criteria getCriteria() {
        if (criteria == null) {
            criteria = new Criteria();
            //高精度
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            //包含高度信息
            criteria.setAltitudeRequired(true);
            //包含方位信息
            criteria.setBearingRequired(true);
            //包含速度信息
            criteria.setSpeedRequired(true);
            //允许付费
            criteria.setCostAllowed(true);
            //高耗电
            criteria.setPowerRequirement(Criteria.POWER_HIGH);
        }
        return criteria;
    }

    /**
     * 获取已有的位置信息
     *
     * @return
     */
    public LocationEntity getCurrentLocation() {
        return locationEntity;
    }

    /**
     * 更新locationEntity
     * @param lon
     * @param lat
     */
    public void updateLocationEntity(double lon, double lat) {
        locationEntity.setTime(System.currentTimeMillis());
        locationEntity.setLongitude(lon);
        locationEntity.setLatitude(lat);
    }


    /**
     * 注册监听
     *
     * @param observerable
     */
    public void register(LocationObservable observerable) {
        obableList.add(observerable);
    }

    /**
     * 取消注册监听
     *
     * @param observerable
     */
    public void unRegister(LocationObservable observerable) {
        obableList.remove(observerable);
    }

    /**
     * gps位置更新
     *
     * @param locationEntity
     */
    public void notifyLocationUpdate(LocationEntity locationEntity) {
        for (LocationObservable observerable : obableList) {
            observerable.receiveLocationUpdate(locationEntity);
        }
    }

    /**
     * 如果注册了，直接解绑
     */
    public void unRegisterIfAready(LocationObservable observable) {
        obableList.remove(observable);
    }

    /**
     * 是否注册
     *
     * @param observable
     * @return
     */
    public boolean isRegister(LocationObservable observable) {
        return obableList.contains(observable);
    }



}
