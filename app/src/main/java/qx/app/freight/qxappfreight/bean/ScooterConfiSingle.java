package qx.app.freight.qxappfreight.bean;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import qx.app.freight.qxappfreight.utils.Tools;

/**
 * 用户单列
 */
public class ScooterConfiSingle {

    private static volatile ScooterConfiSingle singleton;

    private ScooterConfiSingle() {
    }

    private static Map<String,String> scooterMap = null;

    public static Map<String,String> getInstance() {

        synchronized (ScooterConfiSingle.class) {
            if (scooterMap == null) {
                return new HashMap <String,String>();
            }
        }
        return scooterMap;
    }

    public static void setScooterMap(HashMap<String,String> scooterMap1) {
            try {
                scooterMap = Tools.IOclone(scooterMap1) ;
            }
            catch (Exception e)
            {
                Log.e("scooterMap","板车信息map深拷贝失败");
            }

    }

    public static void ScooterMapClear() {
        if (scooterMap != null){
            scooterMap.clear();
            scooterMap = null;
        }

    }


}
