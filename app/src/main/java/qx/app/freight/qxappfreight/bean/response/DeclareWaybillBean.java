package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

public class DeclareWaybillBean {


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
    private int coldStorage;

    /**
     * 总件数
     */
    private int totalNumberPackages;

    /**
     * 重量
     */
    private int billingWeight;

    /**
     * 总重量
     */
    private int totalWeight;

    /**
     * 总体积
     */
    private int totalVolume;

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
     * 运单状态:0:待退货,-1:已退货,-2:系统数据异常,-3收验失败,1:未申报,2:已申报,3:已收验,4:已安检,5:已收货,6:待收费,7:已收费,8,已预配,9:已组板,10:已报载,11:已复重,12:已串板,13:已起飞,14:已拉货
     */
    private int status;

    private String createUser;

    private long createTime;

    private long lastUpdateTime;

    private String lastUpdateOperator;

    private int delFlag;

    /**
     * 收费标记: 0:未收费,1:已收费
     */
    private int chargeFlag;

    /**
     * 是否为虚拟运单,1代表是,默认为0
     */
    private int virtualFlag;

    /**
     * 是否为其他运单,1代表是,默认是0
     */
    private int otherFlag;

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
     * 收运总件数
     */
    private int receivecargoNumber;

    /**
     * 收运总重量
     */
    private int receivecargoWeight;

    /**
     * 收运总体积
     */
    private int receivecargoVolumn;

    /**
     * 入库时间
     */
    private int storageTime;

    /**
     * 是否大件 0：否 1： 是
     */
    private int bigFlag;

    /**
     * 更换次数
     */
    private int exchangeNumber;

    /**
     * 中转站
     */
    private String transferStation;
    /**
     * 转运类型: 0未转运, 1国内转国内(defuatl: 0)
     */
    private int returnOutTransportFlag;

    /**
     * 0:正常1:部分拉货,2:全部拉货
     */
    private int exceptionFlag;

    private List<DeclareItem> declareItems;

    private List<DeclareItem> declareItem;

    private String freightName;

    private String flightName;

    /**
     * 新运单号
     */
    private String newDeclareWaybillCode;

//    private DeclareWaybillAddition declareWaybillAddition;

    /**
     * 板车号
     */
    private String scooterId;

    /**
     * 抽检标志
     */
    private Integer spotFlag;

    /**
     * 中转站中文
     */
    private String transferStationTs;

    /**
     * 中转站中文
     */
    private String transferStationCn;

    /**
     * 目的站中文
     */
    private String destinationStationCn;

    /**
     * 备注
     */
    private String remark;

    /**
     * 0申报 1收运
     */
    private Integer updateType;

    private long startUpdateTime;
    private long endUpdateTime;
}