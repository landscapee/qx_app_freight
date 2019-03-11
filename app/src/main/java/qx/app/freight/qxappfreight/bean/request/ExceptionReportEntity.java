package qx.app.freight.qxappfreight.bean.request;

import java.util.List;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;

@Data
public class ExceptionReportEntity {
    private String id;

    private long flightId;//

    private String flightNum;//

    private String reportTime;

    private String exceptionDesc;

    private List<String> files;

    private int reType;//3拉货上报

    private String reOperator;//

    private String flightType;

    private String flightSeat;

    private int transportType;

    private String rpDate; //年月日

    private String rpInfo;

    private List<TransportTodoListBean> scooters;//板车数据
//    private List<TpExceptionScooter> scooters;
}
