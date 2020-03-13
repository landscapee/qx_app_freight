package qx.app.freight.qxappfreight.bean.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 库存实体
 */
@Getter
@Setter
public class SmInventorySummary {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 营业点
     */
    private String outletCode;

    /**
     * 库区id
     */
    private String areaId;

    /**
     * 库位id
     */
    private String binId;

    /**
     * 运单号
     */
    private String waybillCode;

    /**
     * 件数
     */
    private Integer number;

    /**
     * 重量
     */
    private Double weight;

    /**
     * 存储类型
     */
    private String storageType;

    /**
     * 特货代码
     */
    private String specialCode;

    /**
     * 品名
     */
    private String cargoCn;

    /**
     * 货邮类型  C代表货物,M代表邮件
     */
    private String mailType;

    /**
     * 库区类型编码
     */
    private String areaTypeCode;

    /**
     * 入库关联id
     */
    private String mainId;

    /**
     * 库区名称
     */
    private String areaName;

    /**
     *  入库时间
     */
    private long execTime;

}
