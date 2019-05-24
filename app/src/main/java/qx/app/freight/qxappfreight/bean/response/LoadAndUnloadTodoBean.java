package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class LoadAndUnloadTodoBean implements Serializable {

    /**
     * id : 52551c6ea57a3e7aebb4041637df6e85
     * workerId : a182c026541342b5b38404b3a6852c9b
     * workerName : 马木全-监装员
     * deptId : FreightLoad
     * flightId : 12248758
     * flightNo : 9C8758
     * movement : 4
     * taskId : 9ef564ec-1987-4bc8-b830-6fa7b9b7
     * taskType : 2
     * createTime : 1558687832909
     * acceptTime : null
     * arrivalTime : null
     * openDoorTime : null
     * startLoadTime : null
     * endLoadTime : null
     * startUnloadTime : null
     * endUnloadTime : null
     * closeDoorTime : null
     * aircraftno : B6820
     * seat : 102
     * scheduleTime : 1558695900000
     * route : ["HKT","CTU"]
     * actualTakeoffTime : null
     * actualArriveTime : 1558688629465
     * cancelFlag : false
     * cancelTime : null
     * beginLoadUnloadTime : null
     * operationStepObj : [{"operationCode":"FreightLoadReceived","operationName":"领受"},{"operationCode":"FreightLoadArrived","operationName":"到位"},{"operationCode":"FreightLoadStart","operationName":"开启舱门"},{"operationCode":"FreightUnloadEnd","operationName":"关闭舱门"},{"operationCode":"FreightLoadBegin","operationName":"开始装机"},{"operationCode":"FreightLoadFinish","operationName":"装机结束"},{"operationCode":"FreightUnloadBegin","operationName":"开始卸机"},{"operationCode":"FreightUnloadFinish","operationName":"卸机结束"}]
     * relateInfoObj : {"id":null,"workerId":null,"workerName":null,"deptId":"FreightLoad","flightId":"12250194","flightNo":"9C8986","movement":4,"taskId":"9ef564ec-1987-4bc8-b830-6fa7b9b7","taskType":5,"createTime":null,"acceptTime":null,"arrivalTime":null,"openDoorTime":null,"startLoadTime":null,"endLoadTime":null,"startUnloadTime":null,"endUnloadTime":null,"closeDoorTime":null,"aircraftno":"B6820","seat":"102","scheduleTime":1558701600000,"route":null,"actualTakeoffTime":null,"actualArriveTime":null,"cancelFlag":false,"cancelTime":null,"beginLoadUnloadTime":null,"operationStep":null,"relateInfo":null,"operationStepObj":null,"relateInfoObj":null}
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
    private long startLoadTime;
    private long endLoadTime;
    private long startUnloadTime;
    private long endUnloadTime;
    private long closeDoorTime;
    private String aircraftno;
    private String seat;
    private long scheduleTime;
    private String route;
    private Object actualTakeoffTime;
    private long actualArriveTime;
    private boolean cancelFlag;
    private Object cancelTime;
    private Object beginLoadUnloadTime;
    private RelateInfoObjBean relateInfoObj;
    private List<OperationStepObjBean> operationStepObj;

    @Data
    public static class RelateInfoObjBean implements Serializable{
        /**
         * id : null
         * workerId : null
         * workerName : null
         * deptId : FreightLoad
         * flightId : 12250194
         * flightNo : 9C8986
         * movement : 4
         * taskId : 9ef564ec-1987-4bc8-b830-6fa7b9b7
         * taskType : 5
         * createTime : null
         * acceptTime : null
         * arrivalTime : null
         * openDoorTime : null
         * startLoadTime : null
         * endLoadTime : null
         * startUnloadTime : null
         * endUnloadTime : null
         * closeDoorTime : null
         * aircraftno : B6820
         * seat : 102
         * scheduleTime : 1558701600000
         * route : null
         * actualTakeoffTime : null
         * actualArriveTime : null
         * cancelFlag : false
         * cancelTime : null
         * beginLoadUnloadTime : null
         * operationStep : null
         * relateInfo : null
         * operationStepObj : null
         * relateInfoObj : null
         */

        private Object id;
        private Object workerId;
        private Object workerName;
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
        private long startLoadTime;
        private long endLoadTime;
        private long startUnloadTime;
        private long endUnloadTime;
        private long closeDoorTime;
        private String aircraftno;
        private String seat;
        private long scheduleTime;
        private String route;
        private Object actualTakeoffTime;
        private long actualArriveTime;
        private boolean cancelFlag;
        private Object cancelTime;
        private Object beginLoadUnloadTime;
        private Object operationStep;
        private Object relateInfo;
        private Object operationStepObj;
        private Object relateInfoObj;
    }

    @Data
    public static class OperationStepObjBean implements Serializable{
        /**
         * operationCode : FreightLoadReceived
         * operationName : 领受
         */

        private String operationCode;
        private String operationName;
    }
}
