package qx.app.freight.qxappfreight.bean;

import java.util.List;

import lombok.Data;

@Data
public class InportTallyBean {

    /**
     * id : f3cf84e2be218b61971c69bcb78a3f75
     * waybillCode : 123
     * indicator : D
     * flightNo : GJ8685
     * flightInfoId : 0d6aec577d77477ea0545daef1798a70
     * flightId : 11956178
     * mailType : AWBA
     * documentDelivery : 123
     * totalNumberPackages : 123
     * totalWeight : 123
     * specialCargoCode : 123
     * commodityName : 123
     * consignee : 123
     * consigneePhone : 123
     * consigneeCarid : 123
     * forwarderId : null
     * forwarderName : null
     * forwarderPhone : null
     * priority : 123
     * buckleGoods : 123
     * transit : 123
     * refrigerated : 123
     * devanning : 123
     * originatingStation : HGH
     * originatingStationCn :
     * destinationStation : CTU
     * destinationStationCn : 成都
     * manifestTotal : 0
     * manifestWeight : 0
     * warehouseArea : null
     * warehouseLocation : null
     * putStorageTime : null
     * putStorageUser : null
     * outStorageTime : null
     * outStorageUser : null
     * tallyingTotal : 0
     * tallyingWeight : 0
     * waybillStatus : 1
     * waybillCodeScanUrl :
     * commodityScanUrl :
     * waybillScanUrl :
     * createDate : 1552633859010
     * createUser : uc6e1d3a2d7cb408eba58c10d92518b9e
     * updateDate : 1552633887840
     * updateUser : system
     * ubnormalType : null
     * ubnormalNum : null
     */

    private String id;
    private String waybillCode;
    private String indicator;
    private String flightNo;
    private String flightInfoId;
    private int flightId;
    private String mailType;
    private int documentDelivery;
    private int totalNumberPackages;
    private double totalWeight;
    private String specialCargoCode;
    private String commodityName;
    private String consignee;
    private String consigneePhone;
    private String consigneeCarid;
    private Object forwarderId;
    private Object forwarderName;
    private Object forwarderPhone;
    private int priority;
    private int buckleGoods;
    private int transit;
    private int refrigerated;
    private int devanning;
    private String originatingStation;
    private String originatingStationCn;
    private String destinationStation;
    private String destinationStationCn;
    private int manifestTotal;
    private double manifestWeight;
    private Object warehouseArea;
    private Object warehouseLocation;
    private Object putStorageTime;
    private Object putStorageUser;
    private Object outStorageTime;
    private Object outStorageUser;
    private int tallyingTotal;
    private double tallyingWeight;
    private int waybillStatus;
    private String waybillCodeScanUrl;
    private String commodityScanUrl;
    private String waybillScanUrl;
    private long createDate;
    private String createUser;
    private long updateDate;
    private String updateUser;
    private List<Integer> ubnormalType;
    private Object ubnormalNum;
}
