package qx.app.freight.qxappfreight.bean.request;

/**
 * Created by Lain on 2016/9/28.
 */

public class ReqLoginOutBean {
    private String token;
    private String userId;
    public ReqLoginOutBean (String token, String userId){
        this.token = token;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
