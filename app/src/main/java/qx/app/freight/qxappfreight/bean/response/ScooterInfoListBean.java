package qx.app.freight.qxappfreight.bean.response;

import lombok.Data;

/**
 * TODO : 板车列表信息
 * Created by pr
 */
@Data
public class ScooterInfoListBean {

    private String id;
    private String scooterCode;
    private int scooterWeight;
    private int scooterType;
    private int delFlag;
    private long createDate;
    private String createUser;
    private long updateDate;
    private String updateUser;

}
