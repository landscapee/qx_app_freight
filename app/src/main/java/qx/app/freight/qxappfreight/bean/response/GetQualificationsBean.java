package qx.app.freight.qxappfreight.bean.response;

import lombok.Data;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
public class GetQualificationsBean {
    /**
     * id : 00001
     * freightCode : endd1662dc899f48e38c8b707fe76f8d49
     * freightName : 顺丰
     * riskFlag : 1
     * preciousFlag : 1
     * aliveFlag : 1
     */
    private String id;
    private String freightCode;
    private String freightName;
    private int riskFlag;
    private int preciousFlag;
    private int aliveFlag;
}
