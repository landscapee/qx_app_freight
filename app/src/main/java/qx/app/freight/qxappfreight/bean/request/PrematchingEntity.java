package qx.app.freight.qxappfreight.bean.request;

import lombok.Data;

@Data
public class PrematchingEntity {
    private String taskHandler;
    private String flightNo;
    private String taskStartTime;
    private String taskEndTime;
    private String taskType;

}
