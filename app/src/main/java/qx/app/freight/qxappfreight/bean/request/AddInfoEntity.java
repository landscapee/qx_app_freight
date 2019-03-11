package qx.app.freight.qxappfreight.bean.request;

import java.io.Serializable;

import lombok.Data;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
public class AddInfoEntity implements Serializable {

    /**
     * waybillId : 8b2f73d2afaec8294641425ec2abb5cf
     * cargoId : 12
     * number : 100
     * weight : 200
     * volume : 300
     * scooterId : 7c9dce61cd6c7ba5ee9acd9ba59772d9
     * scooterType : 1
     * scooterCode : 板车编号
     * scooterWeight : 8888
     * repPlaceId : 929a4d6e49154fc939ef9bdc003eb625
     * repName : 普货01
     * repPlaceNum : 001
     * repPlaceStatus : 1
     * uldId : e273839e8c53e2d072774b80bc31c180
     * uldCode : 集装箱编码
     * uldType : AKE集装箱类型
     * uldWeight : 8888
     * iata : 3U
     * overWeight : 100
     * createUser : zhuyuhang
     * updateUser : zhuyuhang
     */

    private String waybillId;
    private String cargoId;
    private int number;
    private int weight;
    private int volume;
    private String scooterId;
    private int scooterType;
    private String scooterCode;
    private int scooterWeight;
    private String repPlaceId;
    private String repName;
    private String repPlaceNum;
    private String repPlaceStatus;
    private String uldId;
    private String uldCode;
    private String uldType;
    private int uldWeight;
    private String iata;
    private int overWeight;
    private String createUser;
    private String updateUser;


}
