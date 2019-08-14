package qx.app.freight.qxappfreight.bean.request;

import java.util.List;

import lombok.Data;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
public class BaseFilterEntity<T> {
    private String currentStep;
    private boolean androidFlag;
    private int current; // FIXME check this code
    private int size;
    private T filter;
    private String stepOwner;
    //临时添加
    private String userId;
    //装卸机代办添加
    private String workerId;
    private String taskType;
    //组板字段添加
    private String undoType;
    //进港提货
    private String billId;
    private String id;
    private String outStorageUser;
    private String counterbillId;
    private String taskId;
    private String completeUser;
    private int forkliftTruckNumber; //叉车数量
    //复重
    private String scooterCode;
    //航班锁定
    private String flightId;
    private String minutes;

    //查询库区
    private String deptCode;
    private int reservoirType;

    //代办查询增加字段
    private String roleCode;
    //消息中心
    private List<String> desc;
    private String messageId;
    private String noticeId;
//    private String userId;  需要添加
    //航班动态
    private String day;

    private String createUser;
    private int outboundNumber;
    private String  waybillId;
    private String  flightInfoId;
    private String facility;
    private String staffId;
    private String staffIds;
    private String operationUser;
    private String operationUserName;
    private int documentType;
    //报载记录ID
    private String reportInfoId;
    //操作类型
    private String auditType;
    //退回报载原因
    private String returnReason;

    private int sort;
}
