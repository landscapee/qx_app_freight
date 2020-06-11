package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

/**
 * Created by zzq On 2020/6/11 14:46 & Copyright (C), 青霄科技
 *
 * @文档说明: 关联搜索航班结果实体
 */
@Data
public class SearchFlightInfoBean {
    /**
     * id : 4f868e9ea6ac4423b854ddd15a120a76
     * flightId : 14159529
     * airlineCode : MU
     * flightNo : MU5418
     * aircraftNo : B6560
     * scheduleTime : 1591601100000
     * estimateTime : 1591601940000
     * actualTime : 1591601400000
     * movement : D
     * flightType : W
     * flightIndicator : D
     * aircraftType : 320
     * terminal : T2
     * sta : null
     * eta : null
     * ata : null
     * std : 1591601100000
     * etd : 1591601100000
     * atd : 1591601400000
     * seat : 138
     * flightStatus : NST
     * flightStatusCn : null
     * flightExtStatus : NST
     * flightExtRemark :
     * cancelTime : null
     * createDate : null
     * succession : MU
     * successionFlightNo : MU5596
     * successionId : 14160478
     * carrier : null
     * masterFlightId : 0
     * delay : 0
     * estimateInSeat : null
     * vipMark : null
     * vipNo : 0
     * destChangeReason : null
     * destChangeDirection : null
     * destChangeAirport : null
     * returnReason : null
     * returnCode : null
     * landAbortedReason : null
     * landAbortedCode : null
     * aircraftIn : null
     * aircraftOut : 1591600609000
     * preEtd : null
     * preAtd : null
     * nxtEta : null
     * nxtAta : null
     * keyMaintaince : null
     * boardingTime : 1591598700000
     * associateAirport : SHA
     * runway : 02L
     * runwayTime : null
     * delFlag : 0
     * delData : null
     * iacoAirlineCode : CES
     * tobt : null
     * cobt : 1591601100000
     * ctot : 1591601940000
     * acdmSeat : null
     * transportStatus : 0
     * taskId : null
     * route : ["CTU","SHA"]
     * totalScooterNum : 0
     * arriveWarehouseNum : 0
     * originSta : null
     * dataType : 0
     * scooters : null
     * flightCourseByAndroid : ["CTU","SHA"]
     * flightCourseCn : ["成都","上海"]
     * airportCn : ["成都双流机场（蓉）","上海虹桥"]
     * associateAirportCn : null
     * associateAirportNameCn : null
     * luggageScanningUser : null
     * flightBody : null
     * inWaybillRecords : []
     * cargos : []
     * interArrivalCargoInfos : []
     * interManifestWaybills : []
     * manifestWaybills : []
     * airlineCodeCn : null
     */
    private String id;
    private int flightId;
    private String airlineCode;
    private String flightNo;
    private String aircraftNo;
    private long scheduleTime;
    private long estimateTime;
    private long actualTime;
    private String movement;
    private String flightType;
    private String flightIndicator;
    private String aircraftType;
    private String terminal;
    private Object sta;
    private Object eta;
    private Object ata;
    private long std;
    private long etd;
    private long atd;
    private String seat;
    private String flightStatus;
    private Object flightStatusCn;
    private String flightExtStatus;
    private String flightExtRemark;
    private Object cancelTime;
    private Object createDate;
    private String succession;
    private String successionFlightNo;
    private int successionId;
    private Object carrier;
    private int masterFlightId;
    private int delay;
    private Object estimateInSeat;
    private Object vipMark;
    private int vipNo;
    private Object destChangeReason;
    private Object destChangeDirection;
    private Object destChangeAirport;
    private Object returnReason;
    private Object returnCode;
    private Object landAbortedReason;
    private Object landAbortedCode;
    private Object aircraftIn;
    private long aircraftOut;
    private Object preEtd;
    private Object preAtd;
    private Object nxtEta;
    private Object nxtAta;
    private Object keyMaintaince;
    private long boardingTime;
    private String associateAirport;
    private String runway;
    private Object runwayTime;
    private int delFlag;
    private Object delData;
    private String iacoAirlineCode;
    private Object tobt;
    private long cobt;
    private long ctot;
    private Object acdmSeat;
    private int transportStatus;
    private Object taskId;
    private String route;
    private int totalScooterNum;
    private int arriveWarehouseNum;
    private Object originSta;
    private int dataType;
    private Object scooters;
    private Object associateAirportCn;
    private Object associateAirportNameCn;
    private Object luggageScanningUser;
    private Object flightBody;
    private Object airlineCodeCn;
    private List<String> flightCourseByAndroid;
    private List<String> flightCourseCn;
    private List<String> airportCn;
    private List<?> inWaybillRecords;
    private List<?> cargos;
    private List<?> interArrivalCargoInfos;
    private List<?> interManifestWaybills;
    private List<?> manifestWaybills;
}
