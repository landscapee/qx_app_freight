package qx.app.freight.qxappfreight.bean.request;

import lombok.Data;

@Data
public class UnLoadRequestEntity {

    /**
     * flightId : 当前航班优利系统ID
     * unloadingUser : 当前请求卸机单用户
     */

    private String flightId;
    private String unloadingUser;
    private String operationUserName;
}
