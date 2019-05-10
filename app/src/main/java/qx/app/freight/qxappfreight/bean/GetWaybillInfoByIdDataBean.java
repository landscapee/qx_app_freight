package qx.app.freight.qxappfreight.bean;

import java.util.List;

import lombok.Data;

@Data
public class GetWaybillInfoByIdDataBean {

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
        private double totalNumberPackages;
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
        private Object cargoType;
        private Object waybillDestinationStationCn;
        private Object originatingStationCn;
        private Object updateWeights;
        private Object waybillAirportDestinationStationName;
        private List<DeclareItemsBean> declareItems;

        @Data
        public static class DeclareItemsBean {

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

//    /**
//     * 系统运单ID 系统编号 */
//    /**
//     * 运单号 航空公司提供 */
//    private String waybillCode;
//    /**
//     * 货邮类型 C代表货物,M代表邮件 */
//    private String mailType;
//    /**
//     * 航班号 */
//    private String flightNumber;
//    /**
//     * 航班⽇日期 */
//    private long flightDate;
//    /**
//     * 始发站 */
//    private String originatingStation; /**
//     * ⽬目的站 */
//    private String destinationStation;
//    /**
//     * 收货⼈人 */
//    private String consignee;
//    /**
//     * 电话 */
//    private String consigneePhone;
//    /**
//     * 邮编 */
//    private String consigneePostcode;
//    /**
//     * 地址 */
//    private String consigneeAddress;
//    /**
//     * 特货代码 */
//    private String specialCargoCode;
//    /**
//     * 货物类别 1:贵重 2:危险 3:活体 4:冷藏 0:普货 */
//    private Integer coldStorage;
//    /**
//     * 总件数 */
//    private int totalNumberPackages;
//    /**
//     * 重量量 */
//    private int billingWeight;
//    /**
//     * 总重量量 */
//    private int totalWeight;
//    /**
//     * 总体积 */
//    private int totalVolume;
//    /**
//     * 冷藏温度 */
//    private String refrigeratedTemperature;
//    /**
//     * 货代公司ID */
//    private String shipperCompanyId;
//    /**
//     * 航班ID */
//    private String flightId;
//    /**
//     * 流程ID */
//    private String activitiId;
//    /**
//     * 内转表⽇日期 */
//    private long internalTransferDate;
//    /**
//     * 内转表进港航班 */
//    private String internalTransferFlight;
//    /**
//     * 内转表进港运单号 */
//    private String internalTransferWaybill;
//    /**
//     * 换单标记 1:换单运单,0:未换单运单 */
//    private Integer exchangeFlag;
//    /**
//     * 换单之前的运单号 */
//    private String exchangeBeforeCode;
//
//    /**
//     * (-1新换单运单 1:暂存 2 提交中 3 已提交 4 提交失败 5 申请换单 6 换单拒绝 7换单成功 8 退货中 9 退货完成,订单关闭 10 借货中 11 借货拒绝)
//     运单状态:0:待退货,-1:已退货,-2:系统数据异常,-3收验失败,1:未申报,2:已申报,3:已收验,4:已安检,5:已收货,6:待收费,7:已收费,8,已预配,9:已组板,10:已报载,11:已复重,12:已串串板,13:已起⻜飞,14:已拉
//     货
//     补重运单状态:50:待收费, 51:已收费 */
//    private int status;
//
//    private String createUser;
//
//    private String lastUpdateOperator;
//
//    private int delFlag;
//    /**
//     * 收费标记: 0:未收费,1:已收费 */
//    private int chargeFlag;
//    /**
//     * 是否为虚拟运单,1代表是,默认为0 */
//    private int virtualFlag;
//    /**
//     * 是否为其他运单,1代表是,默认是0 */
//    private int otherFlag;
//    /**
//     * 申请时间 */
//    private long applyTime;
//    /**
//     * 预计交货道⼝口 */
//    private String expectedDeliveryGate;
//    /**
//     * 预计交货时间 */
//    private long expectedDeliveryTime;
//    /**
//     * 收运总件数 */
//    private int receivecargoNumber;
//    /**
//     * 收运总重量量 */
//    private int receivecargoWeight;
//    /**
//     * 收运总体积 */
//    private int receivecargoVolumn;
//    /**
//     * ⼊入库时间 */
//    private int storageTime;
//    /**
//     * 是否⼤大件 0:否 1: 是 */
//    private int bigFlag;
//    /**
//     * 更更换次数 */
//    private int exchangeNumber;
//    /**
//     * 中转站 */
//    private String transferStation; /**
//     * 转运类型: 0未转运, 1国内转国内(defuatl: 0) */
//    private int returnOutTransportFlag;
//    /**
//     * 0:正常1:部分拉货,2:全部拉货 */
//    private int exceptionFlag;
//
////    private List<DeclareItem> declareItems;
////
////    private List<DeclareItem> declareItem;
//
//    private String freightName;
//
//    private String flightName;
//    /**
//     * 新运单号 * */
//    private String newDeclareWaybillCode;
//
////    private DeclareWaybillAddition declareWaybillAddition;
//    /**
//     * 板⻋车号 */
//     private String scooterId;
//    /**
//     * 抽检标志 */
//    private Integer spotFlag;
//    /**
//     * 中转站中⽂文 */
//    private String transferStationTs;
//    /**
//     * 中转站中⽂文 */
//    private String transferStationCn;
//    /**
//     * ⽬目的站中⽂文 */
//    private String destinationStationCn;
//    /**
//     * 备注 */
//    private String remark;
//    /**
//     * 0申报 1收运 */
//    private Integer updateType;
//    /**
//     * 运单类型(default : 0) * 0申报, 1补重
//     */
//    private Integer waybillType;
//    /**
//     * ⽗父运单id */
//    private String paWaybillId;
//
//    private long startUpdateTime;
//
//    private long endUpdateTime;
//    /**
//     * 运单⽬目的站 */
//    private String waybillDestinationStation;
//    /**
//     * 品名 */
//    private String cargoCn;
//    /**
//     * 包装类型 * */
//    private String packagingType;
//    /**
//     * 品类 */
//    private String cargoType;
//    /**
//     * 运单⽬目的站中⽂文 */
//    private String waybillDestinationStationCn;
//    /** *始发站中⽂文 * */
//    private String originatingStationCn;
//    /**
//     * 报载修改收费重量量差值 */
//
////    private BigDecimal[] updateWeights;



}
