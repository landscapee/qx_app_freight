package qx.app.freight.qxappfreight.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * created by swd
 * 2019/8/2 11:33
 */

@Data
public class WeightWayBillBean implements Serializable {

    /**
     * id : 864ecc6208ba281d81b160ceb21e0362
     * waybillId : d942fa45b5b94bc084c65aa943c62849
     * waybillCode : xxx-19072603
     * number : 12
     * weight : 12
     * volume : 1
     * runTimeScooterId : dc559d294dc546bda3c960bcd8b9472d
     * scooterId : 782ce60b2aa14b8a83ae7b9ea5f568a4
     * scooterType : 2
     * scooterCode : 24994
     * scooterWeight : 510
     * repPlaceId : null
     * repName : null
     * repPlaceNum : null
     * repPlaceStatus : null
     * delFlag : 0
     * createUser : null
     * createTime : 1564126907715
     * updateUser : ua6f80d0ca6ab4d2fbc5c362e09a224a9
     * updateTime : null
     * updateStatus : 0
     * inFlight : 0
     * addWeight : 0
     * infoType : 0
     * addWeightStatus : 0
     * groupScooterStatus : 40
     * destinationStation : －－
     * toCityCn : null
     * toCityEn : JMJ
     * inFlightCourse : 0
     * singleType : 1
     * mergedMasterFlag : 0
     * groupScooterTaskId : 73327975
     * reWeightTaskId : 73327980
     * processIsEnd : 0
     * prematchingFlag : 1
     * flightInfoId : 429773ebd11748708fafaab86edb16c7
     * rcInfoId : null
     * cargoCn : 其他
     * selectedStatus : 1
     * signUserId : null
     */

    private String id;
    private String waybillId;
    private String waybillCode;
    private int number;
    private double weight;
    private double volume;
    private String runTimeScooterId;
    private String scooterId;
    private int scooterType;
    private String scooterCode;
    private int scooterWeight;
    private Object repPlaceId;
    private Object repName;
    private Object repPlaceNum;
    private Object repPlaceStatus;
    private int delFlag;
    private Object createUser;
    private long createTime;
    private String updateUser;
    private Object updateTime;
    private int updateStatus;
    private int inFlight;
    private int addWeight;
    private int infoType;
    private int addWeightStatus;
    private int groupScooterStatus;
    private String destinationStation;
    private Object toCityCn;
    private String toCityEn;
    private int inFlightCourse;
    private int singleType;
    private int mergedMasterFlag;
    private String groupScooterTaskId;
    private String reWeightTaskId;
    private int processIsEnd;
    private int prematchingFlag;
    private String flightInfoId;
    private Object rcInfoId;
    private String cargoCn;
    private int selectedStatus;
    private Object signUserId;

}
