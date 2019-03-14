package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
public class TransportListBean implements Serializable {

    /**
     * taskId : 522660
     * taskType : 收运
     * taskTypeCode : collection
     * taskStartTime : null
     * taskEndTime : null
     * taskResult : null
     * stepOrder : 4
     * id : b77f73826693dc6a3e6245404b7f103f
     * waybillCode : 0281616161
     * mailType : C
     * flightNumber : EU9568
     * flightDate : 1546444800000
     * originatingStation : null
     * destinationStation : null
     * consignee : 青宵科技
     * consigneePhone : 13654988874
     * consigneePostcode : jsows@foxmail.com
     * consigneeAddress : 四川省成都市双流机场1号
     * specialCargoCode : 241872
     * coldStorage : 1
     * totalNumberPackages : 100.0
     * billingWeight : 500.0
     * totalWeight : 100.0
     * refrigeratedTemperature : 0.0
     * activitiId : 530005
     * internalTransferDate : null
     * internalTransferFlight : null
     * internalTransferWaybill : null
     * exchangeWaybillId : null
     * exchangeNewWaybill : null
     * exchangeWaybillBefore : null
     * status : 3
     * shipperCompanyId : enbc55b2a4218a4261ac7083ccaa69c288
     * createUser : null
     * createTime : 1549877417822
     * lastUpdateTime : 1549878311614
     * lastUpdateOperator : null
     * delFlag : 0
     * applyTime : null
     * expectedDeliveryTime : null
     * expectedDeliveryGate : null
     * chargeFlag : 0
     * virtualFlag : 0
     * otherFlag : 0
     * flightId : null
     * declareItem : [{"itemId":null,"waybillId":"b77f73826693dc6a3e6245404b7f103f","cargoId":"7192c890c57d90b76cec8c5e53063ff7","number":100,"weight":1400,"volume":100,"packagingType":["包装","包装2"],"cargoCn":"梅花"}]
     * declareWaybillAddition : {"id":"f777b4e451ea3f633f2187c5407102de","waybillId":"b77f73826693dc6a3e6245404b7f103f","addtionInvoices":"[]"}
     * flightNo : null
     * aircraftType : null
     * associateAirport : null
     * etd : null
     * outboundNumber  0  已出库单数
     * waybillCount  3 总运单数
     */
    private String stepOwner;
    private String taskId;
    private String taskType;
    private String taskTypeCode;
    private Object taskStartTime;
    private Object taskEndTime;
    private Object taskResult;
    private int stepOrder;
    private String id;
    private String waybillCode;
    private String mailType;
    private String flightNumber;
    private long flightDate;
    private Object originatingStation;
    private Object destinationStation;
    private String consignee;
    private String consigneePhone;
    private String consigneePostcode;
    private String consigneeAddress;
    private String specialCargoCode;
    private String coldStorage;
    private String totalNumberPackages;
    private String billingWeight;
    private String totalWeight;
    private String refrigeratedTemperature;
    private String activitiId;
    private Object internalTransferDate;
    private Object internalTransferFlight;
    private Object internalTransferWaybill;
    private Object exchangeWaybillId;
    private Object exchangeNewWaybill;
    private Object exchangeWaybillBefore;
    private String status;
    private String shipperCompanyId;
    private Object createUser;
    private String createTime;
    private String lastUpdateTime;
    private Object lastUpdateOperator;
    private String delFlag;
    private Object applyTime;
    private long expectedDeliveryTime;
    private Object expectedDeliveryGate;
    private String chargeFlag;
    private String virtualFlag;
    private String otherFlag;
    private String flightId;
    private DeclareWaybillAdditionBean declareWaybillAddition;
    private String flightNo;
    private int outboundNumber;
    private int waybillCount;
    private Object aircraftType;
    private Object associateAirport;
    private String spotFlag;
    private long etd;
    private List<DeclareItemBean> declareItem;
    private boolean isExpand = false;

    private String serialNumber;  //流水号
    @Data
    public static class DeclareWaybillAdditionBean implements Serializable{
        /**
         * id : f777b4e451ea3f633f2187c5407102de
         * waybillId : b77f73826693dc6a3e6245404b7f103f
         * addtionInvoices : []
         */
        private String id;
        private String waybillId;
        private String addtionInvoices;
    }
    @Data
    public static class DeclareItemBean implements Serializable {
        /**
         * itemId : null
         * waybillId : b77f73826693dc6a3e6245404b7f103f
         * cargoId : 7192c890c57d90b76cec8c5e53063ff7
         * number : 100
         * weight : 1400
         * volume : 100
         * packagingType : ["包装","包装2"]
         * cargoCn : 梅花
         */
        private Object itemId;
        private String waybillId;
        private String cargoId;
        private int number;
        private double weight;
        private double volume;
        private String cargoCn;
        private List<String> packagingType;
    }
}
