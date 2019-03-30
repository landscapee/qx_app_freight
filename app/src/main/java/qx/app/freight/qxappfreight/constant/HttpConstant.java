package qx.app.freight.qxappfreight.constant;

/**
 * TODO : 网络请求地址
 */
public class HttpConstant {
    //张硕ip  http://myyx.nat123.cc:24010/
//    public static final String TEST = "http://myyx.nat123.cc:24010";
//    public static final String TEST = "http://192.168.1.17:9008";
//    public static final String TEST = "http://173.101.2.8:80";//测试环境
    public static final String TEST = "http://173.100.1.75:9008";//开发环境
//    public static final String TEST = "http://192.168.1.16:7001";//刘磊环境
    public static final String HUOYUN = "http://173.100.1.51:80";//航班环境

    public static final String QXAITEST = "http://10.16.23.156:805/mas-main/";//一期智能 生产环境
    //websocket 地址
    public static final String WEBSOCKETURL = "ws://173.100.1.75:9008/socketServer?";
//    public static final String WEBSOCKETURL = "ws://173.100.1.78:7004/socketServer?";
//    public static final String WEBSOCKETURL = "ws://173.101.2.8:7004/socketServer?";//测试环境

//    public static final String IMAGEURL = "http://173.100.1.74/";
    public static final String IMAGEURL = "http://173.101.2.26/";//测试环境

//    public static final String TEST = "http://173.101.2.8:80";
//    public static final String TEST = "http://192.168.1.17:9008";


    public static final String CMCC = "10.16.23.156";//中国移动
    public static final String CT = "222.209.86.130";//中国电信
//    public static final String TEST = "172.18.57.223";//内网测试
    public static final String SCHE_TEST = "172.18.57.232";//排班系统测试环境
    public static final String PRODUCTION = "172.18.57.240";//内网生产访问ip
    public static final String IM_WEB_HOST = "172.18.57.226";//特情web服务器ip    172.18.57.226/172.18.57.240 测试/网闸
    //public static final String IM_WEB_HOST = "172.18.57.226";//特情web服务器ip    172.18.57.226/172.18.57.240 测试/网闸
    public static final int IM_WEB_PORT = 88;//特情web端口号  8080/88   测试/网闸
    //public static final int IM_ILINK_PORT = 88;//ilink服务器端口号
    public static final int IM_ILINK_PORT = 1883;//ilink服务器端口号
    public static final int IM_FILE_PORT = 23000;//文件服务器端口
    public static final String IM_APPKEY = "0be5cb1c-a650-b58451";//ilink_key
    public static final String IM_SECUREKEY = "b1cb1dad27636403bf125b78d98f00b2";//ilink_securekey

    public static final String IM_NOTICE_URL = "notice/noticeInfoInterface";//特情通知公告接口

}
