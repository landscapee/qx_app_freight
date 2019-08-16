package qx.app.freight.qxappfreight.bean.request;

/**
 * Created by Lain on 2016/9/28.
 */

public class UserBean {

    private String userId;
    private String time;

    public UserBean(String userId) {
        this.userId = userId;
    }

    public UserBean(String userId, String time) {
        this.userId = userId;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
