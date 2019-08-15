package qx.app.freight.qxappfreight.bean.response;


import lombok.Data;

@Data
public class CargoReportHisBean {

    private String id;

    private String flightNo;

    private String flightId;

    private String reportOperator;

    private long createTime;

    private String flightIndicator;

    private String dataJson;

    /**
     * 上报类型,1行李上报,2国内货物上报,3国际货物上报
     */
    private int subType;

}
