package qx.app.freight.qxappfreight.bean.request;

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
public class TransportEndEntity {
    private String id;//用于 删除拉货记录
    private String taskId;//用于 识别代办类型
    private List<TransportTodoListBean> mainInfos;
    private List<TransportTodoListBean> scooters;
    private List<TpFlightStep> steps;

}
