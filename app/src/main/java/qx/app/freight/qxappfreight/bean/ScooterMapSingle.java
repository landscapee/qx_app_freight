package qx.app.freight.qxappfreight.bean;

import java.util.HashMap;

/**
 * 卸机板车存储单列
 */
public class ScooterMapSingle {

    private static volatile ScooterMapSingle singleton;

    private ScooterMapSingle() {
    }

    private static HashMap<String,UnloadScooterListEntity> map = null;

    public static HashMap<String,UnloadScooterListEntity> getInstance() {

        synchronized (ScooterMapSingle.class) {
            if (map == null){
                map = new HashMap <>();
                return map;
            }
        }
        return map;
    }

    public static void clear() {
        map.clear();
        map = null;
    }


}
