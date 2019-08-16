package qx.app.freight.qxappfreight.bean.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskClearEntity {
    private String type;//协作任务类型 clear 清场
    private Long flightId;
    private String staffId;
    private String seat;

}
