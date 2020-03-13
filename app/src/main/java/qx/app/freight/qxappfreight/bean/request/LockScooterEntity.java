package qx.app.freight.qxappfreight.bean.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LockScooterEntity {

    /**
     * reportInfoId : 123456fqwefqwefgew7890
     * scooterId : 板车记录ID
     * operationUser : 当前监装人员ID
     * operationType : 1
     */

    private String reportInfoId;
    private String scooterId;
    private String operationUser;
    private int operationType;


}
