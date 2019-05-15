package qx.app.freight.qxappfreight.bean.request;


import lombok.Data;

@Data
public class InventoryUbnormalGoods {

    private String id;

    private String inventoryDetailId;

    private String waybillCode;

    private String ubnormalSource;

    private Integer ubnormalType;

    private String createUser;

    private long createTime;


}
