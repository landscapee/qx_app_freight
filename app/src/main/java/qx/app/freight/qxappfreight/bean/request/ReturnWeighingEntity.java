package qx.app.freight.qxappfreight.bean.request;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;

@Data
public class ReturnWeighingEntity {

    private String userId;
    private String flightId;

    private GetInfosByFlightIdBean scooter;
}
