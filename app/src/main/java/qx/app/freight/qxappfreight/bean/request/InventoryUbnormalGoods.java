package qx.app.freight.qxappfreight.bean.request;


import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class InventoryUbnormalGoods implements Serializable {

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

    /**
     * 异常件数
     */
    private int ubnormalNumber;

    private List<String> uploadPath;


}
