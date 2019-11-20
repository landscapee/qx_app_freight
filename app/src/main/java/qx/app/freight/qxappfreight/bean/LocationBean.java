package qx.app.freight.qxappfreight.bean;

/**
 * TODO : 获取位置信息上传bean
 * Created by owen
 * on 2017-01-05.
 */

public class LocationBean {

    private String Time; //yyyy-MM-ddTHH:mm:ss.SSS
    private double Lat;  //纬度
    private double Lon; //经度
    private double Alt; //高度
    private String DeviceId; //设备id
    private String userId;
    //以下为没有获取到 经纬度 传入的 基站定位 信息
    private String Rssi;
    private String Lac;
    private String Cell;
    //以下值 传 “0”
    private String LocaX;
    private String LocaY;
    private String LocaDir;



    @Override
    public String toString() {
        return "@APPLOC,"
                + Time + ","
                + Lat + ","
                + Lon + ","
                + Alt + ","
                + DeviceId + ","
                + Rssi + ","
                + Lac + ","
                + Cell + ","
                + LocaX + ","
                + LocaY + ","
                + LocaDir + ","
                + userId + ","
                + ",,*4A\r\n";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getAlt() {
        return Alt;
    }

    public void setAlt(double alt) {
        Alt = alt;
    }

    public String getCell() {
        return Cell;
    }

    public void setCell(String cell) {
        Cell = cell;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public String getLac() {
        return Lac;
    }

    public void setLac(String lac) {
        Lac = lac;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public String getLocaDir() {
        return LocaDir;
    }

    public void setLocaDir(String locaDir) {
        LocaDir = locaDir;
    }

    public String getLocaX() {
        return LocaX;
    }

    public void setLocaX(String locaX) {
        LocaX = locaX;
    }

    public String getLocaY() {
        return LocaY;
    }

    public void setLocaY(String locaY) {
        LocaY = locaY;
    }

    public double getLon() {
        return Lon;
    }

    public void setLon(double lon) {
        Lon = lon;
    }

    public String getRssi() {
        return Rssi;
    }

    public void setRssi(String rssi) {
        Rssi = rssi;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
