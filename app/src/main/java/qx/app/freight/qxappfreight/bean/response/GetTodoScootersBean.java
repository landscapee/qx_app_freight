package qx.app.freight.qxappfreight.bean.response;

import lombok.Data;

@Data
public class GetTodoScootersBean {
        /**
         * id : 记录ID
         * scooterId : 板车id
         * flightInfoId : 航班计划id
         * total : 装载数量
         * weight : 装载重量
         * volume : 装载体积
         * destinationStation : 目的地
         * suggestRepository : 建议仓位
         * uldId : uld id(集装器记录ID)
         * createTime : 创建时间
         * createUser : 创建人
         * updateTime : 修改时间
         * updateUser : 修改人
         * delFlag : 删除标识: 0未删除 , 1删除. defualt:0
         * reWeight : 复重重量 defualt:0
         * rePureWeight :
         * reDifference : 复重差值 defualt:0
         * reDifferenceRate : 复重差率 defualt:0
         * reWeightFinish : 复重完成标识: 0.待复重; 1.完成; 2.复重异常; 3.调整完成; defualt:0;
         * personUpdateValue : 人工干预值 defualt:0
         * addWeight : 补重重量 defualt:0
         * scooterCode : 板车编码
         * scooterType : 板车类型0小 1中 2大
         * uldCode : 集装箱编码
         * uldType : 集装箱类型
         * iata : 航司二制码
         * scooterWeight : 板车自重
         * uldWeight : 集装箱自重
         * remark : 备注
         * toCityCn : 中文航段（来自预配表）
         * toCityEn : 三字码航段（来自预配表）
         * repName : 库区名
         * repPlaceNum : 库位信息
         * groupScooters :
         * updateStatus :
         * inFlight :
         * flightNo :
         * std :
         * aircraftNo :
         * seat :
         * dayOfMonth :
         * logUserId :
         * scooterStatus : 状态:0正常, 1拉回. defualt:0 (暫時修改為臨時屬性)
         * inFlightCourse :
         * currentStep :
         * singleType : 挑单类型：1 预配员运单  2：配置小队长运单
         * mergedMasterFlag : 该是否被主流程合并标识
         * groupScooterTaskId : 组板任务id
         * reWeightTaskId : 复重任务id
         * processIsEnd : 流程已经结束: 0.未结束; 1.结束.
         * prematchingFlag : 预配标识: 0.未预配; 1.已经预配;
         */

        private String id;
        private String scooterId;
        private String flightInfoId;
        private String total;
        private String weight;
        private String volume;
        private String destinationStation;
        private String suggestRepository;
        private String uldId;
        private String createTime;
        private String createUser;
        private String updateTime;
        private String updateUser;
        private String delFlag;
        private String reWeight;
        private String rePureWeight;
        private String reDifference;
        private String reDifferenceRate;
        private String reWeightFinish;
        private String personUpdateValue;
        private String addWeight;
        private String scooterCode;
        private String scooterType;
        private String uldCode;
        private String uldType;
        private String iata;
        private String scooterWeight;
        private String uldWeight;
        private String remark;
        private String toCityCn;
        private String toCityEn;
        private String repName;
        private String repPlaceNum;
        private String groupScooters;
        private String updateStatus;
        private String inFlight;
        private String flightNo;
        private String std;
        private String aircraftNo;
        private String seat;
        private String dayOfMonth;
        private String logUserId;
        private String scooterStatus;
        private String inFlightCourse;
        private String currentStep;
        private String singleType;
        private String mergedMasterFlag;
        private String groupScooterTaskId;
        private String reWeightTaskId;
        private String processIsEnd;
        private String prematchingFlag;

}
