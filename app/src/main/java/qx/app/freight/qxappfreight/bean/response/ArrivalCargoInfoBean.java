package qx.app.freight.qxappfreight.bean.response;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.InWaybillRecord;

/**
 * Created by zzq On 2020/7/1 15:41 & Copyright (C), 青霄科技
 *
 * @文档说明: 根据运单号查数据库，如果库里有此运单号，返回的运单数据实体
 */
@Data
public class ArrivalCargoInfoBean {
    /**
     * status : 200
     * message : 无运单
     * rowCount : null
     * data : null
     * flag : null
     */
    private String status;
    private String message;
    private String rowCount;
    private InWaybillRecord data;
    private String flag;
}
