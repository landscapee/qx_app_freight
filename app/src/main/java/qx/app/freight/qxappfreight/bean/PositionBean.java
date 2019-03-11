package qx.app.freight.qxappfreight.bean;

/**
 * TODO :
 * Created by owen
 * on 2016-12-26.
 */
public class PositionBean {
    private String longitude;
    private String latitude;
    private String altitude;
    private long time;

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
