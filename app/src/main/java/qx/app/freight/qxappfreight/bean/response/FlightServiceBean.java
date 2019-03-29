package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 航班保障服务数据模型
 * 保障完成度
 * 任务节点
 * Created by mm on 2016/9/29.
 */
@Data
public class FlightServiceBean implements Serializable {
    private String completionDegree;
    private List<MilepostBean> flightMilepostlist;


}
