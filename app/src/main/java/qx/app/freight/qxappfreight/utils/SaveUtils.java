package qx.app.freight.qxappfreight.utils;

import java.util.HashMap;

/**
 * 内存保存工具类
 */
public class SaveUtils {
    private HashMap<String, Object> strInfo;
    public static volatile SaveUtils utils;

    /**
     * 单例模式获取
     */
    public static SaveUtils getInstance() {
        if (utils == null) {
            synchronized (SaveUtils.class) {
                if (utils == null) {
                    utils = new SaveUtils();
                }
            }
        }
        return utils;
    }

    private SaveUtils() {
        strInfo = new HashMap<>();
    }

    /**
     * void
     */
    void setValue(String key, Object o) {
        if (strInfo == null) {
            utils = new SaveUtils();
        }
        strInfo.put(key, o);
    }

    /**
     * Object
     */
    Object getValue(String key) {
        return strInfo.get(key);
    }

    /**
     * void
     */
    public void removeAll() {
        strInfo.clear();
    }
}
