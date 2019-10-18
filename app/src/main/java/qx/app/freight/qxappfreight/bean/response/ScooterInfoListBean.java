package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;

import lombok.Data;

/**
 * TODO : 板车列表信息
 * Created by pr
 */
@Data
public class ScooterInfoListBean implements Serializable {


    private String id;
    private String scooterCode;
    private Integer scooterWeight;
    private String scooterType;
    private int delFlag;
    private long createDate;
    private String createUser;
    private long updateDate;
    private String updateUser;
    /**
     * 国际国内标记
     */
    private String flightType;
    private boolean noticeTransport;
    /**
     * 0无效，1横向，2竖向
     */
    private int headingFlag;
}
