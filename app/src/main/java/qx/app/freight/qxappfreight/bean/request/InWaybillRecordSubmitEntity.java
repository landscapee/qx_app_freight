package qx.app.freight.qxappfreight.bean.request;

import java.util.List;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.InWaybillRecord;

/**
 * 进港分拣 -- 提交/暂存 请求参数实体类
 *
 * create by guohao - 2019/4/25
 */
@Data
public class InWaybillRecordSubmitEntity {
    int flag;//  0.暂存; 1.提交
    String flightInfoId;// 航班业务id
    String flightId;// 航班尤里id
    String flightNo;// 航班号
    String taskId;// 任务id
    List<InWaybillRecord> list;//  InWaybillRecord 分拣运单实体类集合
    String userId;// 登录用户id
    String userName;// 登录人名
    String charterReWeight; //包机货包重量
}
