package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

@Data
public class FlightOfScooterBean {
    private String flightNo;
    private String planePlace;
    private String planeType;
    private long etd;
    private String flightRoute;
    private List<TransportTodoListBean> mTransportTodoListBeans;
    private boolean select;
}
