package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class AcceptTerminalTodoBean implements Serializable {

    private String taskId;

    private String taskNumber;

    private String projectName;

    private String userId;

    private List<OutFieldTaskBean> tasks;

    private boolean isExpand = true;

    private List<List<OutFieldTaskBean>> useTasks;

    private Map<String, List <OutFieldTaskBean>> collect;

}
