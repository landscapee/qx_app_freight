package qx.app.freight.qxappfreight.bean;

import java.util.List;

public class TestBean {


    /**
     * aircraftNo : B304X
     * deptId : FreightLoad
     * flightId : 11926473
     * flightNo : GX8965
     * members : {"workerId":"851b9d649bd541989943db577edcfcea","workerName":"张兵-监装员"}
     * movement : 4
     * relatedId : 11926470
     * routes : ["NNG","CTU"]
     * scheduleTime : 1551963000000
     * seat : 137
     * taskId : c8a970ff13824c07b78346b52b424d03
     */

    private String aircraftNo;
    private String deptId;
    private int flightId;
    private String flightNo;
    private MembersBean members;
    private int movement;
    private int relatedId;
    private long scheduleTime;
    private String seat;
    private String taskId;
    private List <String> routes;

    public String getAircraftNo() {
        return aircraftNo;
    }

    public void setAircraftNo(String aircraftNo) {
        this.aircraftNo = aircraftNo;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public MembersBean getMembers() {
        return members;
    }

    public void setMembers(MembersBean members) {
        this.members = members;
    }

    public int getMovement() {
        return movement;
    }

    public void setMovement(int movement) {
        this.movement = movement;
    }

    public int getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(int relatedId) {
        this.relatedId = relatedId;
    }

    public long getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(long scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List <String> getRoutes() {
        return routes;
    }

    public void setRoutes(List <String> routes) {
        this.routes = routes;
    }

    public static class MembersBean {
        /**
         * workerId : 851b9d649bd541989943db577edcfcea
         * workerName : 张兵-监装员
         */

        private String workerId;
        private String workerName;

        public String getWorkerId() {
            return workerId;
        }

        public void setWorkerId(String workerId) {
            this.workerId = workerId;
        }

        public String getWorkerName() {
            return workerName;
        }

        public void setWorkerName(String workerName) {
            this.workerName = workerName;
        }
    }
}
