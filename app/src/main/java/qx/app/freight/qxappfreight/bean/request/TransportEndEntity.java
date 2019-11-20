package qx.app.freight.qxappfreight.bean.request;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;

/**
 * TODO : 用于运输接口—开始运输和结束运输--
 * 开始运输接口只传  id
 * 结束运输要传两个  id  dtoType
 * 板车删除取消锁定接口只传  id
 * 扫描板车并锁定   tpScooterCode  tpOperator
 * Created by pr
 */
@Data
public class TransportEndEntity implements Serializable {
    private String id;//用于 删除拉货记录
    private String taskId;//运输任务id
    private String taskType;//代办类型 （首件行李接口注释 1 代表 锁定 2 通知运输）
    private List<TransportTodoListBean> scooters;
    private List<TpFlightStep> steps;
    private String seat;
    private String tpCargoType;//首件行李 删除用

    private boolean endUnloadTask=true;//false  宽体机卸机保障 传false 不完成卸机任务；结束卸机位置传true

    private String taskTypeCode;//任务类型
}
