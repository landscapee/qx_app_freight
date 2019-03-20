package qx.app.freight.qxappfreight.utils;

public class MapValue {

    /**
     * 根据code 获取 运输位置type文字
     * @param code
     * @return
     */
    public static String getLocationValue(String code){
        String value ="无";
        if ("temp_area".equals(code))
            value = "临时区";
        else if ("waiting_area".equals(code))
            value = "待运区";
        else if ("seat".equals(code))
            value = "机下";
        else if ("baggage_area".equals(code))
            value = "行李区";
        else if ("warehouse".equals(code))
            value = "库区";

        return value;
    }
    /**
     * 根据code 获取 板车类型
     * @param code
     * @return 0-大滚筒（宽），1-大滚筒（窄），2-小滚筒，3-平板
     */
    public static String getCarTypeValue(String code){
        String value ="无";
        if ("0".equals(code))
            value = "大滚筒（宽）";
        else if ("1".equals(code))
            value = "大滚筒（窄）";
        else if ("2".equals(code))
            value = "小滚筒";
        else if ("3".equals(code))
            value = "平板";


        return value;
    }
    public static int getAreaType(String code){
        int areaType = 0;
        if ("temp_area".equals(code))
            areaType = 3;
        else if ("waiting_area".equals(code))
            areaType = 2;
        else if ("seat".equals(code))
            areaType = 4;
        else if ("baggage_area".equals(code))
            areaType = 5;
        else if ("warehouse".equals(code))
            areaType = 1;

        return areaType;
    }

}
