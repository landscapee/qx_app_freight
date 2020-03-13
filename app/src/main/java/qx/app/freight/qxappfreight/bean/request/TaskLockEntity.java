package qx.app.freight.qxappfreight.bean.request;

import java.util.List;

import lombok.Data;

/**
 * Created by guohao On 2019/6/24 14:47
 *
 * 待办锁定 请求实体
 */
@Data
public class TaskLockEntity {

    private String userId;

    private List<String> taskId;

    private String roleCode;

    private String freightName;

}
