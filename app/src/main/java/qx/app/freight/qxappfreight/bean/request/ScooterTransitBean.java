package qx.app.freight.qxappfreight.bean.request;

import lombok.Data;

/**
 * Created by pr
 */
@Data
public class ScooterTransitBean {

    private String id;
    private String scooterCode;
    private int scooterWeight;
    private int scooterType;// "0:大滚筒 1:小滚筒 2:平板车",
    private int delFlag;
    private long createTime;
    private String createUser;
    private long updateTime;
    private String updateUser;
    private String flightType;
    /**
     * 0无效，1横向，竖向
     */
    private int headingFlag;
    /**
     * 所属地面代理code
     */
    private String groundAgentCode;
}
