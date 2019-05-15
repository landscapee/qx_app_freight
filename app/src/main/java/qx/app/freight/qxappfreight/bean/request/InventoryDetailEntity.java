package qx.app.freight.qxappfreight.bean.request;

import java.util.List;

import lombok.Data;

@Data
public class InventoryDetailEntity {

    private String id;

    /**
     * 运单Id
     */
    private String waybillId;


    /**
     * 清库任务Id
     */
    private String inventoryTaskId;

    /**
     * 运单号
     */
    private String waybillCode;

    /**
     * 分拣件数
     */
    private int tallyingTotal;

    /**
     * 发货件数
     */
    private int outboundNumber;

    /**
     * 清库件数
     */
    private int inventoryNumber;

    /**
     * 处置时间
     */
    private long dealTime;

    /**
     * 处置人
     */
    private String handler;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 异常详情
     */
    private List<InventoryUbnormalGoods> inventoryUbnormalGoods;

}
