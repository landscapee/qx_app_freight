package qx.app.freight.qxappfreight.bean.response;


import java.io.Serializable;
import java.util.List;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.WeightWayBillBean;

@Data
public class GetInfosByFlightIdBean implements Serializable {
        /**id   :    板id
         * scooterId : 板车id
         * flightId : 航班计划id    flightInfoId
         * total : 装载数量
         * weight : 装载重量
         * volume : 装载体积
         * flightDestination : 目的地  destinationStation
         * suggestRepository : 建议仓位
         * uldId : uld id(集装器记录ID)
         * createDate :   改成  createTime
         * createUser : 创建人
         * updateDate : 修改时间  updateTime
         * updateUser : 修改人
         * delFlag : 删除标识: 0删除 , 1未删除.
         * rcInfoList : {}
         * updateStatus :
         * reWeight : 复重重量
         * reDifference : 复重差值
         * reDifferenceRate : 复重差率
         * reWeightFinish : 复重完成标识: 0待复重, 1完成, 2复重异常
         * scooterCode : 板车号
         * scooterType : 板车类型0小 1中 2大
         * uldCode : 集装器编号
         * uldType : 集装器类型
         * iata : 航司二制码
         * scooterWeight : 板车自重
         * uldWeight : 集装器自重
         * flightNo : 航班号
         * std : 预计起飞时间
         * etd : 预计离岗时间
         * scheduleTime : 计划时间
         * aircraftNo : 机尾号
         * dayOfMonth : 当月的几号
         * taskId : 任务id
         * logUserId :  登录人id
         * seat  :   机位号
         * rePureWeight  :   复重净重
         */




        private String id;
        private String scooterId;
        private String flightInfoId;
        private String total;
        private double weight;
        private double volume;
        private String destinationStation;
        private String suggestRepository;
        private String uldId;
        private String createTime;
        private String createUser;
        private String updateTime;
        private String updateUser;
        private String delFlag;
        private Object rcInfoList;
        private String updateStatus;
        private double reWeight;
        private double reDifference;
        private double reDifferenceRate;
        private int reWeightFinish;
        private String scooterCode;
        private int scooterType;
        private String uldCode;
        private String uldType;
        private String iata;
        private double scooterWeight;
        private double uldWeight;
        private String flightNo;
        private long std;
        private long etd;
        private long scheduleTime;
        private String aircraftNo;
        private String dayOfMonth;

        private String taskId;
        private String logUserId;
        private String remark;
        private String seat;
        private String reWeightTaskId;
        private String currentStep;
        private double personUpdateValue;
        private double rePureWeight;

        private String taskTypeCode;
        /**
         * 复重称重人id
         */
        private String reWeighedUserId;

        /**
         * 复重称重完成时间
         */
        private long reWeighedTime;

        /**
         *包含的运单数据
         */
        private List<WeightWayBillBean> groupScooters;






}
