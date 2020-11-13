package qx.app.freight.qxappfreight.utils;

import qx.app.freight.qxappfreight.bean.ScooterConfiSingle;

public class MapValue {

    /**
     * 根据code 获取 运输位置type文字
     * @param code
     * @return
     */
    public static String getLocationValue(String code){
        String value ="无";
        if ("temp_area".equals(code)) {
            value = "临时区";
        } else if ("waiting_area".equals(code)) {
            value = "待运区";
        } else if ("seat".equals(code)) {
            value = "机下";
        } else if ("baggage_area".equals(code)) {
            value = "行李区";
        } else if ("warehouse_area".equals(code)) {
            value = "库区";
        }
        return value;
    }
    /**
     * 根据code 获取 板车类型
     * @param code
     * @return 9-大滚筒-宽，，8-小滚筒，7-平板 6大滚筒-窄";
     */
    public static String getCarTypeValue(String code){

        String value ="无";
//        if ("9".equals(code))
//            value = "大滚筒-宽";
//        else if ("8".equals(code))
//            value = "小滚筒";
//        else if ("7".equals(code))
//            value = "平板";
//        else if ("6".equals(code))
//            value = "大滚筒-窄";
        if (ScooterConfiSingle.getInstance().isEmpty()) {
            return value;
        } else {
            return ScooterConfiSingle.getInstance().get(code);
        }
    }

    /**
     * 获取运输任务类型
     * @param code
     * @return
     */
    public static String getProjectName(String code){
        String projectName = "";
        if ("CargoOutTransport".equals(code)) {
            projectName = "货物运输";
        } else if ("CargoSiteClearing".equals(code)) {
            projectName = "货物清场";
        } else if ("LuggageTransport".equals(code)) {
            projectName = "行李运输";
        } else if ("EquipmentGuarantee".equals(code)) {
            projectName = "设备保障";
        }

        return projectName;
    }
    public static int getAreaType(String code){
        int areaType = 0;
        if ("temp_area".equals(code)) {
            areaType = 3;
        } else if ("waiting_area".equals(code)) {
            areaType = 2;
        } else if ("seat".equals(code)) {
            areaType = 4;
        } else if ("baggage_area".equals(code)) {
            areaType = 5;
        } else if ("warehouse".equals(code)) {
            areaType = 1;
        }

        return areaType;
    }


    public static String getHYOfZP(String code){
        String hyType = "";
        if ("0".equals(code)) {
            hyType = "8";
        } else if ("1".equals(code)) {
            hyType = "7";
        } else if ("2".equals(code)) {
            hyType = "9";
        } else if ("3".equals(code)) {
            hyType = "8";
        } else if ("4".equals(code)) {
            hyType = "6";
        }

        return hyType;
    }

}
