package qx.app.freight.qxappfreight.bean.request;

/**
 * TODO : 登陆请求类
 * Created by owen
 * on 2016-09-08.
 */
public class ReqLoginBean {

    private String userName;
    private String pwd;
    private String deviceId;
    private String deviceType;
    private String eptModel;
    private String systemName;
    private String systemCode;
    private String phoneNo;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getEptModel() {
        return eptModel;
    }

    public void setEptModel(String eptModel) {
        this.eptModel = eptModel;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
