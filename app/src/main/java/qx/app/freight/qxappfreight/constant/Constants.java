package qx.app.freight.qxappfreight.constant;


import android.os.Environment;

import java.io.File;

public class Constants {
    //更新版本用的参数常量
    public static final String APP_NAME = "智慧货运_";
    //====================================================角色分类======================================================
    /**
     * 安检
     */
    public static String SECURITY_CHECK = "securityCheck";
    /**
     * 收运
     */
    public static String COLLECTION = "collection";
    /**
     * 货运
     */
    public static String INVENTORY_KEEPER = "inventoryKeeper";
    /**
     * 货代
     */
    public static String CARGO_AGENCY = "cargoAgency";
    /**
     * 收验
     */
    public static String RECEIVE = "receive";
    /**
     * 预配-组板
     */
    public static String PREPLANER = "preplaner";
    /**
     * 理货
     */
    public static String BEFOREHAND = "beforehand";
    /**
     * 理货
     */
    public static String INSTALLSCOOTER = "installScooter";
    /**
     * 复重员
     */
    public static String WEIGHTER = "weighter";
    /**
     * 内场司机
     */
    public static String DRIVERIN = "infieldDriver";

    /**
     * 外场司机
     */
    public static String DRIVEROUT = "offSiteEscort";
    /**
     * 装卸机
     */
    public static String INSTALL_UNLOAD_EQUIP = "supervision";
    /**
     * 装卸员小组长
     */
    public static String INSTALL_EQUIP_LEADER = "stevedores";

    /**
     * 结载
     */
    public static String JUNCTION_LOAD = "clipping";

    /**
     * 国际货物
     */
    public static String INTERNATIONAL_GOODS = "international_goods";

    /**
     * 配载
     */
    public static String STOWAGE = "stowage";

    /**
     * 进港提货
     */
    public static String INPORTDELIVERY = "delivery_in";
    /**
     * 进港分拣
     */
    public static String INPORTTALLY = "beforehand_in";

    /**
     * 行李员
     */
    public static String PORTER = "porter";


    //收验type值
    public static final int TYPE_MAIN_LIST_CHECK = 1;
    //收运type值
    public static final int TYPE_MAIN_LIST_TRANSPORT = 2;
    //入库type值
    public static final int TYPE_MAIN_LIST_STORE = 3;


    // 获取当前位置时间与当前系统时间间隔有效分钟数
    public static final int ValidTime = 2;
    // 位置信息
    public static final String POSITION = "position";

    //====================================================广播接收======================================================
    public static final String ACTION = "android.intent.ACTION_DECODE_DATA";

    public static final String BROAD_STRING = "barcode_string";
    //====================================================图片保存地址======================================================
    public static final String CAMERA_PATH = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + "Camera" + File.separator;

    //====================================================存储用户信息静态======================================================
    public static final String token = "token";
    public static final String role = "role";
    public static final String userId = "userId";
    public static final String realName = "realName";
    public static final String timeDelta = "timeDelta";
    public static final String mobile_network = "mobile_network";
    public static final String deptcode = "deptcode";//部门编码


    public static final String KEY_LOGIN_NAME = "key.login.name";//登录账号
    public static final String KEY_LOGIN_PWD = "key.login.pwd";//登录密码


    //====================================================关闭页面的code======================================================
    public static final int FINISH_HANDCAR_DETAILS = 100;
    public static final int FINISH_SUBPACKAGE = 101;
    public static final int FINISH_REFRESH = 200;
    //scan
    public static final int SCAN_RESULT = 88;
    public static final String SACN_DATA = "scan_data";

    //===================================================分页数据获取条数======================================================
    public static final int PAGE_SIZE = 100;//列表分页获取的单页最大的数量


    //im加的字段常量
    public final static int NOTIFY_ID_PUSH_QUIET = 0x07;//静音通知id
    public final static int NOTIFY_ID_CHAT = 0x04;//聊天通知id
    public static final String PUSH_TYPE_SPECAIL_NOTICE = "special_notice_to_user";//被@的消息、
    public final static String IMLIB_BROADCAST_CHAT_NEWMESSAGE = "IMLIB_BROADCAST_CHAT_NEWMESSAGE";//聊天消息
    //拉货上报定义的板车或者运单type值
    public final static int TYPE_PULL_BOARD = 0;//板车下拉
    public final static int TYPE_PULL_BILL = 1;//运单下拉

    /**
     * 运输类型字段
     */
    public static final String TP_TYPE_DEVICE = "device";//设备保障

    public static final String TP_TYPE_LOAD_K = "load";//宽体机 装机保障

    public static final String TP_TYPE_UNLOAD_K = "unload";//宽体机 卸机保障

    public static final String TP_TYPE_SINGLE = "first_baggage";//单板行李任务

    public static final String TP_TYPE_BAGGAAGE = "baggage";//行李任务

    /**
     * 装卸机 运输 结载 步骤操作code
     */
    public static final String LOAD_ACCEPT = "PassengerLoadReceived";    //结载领受	R	1
    public static final String LOAD_END = "PassengerLoadSend";    //结载 结束 （舱单送达）
    public static final String TP_ACCEPT = "CargoOutTransportReceived";    //运输接受
    public static final String TP_ARRIVED = "CargoOutTransportArrived";    //运输到位
    public static final String TP_START = "CargoOutTransportStart";    //运输开始
    public static final String TP_END = "CargoOutTransportEnd";    //运输 结束

    /**
     *  * 待运区
     *  
     */
    public static final int WAIT_AREA = 1;
    /**
     *  * 临时区
     *  
     */
    public static final int TEMP_AREA = 2;
    /**
     *  * 行李区
     *  
     */
    public static final int BAGGAGE_AREA = 3;
    /**
     *  * 机下
     *  
     */
    public static final int OFF_BOX_AREA = 4;

    /**
     *  * 库区
     *  
     */
    public static final int WAREHOUSE_AREA = 5;

    //实际时间类型
    public static final int TIME_TYPE_AUTUAL = 3;
    //预计时间类型
    public static final int TIME_TYPE_EXCEPT = 2;
    //计划时间
    public static final int TIME_TYPE_PLAN = 1;

    //密码验证成功并且密码满足规范;
    public static final String PSW_TYPE_YES = "0";
    //密码不满足规范
    public static final String PSW_TYPE_NO = "1";
    //密码过期;其他密码验证失败
    public static final String PSW_TYPE_OTHER = "2";


    public static final int TYPE_STEP_OVER = 0;
    public static final int TYPE_STEP_NOW = 1;
    public static final int TYPE_STEP_NEXT = 2;

}
