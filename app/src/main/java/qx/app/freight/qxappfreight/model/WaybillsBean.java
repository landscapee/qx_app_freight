package qx.app.freight.qxappfreight.model;

import lombok.Data;

/**
 * created by swd
 * 2019/7/3 16:42
 */
@Data
public class WaybillsBean {
    /**
     * id : null
     * waybillId : 2e1657a507244d40883bc7536b110d50
     * waybillCode : 911-19047405
     * number : 0
     * weight : null
     * volume : null
     * runTimeScooterId : null
     * scooterId : null
     * scooterType : 0
     * scooterCode : RCX,VAL
     * scooterWeight : 0
     * repPlaceId : null
     * repName : null
     * repPlaceNum : null
     * repPlaceStatus : null
     * inFlight : 0
     * groupScooterStatus : 0
     * destinationStation : null
     * cargoCn : 黄金,白金,贵重金属
     * routeEn : null
     * cargoType : null
     * inflightnum : 30000
     * totalnum : 30000
     * inflightweight : 3000
     * totalweight : 3000
     * inFlightVolume : 7
     * totalVolume : 7
     * remark : null
     * updateWeight : null
     * mailType : null
     * updateFlag : 1
     */

    private String id;
    private String waybillId;
    private String waybillCode;
    private int number;
    private double weight;
    private Object volume;
    private Object runTimeScooterId;
    private Object scooterId;
    private int scooterType;
    private String scooterCode;
    private double scooterWeight;
    private Object repPlaceId;
    private Object repName;
    private Object repPlaceNum;
    private Object repPlaceStatus;
    private int inFlight;
    private int groupScooterStatus;
    private Object destinationStation;
    private String cargoCn;
    private Object routeEn;
    private Object cargoType;
    private int inflightnum;
    private int totalnum;
    private double inflightweight;
    private double totalweight;
    private double inFlightVolume;
    private double totalVolume;
    private String remark;
    private String specialCode;
    private Object updateWeight;
    private Object mailType;
    private int updateFlag;
}