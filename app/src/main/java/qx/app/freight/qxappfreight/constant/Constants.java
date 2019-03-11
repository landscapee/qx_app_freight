package qx.app.freight.qxappfreight.constant;


import android.os.Environment;

import java.io.File;

public class Constants {

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
     * 预配
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
     * 外场司机
     */
    public static String INSTALL_UNLOAD_EQUIP = "supervision";

    /**
     * 配载
     */
    public static String STOWAGE = "stowage";

    /**
     * 配载
     */
    public static String INPORTDELIVERY = "delivery_in";


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
    public static final int PAGE_SIZE = 10;//列表分页获取的单页最大的数量

}
