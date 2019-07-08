package qx.app.freight.qxappfreight.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 进港运单信息
 *

 * </p>
 *
 * @author Nacol
 * @since 2019-04-23
 *
 *  进港分拣 -- 分拣运单实体类
 *           guohao - 2019/4/25
 */
@Data
public class InWaybillRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 进港运单记录ID
     */
    private String id;

    /**
     * 运单号 航空公司运单单号
     *
     */
    private String waybillCode;

    /**
     * D – Domestic/国内 I – International/国际 M – Mixed/混合 R – Regional/地区
     *
     */
    private String indicator;

    /**
     * 航班号
     *
     */
    private String flightNo;

    /**
     * 航班计划记录ID
     *
     */
    private String flightInfoId;

    /**
     * 航班计划优利系统ID
     *
     */
    private Long flightId;

    /**
     * 货邮类型   AWBA 代表货物,AWBM代表邮件
     *
     */
    private String mailType;

    /**
     * 文件送达状态 0 未送达 1 已送达
     *
     */
    private Integer documentDelivery;

    /**
     * 运单总件数
     *
     */
    private Integer totalNumber;

    /**
     * 运单总重量
     *
     */
    private BigDecimal totalWeight;

    /**?
     * 特货代码
     */
    private String specialCargoCode;

    /**?
     * 运单品名信息
     */
    private String commodityName;

    /**?
     * 收货人名称
     */
    private String consignee;

    /**?
     * 收货人电话
     */
    private String consigneePhone;

    /**?
     * 收货人身份证号码
     */
    private String consigneeCarid;

    /**?
     * 代理人(货代公司)ID
     */
    private String forwarderId;

    /**?
     * 代理人(货代公司) 代码
     */
    private String forwarderCode;

    /**?
     * 代理人(货代公司)名称
     */
    private String forwarderName;

    /**?
     * 代理人(货代公司)电话
     */
    private String forwarderPhone;

    /**?
     * 处理优先级 0 普通 1 优先处理
     */
    private Integer priority;

    /**
     * 是否扣货 0 否 1 是
     *
     */
    private Integer buckleGoods;

    /**
     * 是否转关 0 否 1 是
     *
     */
    private Integer transit;

    /**
     * 冷藏 0 否 1 是
     *
     */
    private Integer refrigerated;

    /**
     * 贵重 0 否 1 是
     *
     */
    private Integer precious;

    /**
     * 活体 0 否 1 是
     *
     */
    private Integer living;

    /**
     * 快捷 0 否 1 是
     *
     */
    private Integer quick;

    /**
     * 拆箱板 0 否 1 是
     *
     */
    private Integer devanning;

    /**?
     * 出发站
     */
    private String originatingStation;

    /**?
     * 出发站中文名称
     */
    private String originatingStationCn;

    /**?
     * 到达站
     */
    private String destinationStation;

    /**?
     * 到达站中文名称
     */
    private String destinationStationCn;

    /**?
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
    private BigDecimal manifestWeight;

    /**
     * 库区
     */
    private String warehouseArea;

    /**
     * 库区类型
     */
    private String warehouseAreaType;

    /**
     * 库位
     */
    private String warehouseLocation;

    /**
     * 入库时间
     *
     */
    private Long putStorageTime;

    /**
     * 入库操作人
     *
     */
    private String putStorageUser;

    /**
     * 出库时间
     *
     */
    private Long outStorageTime;

    /**
     * 出库人
     *
     */
    private String outStorageUser;

    /**
     * 理货件数
     *
     */
    private Integer tallyingTotal;

    /**
     * 理货重量
     *
     */
    private BigDecimal tallyingWeight;

    /**
     * 运单状态
     *
     */
    private Integer waybillStatus;

    /**
     * 运单号扫描图图片URL
     *
     */
    private String waybillCodeScanUrl;

    /**
     * 品名扫描图片URL
     *
     */
    private String commodityScanUrl;

    /**
     * 运单扫描图片URL
     *
     */
    private String waybillScanUrl;

    /**
     * 运单录入时间
     *
     */
    private Long createTime;

    /**
     * 运单录入人
     *
     */
    private String createUser;

    private Long updateTime;

    /**
     * 修改人
     */
    private String updateUser;

    /**
     * 转运类型: 0未转运, 1国内转国内(defuatl: 0)
     */
    private Integer returnOutTransportFlag;

    /**
     * 异常数组
     * 异常类型 1.件数异常; 2.有单无货; 3.破损; 4.腐烂; 5.有货无单; 6.泄露; 7.错运; 8.扣货; 9.无标签; 10.对方未发报文;  11.其他; 12:死亡
     */
    private List<CounterUbnormalGoods> counterUbnormalGoodsList = new ArrayList<>();

    /**
     * 关系不明的关联运单
     */
    private List<InWaybill> waybills = new ArrayList<>();

    /**
     * 交款金额
     *
     */
    private BigDecimal amountOfMoney;

    /**
     * 0未删除 / 1删除
     */
    private Integer delFlag;

    /**
     * 父运单id(录单运单Id)
     */
    private String paWaybillId;

    /**
     * 备注
     */
    private String remark;

    /**
     *
     */
    private  List<RcInfoOverweight> overWeightList;

    /**
     * 库区字段 用来显示
     */
    private String warehouseAreaDisplay;
    /**
     * 货物全部到齐标识: 0.未全部到齐; 1.全部到齐;
     * (ps : 每批到货的货物, 非运单所有货物)
     */
    private int allArrivedFlag;

}
