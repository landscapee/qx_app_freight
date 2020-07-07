package qx.app.freight.qxappfreight.bean.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.CounterUbnormalGoods;
import qx.app.freight.qxappfreight.bean.InWaybill;
import qx.app.freight.qxappfreight.bean.RcInfoOverweight;

/**
 * Created by zzq On 2020/6/30 18:30 & Copyright (C), 青霄科技
 *
 * @文档说明:
 */
@Data
public class InWaybillRecordSubmitNewEntity {

    /**
     * flag : 0
     * flightInfoId : 6197223a4ca14a51a2e7600228d00275
     * flightId : 14267160
     * taskId : 75384358
     * userId : u165cdb5251c94ef088b5dc5a27bc76d6
     * userName : 彭维
     * flightNo : GY7183
     * charterReWeight :
     * cargos : {"贵阳":[{"delFlag":0,"cacheId":"45615aa9-db50-4dfb-aef3-0c5e2ad08ab4","waybillCode":"661-46081512","waybillCodeFirst":"661","waybillCodeSecond":"46081512","tallyingTotal":"2","tallyingWeight":"2","warehouseArea":"5b7e8ff9558a3c16644c4f7aa5122a5b","warehouseAreaDisplay":"平板区","warehouseAreaType":"CTU_GARGO_STORAGE_TYPE_001","warehouseLocation":null,"remark":"","transit":"","counterUbnormalGoodsList":[],"mailType":"","allArrivedFlag":0,"stray":0,"surplusNumber":"","surplusWeight":"","strayDisabled":false,"originatingStationCn":"贵阳"}]}
     */
    private int flag;
    private String flightInfoId;
    private String flightId;
    private String taskId;
    private String userId;
    private String userName;
    private String flightNo;
    private String charterReWeight;
    private HashMap<String, List<SingleLineBean>> cargos = new HashMap<>();

