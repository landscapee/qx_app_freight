package qx.app.freight.qxappfreight.bean.response;

import lombok.Data;

@Data
public class StepBean {


    /**
     * status : 200
     * message : 正确
     * rowCount : null
     * data : {"id":"d7119f8e2e76fc9647169d1c5dee7e35","userId":"uf528469abd934e60a947502ed753106d","deptId":null,"userName":"周仁龙","terminalId":"99000750347348","flightTaskId":"YY20190328150302-1692","loadUnloadDataId":"ae867a9d5a197b40b8785383f22f3acd","operationCode":"CargoOutTransportStart","flightId":12006512,"longitude":"0.0","latitude":"0.0","createTime":1553761827534,"type":0}
     */

    private String status;
    private String message;
    private Object rowCount;
    private DataBean data;

    @Data
    public static class DataBean {
        /**
         * id : d7119f8e2e76fc9647169d1c5dee7e35
         * userId : uf528469abd934e60a947502ed753106d
         * deptId : null
         * userName : 周仁龙
         * terminalId : 99000750347348
         * flightTaskId : YY20190328150302-1692
         * loadUnloadDataId : ae867a9d5a197b40b8785383f22f3acd
         * operationCode : CargoOutTransportStart
         * flightId : 12006512
         * longitude : 0.0
         * latitude : 0.0
         * createTime : 1553761827534
         * type : 0
         */

        private String id;
        private String userId;
        private Object deptId;
        private String userName;
        private String terminalId;
        private String flightTaskId;
        private String loadUnloadDataId;
        private String operationCode;
        private int flightId;
        private String longitude;
        private String latitude;
        private long createTime;
        private int type;


    }
}
