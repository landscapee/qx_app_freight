package qx.app.freight.qxappfreight.bean;

import java.util.List;

import lombok.Data;
@Data
public class LoadUnLoadTaskPushBean {

    /**
     * workerId : u883d88d9ff5144538829514bf1ff8d4f
     * taskData : {"seat":"126","flightNo":"MU5402","flightId":12563296,"staffList":[{"staffName":"王九","operationType":1,"staffId":"ua6e45460f36b4ed689f86dfd32ed23bb"},{"staffName":"王一1","operationType":2,"staffId":"ub2f4830035c8455fb63e04e5cd3094eb"}],"taskId":"c14afa21-80f9-4f2d-aec1-fecd4c9a"}
     * taskId : c14afa21-80f9-4f2d-aec1-fecd4c9a
     * stevedoresStaffChange : true
     */

    private String workerId;
    private TaskDataBean taskData;
    private String taskId;
    private boolean stevedoresStaffChange;
@Data
    public static class TaskDataBean {
        /**
         * seat : 126
         * flightNo : MU5402
         * flightId : 12563296
         * staffList : [{"staffName":"王九","operationType":1,"staffId":"ua6e45460f36b4ed689f86dfd32ed23bb"},{"staffName":"王一1","operationType":2,"staffId":"ub2f4830035c8455fb63e04e5cd3094eb"}]
         * taskId : c14afa21-80f9-4f2d-aec1-fecd4c9a
         */

        private String seat;
        private String flightNo;
        private int flightId;
        private String taskId;
        private List<StaffListBean> staffList;
@Data
        public static class StaffListBean {
            /**
             * staffName : 王九
             * operationType : 1
             * staffId : ua6e45460f36b4ed689f86dfd32ed23bb
             */

            private String staffName;
            private int operationType;//1新增，2删除
            private String staffId;
        }
    }
}
