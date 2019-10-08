package qx.app.freight.qxappfreight.bean.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 叉车使用费用
 */
@Getter
@Setter
public class ForkliftWorkingCostBean {
    /**
     * id : 9b21742c5dc0e21d756950b400a9bdd5
     * waybillId : 1e364983b4fd5b71f3ee9e344aa86998
     * number : 1
     * createUser : u668c6577796c44a993f69794c980067c
     * createUserName : 刘磊
     * createTime : 1569393275000
     */
    private String id;
    private String waybillId;
    private int number;
    private String createUser;
    private String createUserName;
    private long createTime;
    private double charge;


}
