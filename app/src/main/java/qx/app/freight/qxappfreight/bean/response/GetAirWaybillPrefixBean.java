package qx.app.freight.qxappfreight.bean.response;


import lombok.Data;

@Data
public class GetAirWaybillPrefixBean {
    /**
     * id : 590a534ced2d47e58f772e7145d392a6
     * awbPrefix : 784
     * iataCode : CSN
     * icaoCode : CZ
     * airline : China Southern Airlines
     */
    private String id;
    private String awbPrefix;
    private String iataCode;
    private String icaoCode;
    private String airline;
}
