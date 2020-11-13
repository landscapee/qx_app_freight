package qx.app.freight.qxappfreight.utils.pushUtil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.security.MessageDigest;
import java.util.UUID;


public class StringUtil {

    public static char convertDigit(int value) {
        value &= 0x0f;
        if (value >= 10) {
            return ((char) (value - 10 + 'a'));
        } else {
            return ((char) (value + '0'));
        }
    }

    public static String convert(final byte[] bytes) {

        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            sb.append(convertDigit((int) (bytes[i] >> 4)));
            sb.append(convertDigit((int) (bytes[i] & 0x0f)));
        }
        return (sb.toString());

    }

    public static String convert(final byte[] bytes, int pos, int len) {

        StringBuffer sb = new StringBuffer(len * 2);
        for (int i = pos; i < pos + len; i++) {
            sb.append(convertDigit((int) (bytes[i] >> 4)));
            sb.append(convertDigit((int) (bytes[i] & 0x0f)));
        }
        return (sb.toString());

    }

    public static byte[] md5Byte(String encryptStr) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(encryptStr.getBytes("UTF-8"));
        return md.digest();
    }

    public static String md5(String encryptStr) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(encryptStr.getBytes("UTF-8"));
        byte[] digest = md.digest();
        StringBuffer md5 = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            md5.append(Character.forDigit((digest[i] & 0xF0) >> 4, 16));
            md5.append(Character.forDigit((digest[i] & 0xF), 16));
        }

        encryptStr = md5.toString();
        return encryptStr;
    }

    public static boolean hasNetwork(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static String GetFiledID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "");
    }

    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0) && isBlank(str);
    }

    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    public static boolean isContains(String str1, String str2) {
        if (isEmpty(str1)) {
            return false;
        } else if (str1.toLowerCase().contains(str2.toLowerCase()) || isEmpty(str2)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEuqals(String str1, String str2) {
        if (isEmpty(str1) || isEmpty(str2)) {
            return false;
        } else if (str1.equals(str2)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 改变文字的颜色 通过html标签
     * @param content 文字内容
     * @param color 文字颜色 16进制值
     * @return 添加html颜色标签后的文字
     */
    public static String changeTextColor(String content, String color){

        content  = "<font color='" +color + "'>"+content+"</font>";

        return content;
    }
}
