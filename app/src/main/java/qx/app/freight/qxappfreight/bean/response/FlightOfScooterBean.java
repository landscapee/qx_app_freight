package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

@Data
public class FlightOfScooterBean {
    private String flightNo;
    private String planePlace;//机位
    private String planeType;//飞机类型
    private long etd;
    private String flightRoute;//航线
    private int num;//板车数量
    private String carType;//板车类型
    private List<TransportTodoListBean> mTransportTodoListBeans;//板车集合
    private boolean select;//是否到机位
}
