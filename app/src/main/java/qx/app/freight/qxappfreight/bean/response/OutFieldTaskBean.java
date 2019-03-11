package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;

import lombok.Data;

@Data
public class OutFieldTaskBean implements Serializable {

        private String id;

        private String taskId;

        private String userId;

        private String flightId;

        private String flightNo;

        private Integer num;

        private String cargoType;

        private String beginAreaType;

        private String beginAreaId;

        private String endAreaType;

        private String endAreaId;

        private long planBeginTime;

        private long planEndTime;

        private String distance;

        private long createTime;

        private long updateTime;

        private Integer status;

        /**
         * 领受时间
         */
        private long acceptTime;

        /**
         * 到位时间
         */
        private long arrivalTime;

        /**
         * 任务开始时间
         */
        private long taskBeginTime;
        /**
         * 任务结束时间
         */
        private long taskEndTime;

        private OutFieldFlightBean flights;

}
