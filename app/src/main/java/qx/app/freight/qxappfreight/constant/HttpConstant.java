package qx.app.freight.qxappfreight.constant;

import qx.app.freight.qxappfreight.BuildConfig;

/**
 * TODO : 网络请求地址
 */
public class HttpConstant {
    public static final String TEST = BuildConfig.TEST;//"http://173.100.1.75:9008";//开发环境

    public static final String HUOYUN = BuildConfig.HUOYUN;//"http://173.100.1.51:80";//航班环境

    public static final String QXAITEST = BuildConfig.QXAITEST;//"http://10.16.23.156:805/mas-main/";//一期智能 生产环境
    //websocket 地址
    public static final String WEBSOCKET = BuildConfig.WEBSOCKET;//"173.100.1.78:7004";
    public static final String WEBSOCKETURL = BuildConfig.WEBSOCKETURL;//推送地址
    /**
     * 显示图片的地址
     */
    public static final String IMAGEURL = BuildConfig.IMAGEURL;


    //==============================================================================================================================================================
    public static final String CMCC = BuildConfig.CMCC;// "10.16.23.156";//中国移动
    public static final String CT = BuildConfig.CT;//"222.209.86.130";//中国电信
    //    public static final String TEST = "172.18.57.223";//内网测试
    public static final String SCHE_TEST = BuildConfig.SCHE_TEST;//"172.18.57.232";//排班系统测试环境
    public static final String PRODUCTION = BuildConfig.PRODUCTION;// "172.18.57.240";//内网生产访问ip
    public static final String IM_WEB_HOST = BuildConfig.IM_WEB_HOST;// "172.18.57.226";//特情web服务器ip    172.18.57.226/172.18.57.240 测试/网闸
    //public static final String IM_WEB_HOST = "172.18.57.226";//特情web服务器ip    172.18.57.226/172.18.57.240 测试/网闸
    public static final int IM_WEB_PORT = Integer.valueOf(BuildConfig.IM_WEB_PORT);//特情web端口号  8080/88   测试/网闸
    //public static final int IM_ILINK_PORT = 88;//ilink服务器端口号
    public static final int IM_ILINK_PORT = Integer.valueOf(BuildConfig.IM_ILINK_PORT);//ilink服务器端口号
    public static final int IM_FILE_PORT = Integer.valueOf(BuildConfig.IM_FILE_PORT);//23000;//文件服务器端口
    public static final String IM_APPKEY = BuildConfig.IM_APPKEY;//"0be5cb1c-a650-b58451";//ilink_key
    public static final String IM_SECUREKEY = BuildConfig.IM_SECUREKEY;// "b1cb1dad27636403bf125b78d98f00b2";//ilink_securekey
    public static final String IM_NOTICE_URL = BuildConfig.IM_NOTICE_URL;//"notice/noticeInfoInterface";//特情通知公告接口

}
