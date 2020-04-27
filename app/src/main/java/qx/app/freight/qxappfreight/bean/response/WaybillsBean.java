package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class WaybillsBean implements Serializable {
    /**
     * id : a2c4963e2e646e9e8660c03511282661
     * waybillCode : 082-90987876
     * indicator : D
     * flightNo : EU2232
     * flightInfoId : 11cd8a93b76b4827b5ab9d18b4730695
     * flightId : 11894585
     * mailType : AWBA
     * documentDelivery : 1
     * totalNumberPackages : 800
     * totalWeight : 1500.02
     * specialCargoCode : NPS
     * commodityName : 兰花,主办,外壳,上衣
     * consignee : 王麻子
     * consigneePhone : 13690809889
     * consigneeCarid : 5104333
     * forwarderId : 1241231231221
     * forwarderName : 四川泰顺物流有限公司
     * forwarderPhone : 15909887611
     * priority : 0
     * buckleGoods : 0
     * transit : 0
     * refrigerated : 0
     * devanning : 0
     * originatingStation : NKG
     * originatingStationCn : 南京
     * destinationStation : CTU
     * destinationStationCn : 程度
     * manifestTotal : 800
     * manifestWeight : 1500.2
     * warehouseArea : null
     * warehouseLocation : null
     * putStorageTime : null
     * putStorageUser : null
     * outStorageTime : null
     * outStorageUser : null
     * tallyingTotal : null
     * tallyingWeight : null
     * waybillStatus : 3
     * waybillCodeScanUrl :
     * commodityScanUrl :
     * waybillScanUrl :
     * createDate : 1551175629446
     * createUser : big weiwei
     * updateDate : 1551322223058
     * updateUser : dazhangwei333
     * errors : null
     */

    private String id;
    private String waybillCode;
    private String indicator;
    private String flightNo;
    private String flightInfoId;
    private int flightId;
    private String mailType;
    private int documentDelivery;
    private int totalNumber;
    private String totalWeight;
    private String specialCargoCode;
    private String commodityName;
    private String consignee;
    private String consigneePhone;
    private String consigneeCarid;
    private String forwarderId;
    private String forwarderName;
    private String forwarderPhone;
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
    private String warehouseArea;
    private Object warehouseLocation;
    private Object putStorageTime;
    private Object putStorageUser;
    private long outStorageTime;
    private String outStorageUser;
    private Integer tallyingTotal;
    private String tallyingWeight;
    private int waybillStatus;//运单状态 必填 5 待提货 6 已经提货 必须填
    private String waybillCodeScanUrl;
    private String commodityScanUrl;
    private String waybillScanUrl;
    private long createDate;
    private String createUser;
    private long updateDate;
    private String updateUser;
    private Object errors;

    private String rqName;//库区 获取储存

    /**
     * 已提货件数
     */
    private Integer outboundNumber;
    /**
     * 待提货件数
     */
    private Integer storageNumber;

    /**
     * 预期费用
     */
    private int amountOfMoney;
    /**
     * 逾期费用
     */
    private double defermentCharge;
    /**
     * 超重费用
     */
    private double overweightCharge;
    /**
     * 超重数量
     */
    private int overWieghtCount;

    /**
     * 超重重量
     */
    private double overWieght;

    /**
     * 提货人姓名
     */
    private String pickUpGoodsUser;

    /**
     * 提货人电话
     *
     * @date 2019/5/9 11:02 AM
     */
    private String pickUpGoodsUserPhone;

    /**
     * 提货人身份证号码
     *
     * @date 2019/5/9 11:09 AM
     */
    private String pickUpGoodsUserCardId;

    /**
     * 运单存放的库区
     */
   private List<WaybillArea> smInventorySummaryList;
}
