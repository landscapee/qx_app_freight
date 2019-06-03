package qx.app.freight.qxappfreight.bean.response;

import lombok.Data;

@Data
public class ChangeStorageBean {
    /**
     * 任务id
     */
    private String taskId;

    /**
     * 操作人
     */
    private String userId;

    /**
     * 0拒绝 1通过
     */
    private int judge;

    private DeclareApplyForRecords declareApplyForRecords;

}
