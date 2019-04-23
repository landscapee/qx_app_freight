package qx.app.freight.qxappfreight.bean.request;

import java.io.Serializable;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.response.DeclareWaybillBean;

/**
 * 换单审核 - 提交（同意或者拒绝）
 */
@Data
public class ChangeWaybillEntity implements Serializable {

    private int flag;//  1:同意，0：拒绝
    private DeclareWaybillBean declareWaybill;
    private String taskId;
    private String userid;
}
