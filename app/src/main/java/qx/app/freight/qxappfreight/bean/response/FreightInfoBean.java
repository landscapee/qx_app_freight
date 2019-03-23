package qx.app.freight.qxappfreight.bean.response;

import lombok.Data;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
public class FreightInfoBean {

    /**
     * id :
     * freightName :
     * riskFlag :
     * preciousFlag :
     * aliveFlag :
     * freightCode :
     */
    private String id;
    private String iata;
    private String icao;
    private String fullname;
    private String memo;
    private int status;
    private int delFlag;
    private String require;
    private long createDate;
    private String createUser;
    private long updateDate;
    private String param;
}
