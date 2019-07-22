package qx.app.freight.qxappfreight.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import qx.app.freight.qxappfreight.bean.InstallEquipEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.constant.Constants;


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
        if (!TextUtils.isEmpty(response) && !"null".equals(response)) {
            result = response;
        }
        return result;
    }

    /**
     * 对字符串做非null 判断 返回想要的 默认值
     *
     * @param response
     * @param defaultStr 默认值
     * @return
     */
    public static String toText(String response, String defaultStr) {
        String result = defaultStr;
        if (!TextUtils.isEmpty(response) && !"null".equals(response)) {
            result = response;
        }
        return result;
    }

    /**
     * 改变文字的颜色 通过html标签
     *
     * @param content 文字内容
     * @param color   文字颜色 16进制值
     * @return 添加html颜色标签后的文字
     */
    public static String changeTextColor(String content, String color) {

        content = "<font color='" + color + "'>" + content + "</font>";

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

    public static SpannableStringBuilder getAutoColorText(String text) {
        String splitTexts[] = text.split(":");
        SpannableStringBuilder builderText = new SpannableStringBuilder(text);
        ColorStateList blue = ColorStateList.valueOf(Color.parseColor("#31ccbd"));
        ColorStateList red = ColorStateList.valueOf(Color.RED);
        if (splitTexts.length == 2) {
            int index = text.indexOf(":");
            boolean flag = text.substring(index + 1, index + 2).equals("Y");
            TextAppearanceSpan textAppearanceSpan;
            if (flag) {
                textAppearanceSpan = new TextAppearanceSpan(null, 0, 0, blue, null);
            } else {
                textAppearanceSpan = new TextAppearanceSpan(null, 0, 0, red, null);
            }
            builderText.setSpan(textAppearanceSpan, index + 1, index + 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            return builderText;
        } else {
            List <Integer> indexes = getIndex(text, ":");
            for (Integer index : indexes) {
                boolean flag = text.substring(index + 1, index + 2).equals("Y");
                TextAppearanceSpan textAppearanceSpan;
                if (flag) {
                    textAppearanceSpan = new TextAppearanceSpan(null, 0, 0, blue, null);
                } else {
                    textAppearanceSpan = new TextAppearanceSpan(null, 0, 0, red, null);
                }
                builderText.setSpan(textAppearanceSpan, index + 1, index + 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            return builderText;
        }
    }

    private static List <Integer> getIndex(String strings, String str) {
        List <Integer> list = new ArrayList <>();
        int flag = 0;
        while (strings.contains(str)) {
            //截取包含自身在内的前边部分
            String aa = strings.substring(0, strings.indexOf(str) + str.length());
            flag = flag + aa.length();
            list.add(flag - str.length());
            strings = strings.substring(strings.indexOf(str) + str.length());
        }
        return list;
    }

    /**
     * 转换为 保留两位小数的 字符串
     *
     * @param value
     * @return
     */
    public static String formatString2(Object value) {
        if (null == value)
            return "0";
        return String.format("%.1f", value);
    }

    /**
     * 去掉后面无用的零
     *
     * @param value
     * @return
     */
    public static String formatStringDeleteDot(String value) {
        if (value == null || "".equals(value) || !isNumeric(value))
            return "0";
        if (value.indexOf(".") > 0) {
            //正则表达
            value = value.replaceAll("0+?$", "");//去掉后面无用的零

            value = value.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点

        }
        return value;
    }

    /**
     *      * 匹配是否包含数字
     *      * @param str 可能为中文，也可能是-19162431.1254，不使用BigDecimal的话，变成-1.91624311254E7
     *      * @return
     *      * @author yutao
     *      * @date 2016年11月14日下午7:41:22
     *      
     */
    public static boolean isNumeric(String str) {
        // 该正则表达式可以匹配所有的数字 包括负数
        Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }

        Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
        if (!isNum.matches()) {
            return false;
        }
        return true;
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

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
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

    /**
     * 设置航线数据
     *
     * @param route  航线数据
     * @param entity 需要设置航线数据的实体
     */
    public static void setFlightRoute(String route, InstallEquipEntity entity) {
        if (route == null) {//根据航线信息字符串数组设置起点、中点、终点的数据显示
            entity.setFlightInfoList(new ArrayList <>());
        } else {
            String[] placeArray = route.split(",");
            List <String> resultList = new ArrayList <>();
            List <String> placeList = new ArrayList <>(Arrays.asList(placeArray));
            for (String str : placeList) {
                String temp = str.replaceAll("[^(a-zA-Z\\u4e00-\\u9fa5)]", "");
                resultList.add(temp);
            }
            entity.setFlightInfoList(resultList);
        }
    }

    /**
     * 解析航线数据，返回航站三字码列表
     *
     * @param route
     * @return
     */
    public static List <String> getFlightList(String route) {
        List <String> result = new ArrayList <>();
        if (route != null) {
            String[] placeArray = route.split(",");
            List <String> placeList = new ArrayList <>(Arrays.asList(placeArray));
            for (String str : placeList) {
                String temp = str.replaceAll("[^(a-zA-Z\\u4e00-\\u9fa5)]", "");
                result.add(temp);
            }
        }
        return result;
    }

    /**
     * 判断时间字符串为空
     *
     * @param time 参数
     * @return 结果
     */
    public static boolean isTimeNull(String time) {
        boolean result = false;
        if (time == null || "0".equals(time)) {
            result = true;
        }
        return result;
    }

    /**
     * 根据数据设置时间和时间显示类型
     *
     * @param bean   服务器传回的数据
     * @param entity 本地封装的数据model
     */
    public static void setTimeAndType(LoadAndUnloadTodoBean bean, InstallEquipEntity entity) {
        String time;
        int timeType;
        if (bean.getTaskType() == 2 || bean.getTaskType() == 5) {//卸机或装卸机任务显示时间
            if (!StringUtil.isTimeNull(String.valueOf(bean.getAta()))) {//实际到达时间
                time = TimeUtils.getHMDay(bean.getAta());
                timeType = Constants.TIME_TYPE_AUTUAL;
            } else if (!StringUtil.isTimeNull(String.valueOf(bean.getEta()))) {//预计到达时间
                time = TimeUtils.getHMDay(bean.getEta());
                timeType = Constants.TIME_TYPE_EXCEPT;
            } else {//计划到达时间
                time = TimeUtils.getHMDay(bean.getSta());
                timeType = Constants.TIME_TYPE_PLAN;
            }
        } else {//装机机任务显示时间
            if (!StringUtil.isTimeNull(String.valueOf(bean.getAtd()))) {//实际出港时间
                time = TimeUtils.getHMDay(bean.getAtd());
                timeType = Constants.TIME_TYPE_AUTUAL;
            } else if (!StringUtil.isTimeNull(String.valueOf(bean.getEtd()))) {//预计出港时间
                time = TimeUtils.getHMDay(bean.getEtd());
                timeType = Constants.TIME_TYPE_EXCEPT;
            } else {//计划时间
                time = TimeUtils.getHMDay(bean.getStd());
                timeType = Constants.TIME_TYPE_PLAN;
            }
        }
        entity.setTimeForShow(time);
        entity.setTimeType(timeType);
    }
    /**
     * 根据数据设置时间和时间显示类型
     *
     * @param bean   服务器传回的数据
     */
    public static void setTimeAndType(LoadAndUnloadTodoBean bean) {
        String time;
        int timeType;
        if (bean.getTaskType() == 2 || bean.getTaskType() == 5) {//卸机或装卸机任务显示时间
            if (!StringUtil.isTimeNull(String.valueOf(bean.getAta()))) {//实际到达时间
                time = TimeUtils.getHMDay(bean.getAta());
                timeType = Constants.TIME_TYPE_AUTUAL;
            } else if (!StringUtil.isTimeNull(String.valueOf(bean.getEta()))) {//预计到达时间
                time = TimeUtils.getHMDay(bean.getEta());
                timeType = Constants.TIME_TYPE_EXCEPT;
            } else {//计划到达时间
                time = TimeUtils.getHMDay(bean.getSta());
                timeType = Constants.TIME_TYPE_PLAN;
            }
        } else {//装机机任务显示时间
            if (!StringUtil.isTimeNull(String.valueOf(bean.getAtd()))) {//实际出港时间
                time = TimeUtils.getHMDay(bean.getAtd());
                timeType = Constants.TIME_TYPE_AUTUAL;
            } else if (!StringUtil.isTimeNull(String.valueOf(bean.getEtd()))) {//预计出港时间
                time = TimeUtils.getHMDay(bean.getEtd());
                timeType = Constants.TIME_TYPE_EXCEPT;
            } else {//计划时间
                time = TimeUtils.getHMDay(bean.getStd());
                timeType = Constants.TIME_TYPE_PLAN;
            }
        }
        bean.setTimeForShow(time);
        bean.setTimeType(timeType);
    }
    /**
     * 设置航线数据
     *
     * @param route  航线数据
     * @param entity 需要设置航线数据的实体
     */
    public static void setFlightRoute(String route, LoadAndUnloadTodoBean entity) {
        if (route == null) {//根据航线信息字符串数组设置起点、中点、终点的数据显示
            entity.setFlightInfoList(new ArrayList <>());
        } else {
            String[] placeArray = route.split(",");
            List <String> resultList = new ArrayList <>();
            List <String> placeList = new ArrayList <>(Arrays.asList(placeArray));
            for (String str : placeList) {
                String temp = str.replaceAll("[^(a-zA-Z\\u4e00-\\u9fa5)]", "");
                resultList.add(temp);
            }
            entity.setFlightInfoList(resultList);
        }
    }
}
