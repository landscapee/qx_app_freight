package qx.app.freight.qxappfreight.bean;

import java.util.List;

import lombok.Data;

/**
 * 货邮舱单列表数据
 */
@Data
public class ManifestScooterListBean {

    /**
     * scooterType : 2
     * waybillList : [{"cargoCn":"仁川包量(25-35吨)","updateFlag":1,"addAndOnceAgainBtnIsShow":false,"runTimeScooterId":"1343edc72d7e4ca39307a4fae3e4db44","scooterType":0,"number":12,"inflightnum":0,"updateWeight":"-7","changeAndOnceAgainBtnIsShow":true,"totalnum":0,"receivecargoWeight":12,"waybillId":"67b77876e45a4dd4a464e376232984c1","correctWeight":"-7","receivecargoVolumn":0,"weightDisplay":"12/12","groupScooterStatus":0,"numberDisplay":"12/12","weight":12,"inFlight":0,"realWeight":"5","scooterWeight":0,"volume":0,"receivecargoNumber":12,"mailType":"C","onceAgainBtnIsShow":true,"waybillCode":"100-10000001"}]
     * scooterId : 11dfb21e430844459752ce0587d1c69d
     * total : 4112
     * deleteStatus : 0
     * reWeight : 820
     * suggestRepository : 1H
     * personUpdateValue : 0
     * scooterStatus : 0
     * reDifferenceRate : -0.12
     * weight : 301
     * index : 0
     * scooterWeight : 520
     * uldWeight : 0
     * volume : 0
     * flightInfoId : 3513979faa9d4d8f9d71d5d774e2cd51
     * addWeight : 0
     * scooterCode : 24031
     * reDifference : -1
     */
    /**
     * 缺的数据
     */
    private String toCity;
    private String uldCode;
    private String uldType;
    private String iata;
    private String mailType;
    private String goodsPosition ;
    private String specialNumber;
    private List<String> manifestList;

    private int scooterType;
    private String scooterId;
    private String total;
    private int deleteStatus;
    private int reWeight;
    private String suggestRepository;
    private int personUpdateValue;
    private int scooterStatus;
    private double reDifferenceRate;
    private String weight;
    private int index;
    private int scooterWeight;
    private int uldWeight;
    private String volume;
    private String flightInfoId;
    private int addWeight;
    private String scooterCode;
    private int reDifference;
    private List<WaybillListBean> waybillList;

    private boolean pull;//是否拉下状态

    @Data
    public static class WaybillListBean {
        /**
         * cargoCn : 仁川包量(25-35吨)
         * updateFlag : 1
         * addAndOnceAgainBtnIsShow : false
         * runTimeScooterId : 1343edc72d7e4ca39307a4fae3e4db44
         * scooterType : 0
         * number : 12
         * inflightnum : 0
         * updateWeight : -7
         * changeAndOnceAgainBtnIsShow : true
         * totalnum : 0
         * receivecargoWeight : 12
         * waybillId : 67b77876e45a4dd4a464e376232984c1
         * correctWeight : -7
         * receivecargoVolumn : 0
         * weightDisplay : 12/12
         * groupScooterStatus : 0
         * numberDisplay : 12/12
         * weight : 12
         * inFlight : 0
         * realWeight : 5
         * scooterWeight : 0
         * volume : 0
         * receivecargoNumber : 12
         * mailType : C
         * onceAgainBtnIsShow : true
         * waybillCode : 100-10000001
         */

        private String cargoCn;
        private int updateFlag;
        private boolean addAndOnceAgainBtnIsShow;
        private String runTimeScooterId;
        private int scooterType;
        private String number;
        private int inflightnum;
        private String updateWeight;
        private boolean changeAndOnceAgainBtnIsShow;
        private int totalnum;
        private String receivecargoWeight;
        private String waybillId;
        private String correctWeight;
        private String receivecargoVolumn;
        private String weightDisplay;
        private int groupScooterStatus;
        private String numberDisplay;
        private String weight;
        private int inFlight;
        private String realWeight;
        private String scooterWeight;
        private String volume;
        private int receivecargoNumber;
        private String mailType;
        private boolean onceAgainBtnIsShow;
        private String waybillCode;
        private boolean liveBody;//是否是活体
        private String specialCode;
        private String model; //型号
        private String suggestRepository;//仓位
        private String routeEn;//航线信息
        private String info;//备注
    }
}
