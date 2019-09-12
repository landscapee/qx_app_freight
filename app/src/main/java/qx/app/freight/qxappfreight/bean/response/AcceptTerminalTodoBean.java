package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class AcceptTerminalTodoBean implements Serializable {

    private String taskId;

    private String taskNumber;

    private String projectName; //FollowMeCar 牵引车 ；CargoOutTransport 货运运输 ；CargoSiteClearing 货物清楚 ；LuggageTransport 行李运输；EquipmentGuarantee 设备保障
    private String userId;

    private List<OutFieldTaskBean> tasks;

    private boolean isExpand = false;

    private List<List<OutFieldTaskBean>> useTasks;

    private String transfortType; //0-大滚筒（宽），1-小滚筒，2-平板，3-大滚筒（窄）
    private String taskType;

    private String  taskIntro;//临时任务 描述
//    private Map<String, List <OutFieldTaskBean>> collect;

}
