package qx.app.freight.qxappfreight.bean;

import java.util.List;

import lombok.Data;

/**
 * 包含货邮舱单列表数据的原始数据封装model
 */
@Data
public class ManifestMainBean {

    /**
     * toCityEn : DSN
     * route : 成都-鄂尔多斯
     * routeEn : CTU-DSN
     * toCity : 鄂尔多斯
     * cargos : [{"volume":0,"total":4112,"cargoName":"1H","nothing":"/","weight":301,"scooters":[{"scooterType":2,"waybillList":[{"cargoCn":"仁川包量(25-35吨)","updateFlag":1,"addAndOnceAgainBtnIsShow":false,"runTimeScooterId":"1343edc72d7e4ca39307a4fae3e4db44","scooterType":0,"number":12,"inflightnum":0,"updateWeight":"-7","changeAndOnceAgainBtnIsShow":true,"totalnum":0,"receivecargoWeight":12,"waybillId":"67b77876e45a4dd4a464e376232984c1","correctWeight":"-7","receivecargoVolumn":0,"weightDisplay":"12/12","groupScooterStatus":0,"numberDisplay":"12/12","weight":12,"inFlight":0,"realWeight":"5","scooterWeight":0,"volume":0,"receivecargoNumber":12,"mailType":"C","onceAgainBtnIsShow":true,"waybillCode":"100-10000001"}],"scooterId":"11dfb21e430844459752ce0587d1c69d","total":4112,"deleteStatus":0,"reWeight":820,"suggestRepository":"1H","personUpdateValue":0,"scooterStatus":0,"reDifferenceRate":-0.12,"weight":301,"index":0,"scooterWeight":520,"uldWeight":0,"volume":0,"flightInfoId":"3513979faa9d4d8f9d71d5d774e2cd51","addWeight":0,"scooterCode":"24031","reDifference":-1}]}]
     * id : 9aa03b0d-9598-4ec7-b5e5-d49ae4b51a8c
     */

    private String toCityEn;
    private String route;
    private String routeEn;
    private String toCity;
    private String id;
    private List<CargosBean> cargos;
@Data
    public static class CargosBean {
        /**
         * volume : 0
         * total : 4112
         * cargoName : 1H
         * nothing : /
         * weight : 301
         * scooters : [{"scooterType":2,"waybillList":[{"cargoCn":"仁川包量(25-35吨)","updateFlag":1,"addAndOnceAgainBtnIsShow":false,"runTimeScooterId":"1343edc72d7e4ca39307a4fae3e4db44","scooterType":0,"number":12,"inflightnum":0,"updateWeight":"-7","changeAndOnceAgainBtnIsShow":true,"totalnum":0,"receivecargoWeight":12,"waybillId":"67b77876e45a4dd4a464e376232984c1","correctWeight":"-7","receivecargoVolumn":0,"weightDisplay":"12/12","groupScooterStatus":0,"numberDisplay":"12/12","weight":12,"inFlight":0,"realWeight":"5","scooterWeight":0,"volume":0,"receivecargoNumber":12,"mailType":"C","onceAgainBtnIsShow":true,"waybillCode":"100-10000001"}],"scooterId":"11dfb21e430844459752ce0587d1c69d","total":4112,"deleteStatus":0,"reWeight":820,"suggestRepository":"1H","personUpdateValue":0,"scooterStatus":0,"reDifferenceRate":-0.12,"weight":301,"index":0,"scooterWeight":520,"uldWeight":0,"volume":0,"flightInfoId":"3513979faa9d4d8f9d71d5d774e2cd51","addWeight":0,"scooterCode":"24031","reDifference":-1}]
         */

        private int volume;
        private int total;
        private String cargoName;
        private String nothing;
        private int weight;
        private List<ManifestScooterListBean> scooters;
    }
}
