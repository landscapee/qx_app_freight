package qx.app.freight.qxappfreight.bean.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetIOManifestEntity {
    private String outletId;//"营业点Code码",
    private String storageType;//存储类型
    private String type;//""I入库 O出库",
    private String status;//"0待执行 1已执行",
    private String waybillCode;//运单号
    private String areaId;//库区id

}
