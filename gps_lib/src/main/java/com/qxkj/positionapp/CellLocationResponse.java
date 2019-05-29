package com.qxkj.positionapp;

public class CellLocationResponse {

    /**
     * errcode : 0
     * lat : 30.57097244
     * lon : 103.96138
     * radius : 514
     * address : 四川省成都市双流县东升街道机场东二路;机场南三路与机场东二路路口东133米
     */

    private int errcode;
    private double lat;
    private double lon;
    private String radius;
    private String address;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
