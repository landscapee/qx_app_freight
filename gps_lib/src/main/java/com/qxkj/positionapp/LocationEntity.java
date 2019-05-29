package com.qxkj.positionapp;

public class LocationEntity {

    public LocationEntity(){

    }

    public LocationEntity(double longitude, double latitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //经度
    private double longitude;
    //纬度
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
}
