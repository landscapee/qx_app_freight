package qx.app.freight.qxappfreight.bean.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickGoodsRecordsBean {

    /**
     * id : da2a92c11a2b2502cee64872f6eef69f
     * waybillId : ce6041173ea55ae6ed7d70f0aaf42c3e
     * outboundNumber : 2
     * forkliftTruckNumber : 0
     * createTime : 1582254749000
     * createUser : u57a67b4f9e184757840618927abd40ab
     * revokeFlag : 0
     */

    private String id;
    private String waybillId;
    private int outboundNumber;
    private double outboundWeight;
    private int forkliftTruckNumber;
    private long createTime;
    private String createUser;
    private int revokeFlag; //0 未撤销 1 已撤销
    private String areaId;
    private String outboundBusinessId;



}
