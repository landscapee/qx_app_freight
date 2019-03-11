package qx.app.freight.qxappfreight.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;

import java.util.HashMap;
import java.util.Map;

public class DeviceInfoUtil {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static Map<String, String> getDeviceInfo(Context context) {
        Map<String, String> result = new HashMap<>();
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint({"MissingPermission", "HardwareIds"}) String androidId = tm.getDeviceId();
        result.put("deviceId", androidId);
        boolean isPad = (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        result.put("deviceType", (isPad) ? "Pad" : "phone");
        result.put("eptModel", Build.BRAND);
        result.put("systemName", "Android");
        result.put("systemCode", Build.VERSION.RELEASE);
        result.put("phoneNumber", tm.getLine1Number());
        return result;
    }

    @SuppressLint("HardwareIds")
    public static String getIMEI(Context ctx) throws SecurityException {
        return ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }
}
