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
    /*************行李区数据******************/
    // 出港行李数据保存时需要的行李转盘标识
    private String baggageTurntable;
    //出港行李数据上传用户ID
    private String baggageSubOperator;
    //出港行李数据上传用户名称
    private String baggageSubUserName;
    //出港行李数据上传终端ID
    private String baggageSubTerminal;
}
