package qx.app.freight.qxappfreight.bean.response;

import lombok.Data;

@Data
public class RcDeclareWaybill {

    private static final long serialVersionUID = 1L;

    /**
     * 系统运单ID 系统编号
     */
    private String id;

    /**
     * 运单号 航空公司提供
     */
    private String waybillCode;

    /**
     * 货邮类型  C代表货物,M代表邮件
     */
    private String mailType;

    /**
     * 航班号
     */
    private String flightNumber;

    /**
     * 航班日期
     */
    private long flightDate;

    /**
     * 始发站
     */
    private String originatingStation;

    /**
     * 目的站
     */
    private String destinationStation;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 电话
     */
    private String consigneePhone;

    /**
     * 邮编
     */
    private String consigneePostcode;

    /**
     * 地址
     */
    private String consigneeAddress;

    /**
     * 特货代码
     */
    private String specialCargoCode;

    /**
     * 货物类别  1：贵重  2：危险 3：活体 4：冷藏 0：普货
     */
    private Integer coldStorage;

    /**
     * 总件数
     */
    private Integer totalNumber;

    /**
     * 计费重量
     */
    private Integer billingWeight;

    /**
     * 总重量
     */
    private Integer totalWeight;

    /**
     * 冷藏温度
     */
    private String refrigeratedTemperature;

    /**
     * 货代公司ID
     */
    private String shipperCompanyId;

    /**
     * 航班ID
     */
    private String flightId;

    /**
     * 流程ID
     */
    private String activitiId;

    /**
     * 内转表日期
     */
    private long internalTransferDate;

    /**
     * 内转表进港航班
     */
    private String internalTransferFlight;

    /**
     * 内转表进港运单号
     */
    private String internalTransferWaybill;

    /**
     * 换单标记 1:换单运单,0:未换单运单
     */
    private Integer exchangeFlag;

    /**
     * 换单之前的运单号
     */
    private String exchangeBeforeCode;

    /**
     * (-1新换单运单 1:暂存 2 提交中 3 已提交 4 提交失败 5 申请换单 6 换单拒绝 7换单成功 8 退货中 9 退货完成,订单关闭 10 借货中 11 借货拒绝)
     * 运单状态:0:待退货,-1:已退货,-2:系统数据异常,-3收验失败,-4:安检失败,1:未申报,2:已申报,3:已收验,4:已安检,5:已收货,6:待收费,7:已收费,8,已预配,9:已组板,10:已报载,11:已复重,12:已串板,13:已起飞,14:已拉货
     */
    private Integer status;

    private String createUser;

    private long createTime;

    private long lastUpdateTime;

    private String lastUpdateOperator;

    private Integer delFlag;

    /**
     * 收费标记: 0:未收费,1:已收费
     */
    private Integer chargeFlag;

    /**
     * 是否为虚拟运单,1代表是,默认为0
     */
    private Integer virtualFlag;

    /**
     * 是否为其他运单,1代表是,默认是0
     */
    private Integer otherFlag;

    /**
     * 申请时间
     */
    private long applyTime;

    /**
     * 预计交货道口
     */
    private String expectedDeliveryGate;

    /**
     * 预计交货时间
     */
    private long expectedDeliveryTime;

    /**
     * 收运件数（收运回填）
     */
    private Integer receivecargoNumber;

    /**
     * 收运重量（收运回填）
     */
    private Integer receivecargoWeight;

    /**
     * 收运体积（收运回填）
     */
    private Integer receivecargoVolumn;

    /**
     * 是否大件 0：否 1： 是
     */
    private Integer bigFlag;

    /**
     * 更换次数
     */
    private Integer exchangeNumber;

    /**
     * 总体积
     */
    private Integer totalVolume;

    /**
     * 抽验标记 0抽验 1不抽验
     */
    private Integer spotFlag;

    /**
     * 入库时间（收运回填）
     */
    private long storageTime;

    /**
     * 0:正常1:部分拉货,2:全部拉货
     */
    private Integer exceptionFlag;

    /**
     * 中转站
     */
    private String transferStation;

    /**
     * 货代公司名
     */
    private String freightName;

    /**
     * 航空公司名
     */
    private String flightName;

    /**
     * 转运标识: 0无, 1国内转国内 (defualt: 0)
     */
    private Integer returnOutTransportFlag;

    /**
     * 中转站中文名
     */
    private String transferstationTs;

    private String transferStationTs;

    /**
     * 目的站中文
     */
    private String destinationStationCn;

    /**
     * 中转站中文
     */
    private String transferStationCn;

    /**
     * 备注
     */
    private String remark;

    /**
     * 运单目的站
     */
    private String waybillDestinationStation;

    /**
     * 0 申报 1收运
     */
    private Integer updateType;

    /**
     * 原始运单id
     */
    private String orgId;
}
