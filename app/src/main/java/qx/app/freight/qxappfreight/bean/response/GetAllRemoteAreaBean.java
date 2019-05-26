package qx.app.freight.qxappfreight.bean.response;

import lombok.Data;

@Data
public class GetAllRemoteAreaBean {

    private String areaId;
    private String lat;
    private String lon;
    private int areaType;
    private Object isAirport;
    private Object isCloserSeat;
}
