package qx.app.freight.qxappfreight.utils;

import android.content.Context;
import android.text.TextUtils;

import java.util.regex.Pattern;


public class StringUtil {
    /**
     * 返回格式化的字符串
     *
     * @param context   上下文
     * @param formatSrc 格式化资源id
     * @param params    不定长参数
     * @return 结果
     */
    public static String format(Context context, int formatSrc, Object... params) {
        return String.format(context.getString(formatSrc), params);
    }

    /**
     * 对得到的字符串进行判断
     *
     * @param response 数据
     * @return 字符串
     */
    public static String toText(String response) {
        String result = "- -";
        if (!TextUtils.isEmpty(response)) {
            result = response;
        }
        return result;
    }

    /**
     * 改变文字的颜色 通过html标签
     * @param content 文字内容
     * @param color 文字颜色 16进制值
     * @return 添加html颜色标签后的文字
     */
    public static String changeTextColor(String content,String color){

        content  = "<font color='" +color + "'>"+content+"</font>";

        return content;
    }

    //判断整数（int）
    public static boolean isInteger(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    //判断浮点数（double和float）
    public static boolean isDouble(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        boolean flag = false;
        try {
            Double.valueOf(str);
            flag = true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return flag;
    }

}
