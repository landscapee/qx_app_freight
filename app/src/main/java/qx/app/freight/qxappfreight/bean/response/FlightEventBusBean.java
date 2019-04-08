package qx.app.freight.qxappfreight.bean.response;

import lombok.Data;

@Data
public class FlightEventBusBean {

    public FlightEventBusBean(String flight) {
        this.flight = flight;
    }

    public String flight;
}
