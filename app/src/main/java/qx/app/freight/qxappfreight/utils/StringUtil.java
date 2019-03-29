package qx.app.freight.qxappfreight.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
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
    public static SpannableStringBuilder getAutoColorText(String text){
        String splitTexts[]=text.split(":");
        SpannableStringBuilder builderText = new SpannableStringBuilder(text);
        ColorStateList blue = ColorStateList.valueOf(Color.parseColor("#31ccbd"));
        ColorStateList red = ColorStateList.valueOf(Color.RED);
        if (splitTexts.length==2){
            int index=text.indexOf(":");
            boolean flag=text.substring(index+1,index+2).equals("Y");
            TextAppearanceSpan textAppearanceSpan;
            if (flag){
                textAppearanceSpan = new TextAppearanceSpan(null, 0, 0, blue, null);
            }else {
                textAppearanceSpan = new TextAppearanceSpan(null, 0, 0, red, null);
            }
            builderText.setSpan(textAppearanceSpan, index+1,index+2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            return builderText;
        }else {
            List<Integer> indexes=getIndex(text,":");
            for (Integer index:indexes){
                boolean flag=text.substring(index+1,index+2).equals("Y");
                TextAppearanceSpan textAppearanceSpan;
                if (flag){
                    textAppearanceSpan = new TextAppearanceSpan(null, 0, 0, blue, null);
                }else {
                    textAppearanceSpan = new TextAppearanceSpan(null, 0, 0, red, null);
                }
                builderText.setSpan(textAppearanceSpan, index+1,index+2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            return builderText;
        }
    }
    private static List<Integer> getIndex(String strings, String str){
        List<Integer> list=new ArrayList<>();
        int flag=0;
        while (strings.contains(str)){
            //截取包含自身在内的前边部分
            String aa= strings.substring(0,strings.indexOf(str)+str.length());
            flag=flag+aa.length();
            list.add(flag-str.length());
            strings=strings.substring(strings.indexOf(str)+str.length());
        }
        return list;
    }

    /**
     * 转换为 保留两位小数的 字符串
     * @param value
     * @return
     */
    public static String formatString2(Object value){

        return  String.format("%.1f", value);
    }


    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0) && isBlank(str);
    }
    private static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /*** 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /** * 去除特殊字符或将所有中文标号替换为英文标号
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
    public static boolean isContains(String str1, String str2) {
        if (isEmpty(str1))
            return false;
        else return str1.toLowerCase().contains(str2.toLowerCase()) || isEmpty(str2);
    }
}
