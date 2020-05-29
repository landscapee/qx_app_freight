package qx.app.freight.qxappfreight.bean.request;

import java.math.BigDecimal;

import lombok.Data;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
public class StorageCommitEntity {
    /****
     *
     * {
     *   "waybillId" : "运单id",
     *   "insFile":"报检员资质路径"，
     *   "insCheck" : "报检是否合格1合格 0不合格",
     *   "fileCheck" : "资质是否合格1合格 0不合格",
     *   "packaging" : "是否已包装1勾选 0不勾选",
     *   "require" : "是否满足航空公司要求1勾选 0不勾选",
     *   "spotResult" : "抽检结果1合格 0不合格",
     *   "unspotReson" : "未通过原因",
     *   "type" : "0暂存 1提交",
     *   "taskId" : "当前任务id",
     *   "userId" : "当前提交人id"
     * }
     */
    private String insFile;
    private String waybillId;
    private String waybillCode;
    private int insCheck;
    private int fileCheck;
    private int packaging;
    private int require;
    private int spotResult;
    private int insStatus;
    private String insUserId;

    private String addOrderId;
    /**
     * 报检员名称
     */
    private String insUserName;
    /**
     * 报检员危险开始时间
     */
    private long insDangerStart;
    /**
     * 报检员危险结束时间
     */
    private long insDangerEnd;
    /**
     * 报检员资质结束时间
     */
    private long insEndTime;
    /**
     * 报检员头像地址
     */
    private String insUserHead;
    private int type;
    private String taskId;
    private String taskTypeCode;
    private String unspotReson;
    private String userId;
    /**
     * 报检员资质起始时间
     */
    private long insStartTime;

    /**
     * 件数
     */
    private BigDecimal number;
    /**
     * 重量
     */
    private BigDecimal weight;

    private String remark;

}
