package qx.app.freight.qxappfreight.bean.request;

import java.util.List;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.InportTallyBean;

@Data
public class InPortTallyCommitEntity {

    /**
     * taskId : taskId
     * userId : userId
     * userName : userName
     * flightId : flightId
     * flightNo : flightNo
     */

    private String taskId;
    private String userId;
    private String userName;
    private String flightId;
    private String flightNo;
    private List<InportTallyBean> inWaybills;
}
