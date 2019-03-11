package qx.app.freight.qxappfreight.bean.response;

import lombok.Data;

@Data
public class LoadAndUnloadTodoBean {

    /**
     * id : 57c08733d2345b608ece4c51b5f8d355
     * workerId : 126c5d1fa6ae40b6ba091caba26259d3
     * workerName : 杨明贵-监装员
     * deptId : FreightLoad
     * flightId : 11922973
     * flightNo : null
     * movement : 4
     * taskId : 3995d84bed124543b065b5b72e9a21c1
     * taskType : 2
     * createTime : 1551940690490
     * acceptTime : null
     * arrivalTime : null
     * openDoorTime : null
     * loadTime : null
     * unloadTime : null
     * closeDoorTime : null
     * aircraftno : B5147
     * seat : 136
     * scheduleTime : 1551786600000
     * route :"["SWA","CTU"]"
     */

    private String id;
    private String workerId;
    private String workerName;
    private String deptId;
    private String flightId;
    private String flightNo;
    private int movement;
    private String taskId;
    private int taskType;
    private long createTime;
    private long acceptTime;
    private long arrivalTime;
    private long openDoorTime;
    private long loadTime;
    private long unloadTime;
    private long closeDoorTime;
    private long actualTakeoffTime;
    private long actualArriveTime;
    private String aircraftno;
    private String seat;
    private long scheduleTime;
    private String route;
}
