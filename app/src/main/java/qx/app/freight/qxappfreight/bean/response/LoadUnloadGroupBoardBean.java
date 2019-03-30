package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

@Data
public class LoadUnloadGroupBoardBean {
    private String workerId;
    private int taskType;
    private List<String> taskData;
}
