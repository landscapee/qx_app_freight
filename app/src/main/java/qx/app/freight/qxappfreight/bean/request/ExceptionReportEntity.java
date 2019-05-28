package qx.app.freight.qxappfreight.bean.request;

import java.util.List;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;

@Data
public class ExceptionReportEntity {
    private String loadingListId;
    private String id;

    private long flightId;//

    private String flightNum;//

    private String reportTime;

    private String exceptionDesc;

    private List<String> files;
    /**
     * 1装机异常上报,2卸机异常上报,3装机拉货上报
     * 4 偏离上报
     */
    private int reType;

    private String reOperator;//

    private String flightType;

    private String flightSeat;

    private int transportType;

    private String rpDate; //年月日

    private String rpInfo;

    private String seat;

    private String area;//结束区域id

    private String exceptionCode;//异常 操作code

    private String deptId;//部门id

    private List<TransportTodoListBean> scooters;//板车数据
    private List<TransportTodoListBean> waybillScooters;//运单数据

    private TransportEndEntity transportAppDto; //结束运输的  任务板车信息
}
