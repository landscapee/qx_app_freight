package qx.app.freight.qxappfreight.bean.response;

import com.chad.library.adapter.base.entity.MultiItemEntity;

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
    private String flightType;
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
    /**
     * 进港卸机看到达，出港装机看离港
     */
    private long sta;//计划到达时间
    private long eta;//预计到达时间
    private long ata;//实际到达时间
    private long std;//计划离港时间
    private long etd;//预计离港时间
    private long atd;//实际离港时间
    private List<OperationStepObjBean> operationStepObj;
    private int widthAirFlag;//0是宽体机，1是窄体机
    /*
     * 行李重量
     */
    private double baggageWeight;
    /*
     * 货物重量
     */
    private double cargoWeight;
    /*
     * 邮件重量
     */
    private double mailWeight;
    /**
     * 添加的需要的数据
     */
    private boolean acceptTask = false;//是否已经领受任务
    private String timeForShow;//时间：实际到达（离港）时间  >  预计到达（离港）时间  >  计划到达（离港）时间
    private int timeType;//时间显示类型：3，实际时间；2，预计时间；1，计划时间
    private List<String> flightInfoList;//航线信息列表
    private List<String> stepCodeList;//航线信息列表
    private boolean showDetail;

    @Data
    public static class RelateInfoObjBean implements Serializable {
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
        /**
         * 进港卸机看到达，出港装机看离港
         */
        private long sta;//计划到达时间
        private long eta;//预计到达时间
        private long ata;//实际到达时间
        private long std;//计划离港时间
        private long etd;//预计离港时间
        private long atd;//实际离港时间
    }

    @Data
    public static class OperationStepObjBean implements Serializable, MultiItemEntity {
        /**
         * operationCode : FreightLoadReceived
         * operationName : 领受
         */

        private String operationCode;//操作code
        private String operationName;//操作名称
        private int itemType;        //步骤类型，未执行、即将执行、已执行
        private String stepDoneDate; //步骤时间
        private String flightType;   //航班类型，D，国内；I,国际；M，混合
        private String taskId;
    }
}