    @Data
    public static class SingleLineBean implements Serializable {
        /**
         * id : c6a9f46b23e3d05b0594e1aee60f20c2
         * waybillCode : 898-31534871
         * indicator : D
         * flightNo : CZ3473
         * flightInfoId : 348e4ae5080742ba97516438576cc938
         * flightId : 14297784
         * mailType : C
         * documentDelivery : null
         * totalNumber : 12
         * totalWeight : 12
         * specialCode : AVI
         * commodityName : 苹果
         * consignee :
         * consigneePhone : null
         * consigneeCarid : null
         * freightId : null
         * freightCode : null
         * freightName : null
         * freightPhone : null
         * priority : null
         * buckleGoods : 0
         * transit : 0
         * refrigerated : 0
         * precious : 0
         * living : 0
         * quick : 0
         * devanning : 0
         * originatingStation : null
         * originatingStationCn : 郑州
         * destinationStation : null
         * destinationStationCn : null
         * route : CAN-CTU
         * manifestTotal : 0
         * allManifestTotal : null
         * manifestWeight : 0
         * allManifestWeight : null
         * warehouseArea : null
         * warehouseAreaType : null
         * warehouseLocation : null
         * putStorageTime : null
         * putStorageUser : null
         * outStorageTime : null
         * outStorageUser : null
         * tallyingTotal : 6
         * allTallyingTotal : null
         * tallyingWeight : 6
         * allTallyingWeight : null
         * waybillStatus : null
         * waybillCodeScanUrl : null
         * commodityScanUrl : null
         * waybillScanUrl : null
         * createTime : 1594001602977
         * createUser : u7b4c0e35127648e29ef3aaed649361cb
         * createUserName : null
         * updateTime : null
         * updateUser : null
         * returnOutTransportFlag : 0
         * counterUbnormalGoodsList : []
         * waybills : []
         * amountOfMoney : 0
         * delFlag : 0
         * paWaybillId : cc5d92a6b7fdb41263f59b5167e63d1a
         * masterWaybill : null
         * checkTime : null
         * checkUser : null
         * flightAta : null
         * flightSta : null
         * noticeRound : 0
         * forwarderPayType : 0
         * registerFlag : 0
         * noticeFlag : null
         * noticeStatus : null
         * storageDuration : null
         * remark :
         * checkEndDate : null
         * dataSource : null
         * overWeightList : []
         * targetReservoir : null
         * transship : 0
         * transshipDestination : null
         * warehouseAreaDisplay :
         * allArrivedFlag : 0
         * sortingDoCheck : 0
         * errorFlag : 0
         * cargoAvgWeight : 0
         * stray : 0
         * chargeableWeight : null
         * aliveInform : null
         * noAliveInform : null
         * historyCargoInf : []
         * surplusNumber : null
         * surplusWeight : null
         */
        private String id;//进港运单记录ID
        private String waybillCode;//运单号 航空公司运单单号
        private String indicator;//D – Domestic/国内 I – International/国际 M – Mixed/混合 R – Regional/地区
        private String flightNo;//航班号
        private String flightInfoId;//航班计划记录ID
        private int flightId;//航班计划优利系统ID
        private String mailType;//货邮类型,C货物，M邮件
        private Integer documentDelivery;//文件送达状态 0 未送达 1 已送达
        private int totalNumber;//运单总件数
        private double totalWeight;//运单总重量
        private String specialCode;//特货代码
        private String commodityName;//运单品名信息
        private String consignee;//收货人名称
        private String consigneePhone;//收货人电话
        private String consigneeCarid;//收货人身份证号码
        private Integer priority;//处理优先级 0 普通 1 优先处理
        private Integer buckleGoods;//是否扣货 0 否 1 是
        private Integer transit;//是否转关 0 否 1 是
        private Integer refrigerated;//冷藏 0 否 1 是
        private Integer precious;//贵重 0 否 1 是
        private Integer living;//活体 0 否 1 是
        private Integer quick;//快捷 0 否 1 是
        private Integer devanning;//拆箱板 0 否 1 是
        private String originatingStation;//出发站
        private String originatingStationCn;//出发站中文名称
        private String destinationStation;//到达站
        private String destinationStationCn;//到达站中文名称
        private String route;//航段
        private Integer manifestTotal;//舱单件数
        private double manifestWeight;//舱单重量
        private String warehouseArea;//库区
        private String warehouseAreaType;//库区类型
        private String warehouseLocation;//库位
        private Long putStorageTime;//入库时间
        private String putStorageUser;//入库操作人
        private Long outStorageTime;//出库时间
        private String outStorageUser;//出库人
        private int tallyingTotal;//理货件数
        private double tallyingWeight;//理货重量
        private String waybillStatus;//运单状态
        private String waybillCodeScanUrl;//运单号扫描图图片URL
        private String commodityScanUrl;//品名扫描图片URL
        private String waybillScanUrl;//运单扫描图片URL
        private Long createTime;//运单录入时间
        private String createUser;//运单录入人
        private String updateTime;//修改时间
        private String updateUser;//修改人
        private Integer returnOutTransportFlag;//转运类型: 0未转运, 1国内转国内(defuatl: 0)
        private List<CounterUbnormalGoods> counterUbnormalGoodsList;//异常类型 1.件数异常; 2.有单无货; 3.破损; 4.腐烂; 5.有货无单; 6.泄露; 7.错运; 8.扣货; 9.无标签; 10.对方未发报文;  11.其他; 12:死亡
        private List<InWaybill> waybills;//关系不明的关联运单
        private double amountOfMoney;//交款金额
        private int delFlag;// 0未删除 / 1删除
        private String paWaybillId;//父运单id(录单运单Id)
        private String remark;//备注
        private List<RcInfoOverweight> overWeightList;//超重数据
        private String warehouseAreaDisplay;//库区字段 用来显示
        private int allArrivedFlag;//货物全部到齐标识: 0.未全部到齐; 1.全部到齐;
        private String createUserName;//登录人姓名
        private int stray;//0非错运，1错运
        private boolean canModify;//删除运单数据时的本地判断值，默认为数据库中已录入的运单，不可删除，新增运单时设置为true可删除




        private String waybillCodeFirst;
        private String waybillCodeSecond;
        private int surplusNumber;
        private double surplusWeight;
        private boolean strayDisabled;
        private String freightId;
        private String freightCode;
        private String freightName;
        private String freightPhone;
        private String allManifestTotal;
        private double allManifestWeight;
        private String allTallyingTotal;
        private double allTallyingWeight;
        private String masterWaybill;
        private String checkTime;
        private String checkUser;
        private String flightAta;
        private String flightSta;
        private String noticeRound;
        private int forwarderPayType;
        private int registerFlag;
        private String noticeFlag;
        private String noticeStatus;
        private String storageDuration;
        private String checkEndDate;
        private String dataSource;
        private String targetReservoir;
        private int transship;
        private String transshipDestination;
        private int sortingDoCheck;
        private int errorFlag;
        private double cargoAvgWeight;
        private String chargeableWeight;
        private String aliveInform;
        private String noAliveInform;
        private List<String> historyCargoInf;
    }
}
