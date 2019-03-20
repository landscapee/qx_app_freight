package qx.app.freight.qxappfreight.bean.request;

import lombok.Data;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
public class BaseFilterEntity<T> {
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
    //复重
    private String scooterCode;
    //航班锁定
    private String flightId;
    private String minutes;
    // 出港行李数据保存时需要的行李转盘标识
    private String baggageTurntable;
    //出港行李数据上传用户ID
    private String baggageSubOperator;
    //出港行李数据上传用户名称
    private String baggageSubUserName;
    //出港行李数据上传终端ID
    private String baggageSubTerminal;
}
