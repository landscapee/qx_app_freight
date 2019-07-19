package qx.app.freight.qxappfreight.bean;

import java.util.List;

import lombok.Data;

@Data
public class GetWaybillInfoByIdDataBean {
    private String status;
    private String message;
    private String rowCount;
    private DataMainBean data;

    @Data
    public static class DataMainBean {

        /**
         * id : faf8fa5f5d5b4b8dab8e956af89ba7a5
         * waybillCode : 029-77777770
         * mailType : C
         * flightNumber : CZ6184
         * flightDate : 1557391800000
         * originatingStation : CTU
         * destinationStation : 北京
         * consignee : zzq
         * consigneePhone : 13199998888
         * consigneePostcode : 622222
         * consigneeAddress : null
         * specialCargoCode : AVI
         * coldStorage : 0
         * totalNumberPackages : 6.0
         * billingWeight : null
         * totalWeight : 6.0
         * totalVolume : 6
         * refrigeratedTemperature : 0*0
         * shipperCompanyId : dep6061008ccaa1403ca95e93f87f2e4e91
         * flightId : 529079d8693445dcaf79a5ffc102bae5
         * activitiId : 72795858
         * internalTransferDate : null
         * internalTransferFlight : null
         * internalTransferWaybill : null
         * exchangeFlag : 0
         * exchangeBeforeCode : null
         * status : 11
         * createUser : u90eab98bad7d407ca9ff7537f5e4367a
         * createTime : 1557387928473
         * lastUpdateTime : 1557389629178
         * lastUpdateOperator : null
         * delFlag : 0
         * chargeFlag : 1
         * virtualFlag : 0
         * otherFlag : 0
         * applyTime : 1557387548719
         * expectedDeliveryGate : D口
         * expectedDeliveryTime : 1557387928473
         * receivecargoNumber : 6.0
         * receivecargoWeight : 6.0
         * receivecargoVolumn : 6.0
         * storageTime : 1557387957956
         * bigFlag : 1
         * exchangeNumber : 0
         * transferStation : --
         * returnOutTransportFlag : 0
         * exceptionFlag : 0
         * declareItems : [{"itemId":"12c3841d80ecf1348fe332981bc38838","waybillId":"faf8fa5f5d5b4b8dab8e956af89ba7a5","cargoId":"432dbb9469b54e23a759c99f47c6763f","number":null,"weight":null,"volume":null,"packagingType":null,"cargoCn":"狗","type":0}]
         * declareItem : null
         * freightName : 国内出港项目
         * flightName : CZ
         * newDeclareWaybillCode : null
         * declareWaybillAddition : null
         * scooterId : null
         * spotFlag : 0
         * transferStationTs : null
         * transferStationCn : null
         * destinationStationCn : null
         * remark : null
         * updateType : 0
         * waybillType : 0
         * paWaybillId : null
         * startUpdateTime : null
         * endUpdateTime : null
         * waybillDestinationStation : 北京
         * cargoCn : 狗
         * packagingType : 木盒
         * cargoType : null
         * waybillDestinationStationCn : null
         * originatingStationCn : null
         * updateWeights : null
         * waybillAirportDestinationStationName : null
         */
        private int billItemNumber;//可分装的运单件数
        private double billItemWeight;//可分装的运单重量
        private String cargoType;//M,邮件,C，货物
        private String flightSystemId;//航班数字id

        private String id;
        private String waybillCode;
        private String mailType;
        private String flightNumber;
        private long flightDate;
        private String originatingStation;
        private String destinationStation;
        private String consignee;
        private String consigneePhone;
        private String consigneePostcode;
        private Object consigneeAddress;
        private String specialCargoCode;
        private int coldStorage;
        private double totalNumber;
        private Object billingWeight;
        private double totalWeight;
        private int totalVolume;
        private String refrigeratedTemperature;
        private String shipperCompanyId;
        private String flightId;
        private String activitiId;
        private Object internalTransferDate;
        private Object internalTransferFlight;
        private Object internalTransferWaybill;
        private int exchangeFlag;
        private Object exchangeBeforeCode;
        private int status;
        private String createUser;
        private long createTime;
        private long lastUpdateTime;
        private Object lastUpdateOperator;
        private int delFlag;
        private int chargeFlag;
        private int virtualFlag;
        private int otherFlag;
        private long applyTime;
        private String expectedDeliveryGate;
        private long expectedDeliveryTime;
        private double receivecargoNumber;
        private double receivecargoWeight;
        private double receivecargoVolumn;
        private long storageTime;
        private int bigFlag;
        private int exchangeNumber;
        private String transferStation;
        private int returnOutTransportFlag;
        private int exceptionFlag;
        private Object declareItem;
        private String freightName;
        private String flightName;
        private Object newDeclareWaybillCode;
        private Object declareWaybillAddition;
        private Object scooterId;
        private int spotFlag;
        private Object transferStationTs;
        private Object transferStationCn;
        private Object destinationStationCn;
        private Object remark;
        private int updateType;
        private int waybillType;
        private Object paWaybillId;
        private Object startUpdateTime;
        private Object endUpdateTime;
        private String waybillDestinationStation;
        private String cargoCn;
        private String packagingType;
        private Object waybillDestinationStationCn;
        private Object originatingStationCn;
        private Object updateWeights;
        private Object waybillAirportDestinationStationName;
        private List<DeclareItemsBean> declareItems;

        @Data
        public static class DeclareItemsBean {
            /**
             * itemId : 12c3841d80ecf1348fe332981bc38838
             * waybillId : faf8fa5f5d5b4b8dab8e956af89ba7a5
             * cargoId : 432dbb9469b54e23a759c99f47c6763f
             * number : null
             * weight : null
             * volume : null
             * packagingType : null
             * cargoCn : 狗
             * type : 0
             */

            private String itemId;
            private String waybillId;
            private String cargoId;
            private Object number;
            private Object weight;
            private Object volume;
            private Object packagingType;
            private String cargoCn;
            private int type;
        }
    }
}
