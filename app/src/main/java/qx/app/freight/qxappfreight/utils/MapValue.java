package qx.app.freight.qxappfreight.utils;

public class MapValue {

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
