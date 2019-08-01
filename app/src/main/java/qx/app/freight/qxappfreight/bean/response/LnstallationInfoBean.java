package qx.app.freight.qxappfreight.bean.response;


import java.util.List;

import lombok.Data;

@Data
public class LnstallationInfoBean {


    /**
     * cargoName :
     * scooters : [{"actWgt":"54","cargoStatus":0,"dest":"CGQ","dst":"","estWgt":"54","pos":"","pri":"1","restrictedCargo":"","tailer":"","type":"BY"},{"actWgt":"12","cargoStatus":0,"dest":"SJW","dst":"","estWgt":"12","pos":"","pri":"1","restrictedCargo":"","tailer":"","type":"BY"}]
     */

    private String cargoName;


    private List<ScootersBean> scooters;


    @Data
    public static class ScootersBean {
        /**
         * actWgt : 54
         * cargoStatus : 0
         * dest : CGQ
         * dst :
         * estWgt : 54
         * pos :
         * pri : 1
         * restrictedCargo :
         * tailer :
         * type : BY
         */

        private String suggestRepository="- -";
        private String goodsPosition="- -";
        private String toCity;
        private String uldCode="- -";
        private String specialNumber="- -";
        private String scooterCode;
        private String mailType;
        private String volume;
        private String total;
        private String weight;



        private String actWgt;
        private int cargoStatus;
        private String dest;
        private String dst;
        private String estWgt;
        private String pos;
        private String pri;
        private String restrictedCargo;
        private String tailer;
        private String type;
        private String serialInd;


    }
}
