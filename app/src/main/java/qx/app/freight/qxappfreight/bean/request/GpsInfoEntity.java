package qx.app.freight.qxappfreight.bean.request;

import lombok.Data;

@Data
public class GpsInfoEntity {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 手持ID
     */
    private String terminalId;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 上报时间
     */
    private String time;
}
