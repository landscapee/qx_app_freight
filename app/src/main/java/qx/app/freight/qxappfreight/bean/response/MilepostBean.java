package qx.app.freight.qxappfreight.bean.response;


import lombok.Data;

@Data
public class MilepostBean {
    /**
     * id : c5f8a33c-b0a4-49b9-a3a3-a26a1d1e8f9d
     * flightId : 11541555
     * milestoneCode : PreTakeOff
     * milestoneName : 前站起飞
     * estimatedTime : null
     * actualTime : 1543677060000
     * createTime : 1543677375757
     * userId : null
     * userName : null
     * fmovement : null
     * mmovement : A
     * <p>
     * 缺的字段
     * * isDeviation : 是否偏离
     * taskStepId：91CB91F00724C90B1C3DBAC1B2EE904B(步骤id)
     * taskId : 91CB91F00724C90B1C3DBAC1B2EE904B（任务id）
     * taskState : 2（保障节点状态）
     * showFlag : 是否跳转指定步骤页面 1：是 ， 5：否
     */
    private String id;
    private int flightId;
    private String milestoneCode;
    private String milestoneName;
    private long estimatedTime;
    private long actualTime;
    private long createTime;
    private String userId;
    private String userName;
    private String fmovement;
    private String mmovement;


}

