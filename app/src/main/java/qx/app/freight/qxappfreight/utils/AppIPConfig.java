package qx.app.freight.qxappfreight.utils;


import qx.app.freight.qxappfreight.app.MyApplication;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.constant.HttpConstant;

/**
 * 服务器ip配置
 * Created by mm on 2016/12/20.
 */
public class AppIPConfig {

    public static String getWebApiHost() {
        int netType = SharedPreferencesUtil.getInt(MyApplication.getContext(), Constants.mobile_network, -1);
        switch (netType) {
            case 0:
                return "http://" + HttpConstant.CMCC + ":805/mas-main/";//移动
            case 1:
                return "http://" + HttpConstant.CT + ":805/mas-main/";//电信
            case 2:
                //return "http://192.168.0.25:8080/mas-main/";
                return "http://" + HttpConstant.TEST + ":8080/mas-main/";//测试
            case 3:
                return "http://" + HttpConstant.PRODUCTION + ":805/mas-main/";
            default:
                return "http://" + HttpConstant.CMCC + ":805/mas-main/";//默认
        }
    }

    public static String getSchduleApiHost() {
        int netType = SharedPreferencesUtil.getInt(MyApplication.getContext(), Constants.mobile_network, -1);
        switch (netType) {
            case 0:
                return "http://" + HttpConstant.CMCC + ":86/api/schedule/";//移动
            case 1:
                return "http://" + HttpConstant.CT + ":86/api/schedule/";//电信
            case 2:
                //return "http://192.168.0.101:8080/";
                return "http://" + HttpConstant.SCHE_TEST + "/api/schedule/";//测试
            case 3:
                return "http://" + HttpConstant.PRODUCTION + ":86/api/schedule/";
            default:
                return "http://" + HttpConstant.CMCC + ":86/api/schedule/";//默认
        }
    }

    public static String getSocketIOHost() {
        int netType = SharedPreferencesUtil.getInt(MyApplication.getContext(), Constants.mobile_network, -1);
        switch (netType) {
            case 0:
                return "http://" + HttpConstant.CMCC + ":86";//移动
            case 1:
                return "http://" + HttpConstant.CT + ":86";//电信
            case 2:
                return "http://" + HttpConstant.SCHE_TEST;//测试
            case 3:
                return "http://" + HttpConstant.PRODUCTION + ":86";
            default:
                return "http://" + HttpConstant.CMCC + ":86";//默认
        }
    }

    public static String getHostIP() {
        int netType = SharedPreferencesUtil.getInt(MyApplication.getContext(), Constants.mobile_network, -1);
        switch (netType) {
            case 0:
                return HttpConstant.CMCC;
            case 1:
                return HttpConstant.CT;
            case 2:
                return HttpConstant.TEST;
            case 3:
                return HttpConstant.PRODUCTION;
            default:
                return HttpConstant.CMCC;//默认
        }
    }


    /**
     * 获取IM连接 ip
     *
     * @return
     */
    public static String getIMIP() {
        int netType = SharedPreferencesUtil.getInt(MyApplication.getContext(), Constants.mobile_network, -1);
        switch (netType) {
            case 0:
                return HttpConstant.CMCC;
            case 1:
                return HttpConstant.CT;
            case 2://测试环境imip不同
                return "172.18.57.226";
            case 3://测试环境imip不同
                return HttpConstant.PRODUCTION;
            default:
                return HttpConstant.CMCC;//默认
        }
    }
}
