package qx.app.freight.qxappfreight.bean.response;

import lombok.Data;

@Data
public class GetScooterByScooterCodeBean {

        /**
         * scooterId : 板车id
         * flightId : 航班计划id
         * total : 装载数量
         * weight : 装载重量
         * volume : 装载体积
         * flightDestination : 目的地
         * suggestRepository : 建议仓位
         * uldId : uld id(集装器记录ID)
         * createDate :
         * createUser : 创建人
         * updateDate : 修改时间
         * updateUser : 修改人
         * delFlag : 删除标识: 0删除 , 1未删除.
         * rcInfoList : {}
         * updateStatus :
         * reWeight : 复重重量
         * reDifference : 复重差值
         * reDifferenceRate : 复重差率
         * reWeightFinish : 复重完成标识: 0待复重, 1完成, 2复重异常
         * scooterCode : 板车号
         * scooterType : 1:大板  2:小板
         * uldCode : 集装器编号
         * uldType : 集装器类型
         * iata : 航司二制码
         * scooterWeight : 板车自重
         * uldWeight : 集装器自重
         * flightNo : 航班号
         * std : 预计起飞时间
         * aircraftNo : 机尾号
         * dayOfMonth : 当月的几号
         */

        private String scooterId;
        private String flightId;
        private String total;
        private String weight;
        private String volume;
        private String flightDestination;
        private String suggestRepository;
        private String uldId;
        private String createDate;
        private String createUser;
        private String updateDate;
        private String updateUser;
        private String delFlag;
        private RcInfoListBean rcInfoList;
        private String updateStatus;
        private String reWeight;
        private String reDifference;
        private String reDifferenceRate;
        private String reWeightFinish;
        private String scooterCode;
        private String scooterType;
        private String uldCode;
        private String uldType;
        private String iata;
        private String scooterWeight;
        private String uldWeight;
        private String flightNo;
        private String std;
        private String aircraftNo;
        private String dayOfMonth;

        @Data
        public static class RcInfoListBean {
        }
}
