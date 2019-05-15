package qx.app.freight.qxappfreight.bean.request;


import java.util.List;

import lombok.Data;

@Data
public class InventoryUbnormalGoods {

    private String id;

    /**
     * 清库详情id
     */
    private String inventoryDetailId;

    /**
     * 运单号
     */
    private String waybillCode;

    /**
     *  异常来源
     */
    private String ubnormalSource;

    /**
     * 异常类型
     */
    private int ubnormalType;

    /**
     * 上报人
     */
    private String createUser;

    /**
     * 上报时间
     */
    private long createTime;

    private List<String> uploadFilePath;


}
