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
    private int  currentVersion;//当前装机单版本
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
    private String areaId;// 库区id
    private int outboundNumber;
    private String  waybillId;
    private String  flightInfoId;
    private String facility;
    private String staffId;
    private String staffIds;

    private int documentType; //1 货邮舱单 2 预装机单 3 预装机单建议 4 卸机单 5 最终装机单 6 最终装机单建议
    //报载记录ID
    private String reportInfoId;//货邮仓单ID/装机单ID
    //操作类型
    private String auditType;
    //退回报载原因
    private String returnReason;
    private String installedSingleConfirmUser;
    private String operationUser;
    private String operationUserName;
    private int sort;
    private boolean filterAtd;//true 货邮舱单 装机单 待办 false 结载待办
    private boolean filterHycd;//true  货邮舱单 待办 这个字段传true
    private boolean  filterZjd;//true 装机单待办

    /**
     * 备注
     */
    private String remark;
    private int type;
    private String printName;
    private String exceptionContent; //异常内容

    private int location; //1:最终装机单录入 2:预装机单录入

    private String waybillCode;
}
