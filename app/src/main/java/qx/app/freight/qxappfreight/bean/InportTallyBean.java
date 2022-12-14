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

    /**
     * 运单号 航空公司运单单号
     */
    private String waybillCode;

    /**
     * D – Domestic/国内 I – International/国际 M – Mixed/混合 R – Regional/地区
     */
    private String indicator;

    /**
     * 航班号
     */
    private String flightNo;

    /**
     * 航班计划记录ID
     */
    private String flightInfoId;

    /**
     * 航班计划优利系统ID
     */
    private Long flightId;

    /**
     * 货邮类型   AWBA 代表货物,AWBM代表邮件
     */
    private String mailType;
/**
     * 文件送达状态 0 未送达 1 已送达
     */
    private int documentDelivery;
 /**
     * 运单总件数
     */
    private int totalNumber;

    /**
     * 运单总重量
     */
    private double totalWeight;
/**
     * 特货代码
     */
    private String specialCargoCode;

    /**
     * 运单品名信息
     */
    private String commodityName;

    /**
     * 收货人名称
     */
    private String consignee;

    /**
     * 收货人电话
     */
    private String consigneePhone;

    /**
     * 收货人身份证号码
     */
    private String consigneeCarid;

    /**
     * 代理人(货代公司)ID
     */
    private String forwarderId;

    /**
     * 代理人(货代公司) 代码
     */
    private String forwarderCode;

    /**
     * 代理人(货代公司)名称
     */
    private String forwarderName;

    /**
     * 代理人(货代公司)电话
     */
    private String forwarderPhone;

    /**
     * 处理优先级 0 普通 1 优先处理
     */
    private Integer priority;

    /**
     * 是否扣货 0 否 1 是
     */
    private Integer buckleGoods;

    /**
     * 是否转关 0 否 1 是
     */
    private Integer transit;

    /**
     * 冷藏 0 否 1 是
     */
    private Integer refrigerated;

    /**
     * 贵重 0 否 1 是
     */
    private Integer precious;

    /**
     * 活体 0 否 1 是
     */
    private Integer living;

    /**
     * 快捷 0 否 1 是
     */
    private Integer quick;

    /**
     * 拆箱板 0 否 1 是
     */
    private Integer devanning;

    /**
     * 出发站
     */
    private String originatingStation;

    /**
     * 出发站中文名称
     */
    private String originatingStationCn;

    /**
     * 到达站
     */
    private String destinationStation;

    /**
     * 到达站中文名称
     */
    private String destinationStationCn;

    /**
    * 航段
    */
    private String route;

    /**
     * 舱单件数
     */
    private Integer manifestTotal;

    /**
     * 舱单重量
     */
    private double manifestWeight;

    /**
     * 库区
     */
    private String warehouseArea;

    /**
     * 库位
     */
    private String warehouseLocation;

    /**
     * 入库时间
     */
    private long putStorageTime;

    /**
     * 入库操作人
     */
    private String putStorageUser;

    /**
     * 出库时间
     */
    private long outStorageTime;

    /**
     * 出库人
     */
    private String outStorageUser;

    /**
     * 理货件数
     */
    private Integer tallyingTotal;

    /**
     * 理货重量
     */
    private double tallyingWeight;

    /**
     * 运单状态
     */
    private Integer waybillStatus;

    /**
     * 运单号扫描图图片URL
     */
    private String waybillCodeScanUrl;

    /**
     * 品名扫描图片URL
     */
    private String commodityScanUrl;

    /**
     * 运单扫描图片URL
     */
    private String waybillScanUrl;
 /**
     * 运单录入时间
     */
    private long createDate;
 /**
     * 运单录入人
     */
    private String createUser;
    private long updateDate;
 /**
     * 修改人
     */
    private String updateUser;

    /**
     * 转运类型: 0为转运, 1国内转国内(defuatl: 0)
     */
    private Integer returnOutTransportFlag;

    /**
     * 异常数组
     */
    private List<Integer> ubnormalType;

    /**
     * 异常数量
     */
    private Integer ubnormalNum;

    /**
     * 通知次数
     */
    private Integer noticeNum;

    /**
     * 交款金额
     */
    private double amountOfMoney;
}
