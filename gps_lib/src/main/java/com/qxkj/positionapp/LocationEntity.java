package com.qxkj.positionapp;

/**
 *
 * Created by guohao on 2019/5/30 14:51
 *
 * 坐标实体
 */
public class LocationEntity {

    public LocationEntity(){

    }

    public LocationEntity(double longitude, double latitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * 上一次更新坐标的时间
     */
    private long time = 0;

    /**
     * 经度
     */
    private double longitude;

    /**
     * 纬度
     */
    private double latitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
