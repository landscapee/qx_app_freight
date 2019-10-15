package qx.app.freight.qxappfreight.bean.response;


import java.util.List;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.ManifestScooterListBean;

@Data
public class LnstallationInfoBean {


    /**
     * cargoName :
     * scooters : [{"actWgt":"54","cargoStatus":0,"dest":"CGQ","dst":"","estWgt":"54","pos":"","pri":"1","specialCode":"","tailer":"","type":"BY"},{"actWgt":"12","cargoStatus":0,"dest":"SJW","dst":"","estWgt":"12","pos":"","pri":"1","specialCode":"","tailer":"","type":"BY"}]
     */

    private String cargoName;


    private List<ScootersBean> scooters;


    @Data
    public static class ScootersBean {
        private String location;//货位
        private String uldCode = "- -";//
        private String serialInd;//
        private String specialNumber = "- -";//
        private String specialCode;//特货代码
        private String cargoName = "- -";//舱位
        private String oldCargoName;//舱位
        private long createTime;
        private String createUser;//创建人
        private String destinationStation;//目的站
        private int exceptionFlag;//1表示建议拉下
        private String flightInfoId;
        private String id;
        private String reportInfoId;
        private String scooterCode;//板车号
        private String total;//总数量
        private String type;//类型
        private int version;//版本号
        private List<ManifestScooterListBean.WaybillListBean> waybillList;
        private String weight;//重量
        private boolean change;//是否修改过
    }
}
