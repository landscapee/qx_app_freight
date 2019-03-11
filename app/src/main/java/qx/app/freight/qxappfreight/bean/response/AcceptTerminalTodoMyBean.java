package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class AcceptTerminalTodoMyBean implements Serializable {

    private String taskId;

    private String taskNumber;

    private String projectName;

    private String userId;

    private List<OutFieldTaskMyBean> tasks;



}
