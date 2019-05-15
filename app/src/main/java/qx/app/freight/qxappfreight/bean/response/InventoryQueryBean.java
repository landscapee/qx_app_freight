package qx.app.freight.qxappfreight.bean.response;

import lombok.Data;

@Data
public class InventoryQueryBean {

    private String id;

    /**
     * 任务类型：(0：鲜活清库；1：全仓清库)
     */
    private int taskType;

    /**
     * 任务创建时间
     */
    private long createTime;

    /**
     * 任务结束时间
     */
    private long endTime;

    /**
     * 状态：(0：已完成；1：进行中)
     */
    private int status;

    /**
     * 审核处置人
     */
    private String handler;

    /**
     * 任务创建人
     */
    private String createUser;

}
