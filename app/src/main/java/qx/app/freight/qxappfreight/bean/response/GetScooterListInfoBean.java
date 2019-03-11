package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
public class GetScooterListInfoBean {


    /**
     * flight : {"id":"b0bf4cb543ed4304a3bb4d3639eedac2","flightId":11902827,"airlineCode":"CA","flightNo":"CA4467","aircraftNo":"B6479","scheduleTime":1550828700000,"estimateTime":1550829540000,"actualTime":1550830320000,"movement":"D","flightType":"W","flightIndicator":"D","aircraftType":"319","terminal":"T2","sta":null,"eta":null,"ata":null,"std":1550828700000,"etd":1550828700000,"atd":1550830320000,"seat":"317L","flightStatus":"NST","flightExtStatus":"NST","flightExtRemark":"","cancelTime":null,"createDate":1550758177452,"succession":"CA","successionFlightNo":"CA4192","successionId":11872586,"carrier":null,"masterFlightId":0,"delay":0,"estimateInSeat":null,"vipMark":null,"vipNo":0,"destChangeReason":null,"destChangeDirection":null,"destChangeAirport":null,"returnReason":null,"returnCode":null,"landAbortedReason":null,"landAbortedCode":null,"aircraftIn":null,"aircraftOut":1550828883000,"preEtd":null,"preAtd":null,"nxtEta":null,"nxtAta":null,"keyMaintaince":null,"boardingTime":1550826000000,"associateAirport":"XIC","runway":"02L","runwayTime":null,"delFlag":0,"delData":null,"iacoAirlineCode":"CCA","tobt":null,"cobt":1550828700000,"ctot":1550829540000,"acdmSeat":null}
     * scooters : [{"id":"e414cc77a0d04913a62270230364210d","scooterId":"5dab37031dbf467a8765d362379d9c68","flightId":"b0bf4cb543ed4304a3bb4d3639eedac2","total":null,"weight":123,"volume":123,"flightDestination":null,"suggestRepository":"H1","uldId":null,"createDate":null,"createUser":null,"updateDate":null,"updateUser":null,"delFlag":null,"reWeight":null,"reDifference":null,"reDifferenceRate":null,"reWeightFinish":null,"personUpdateValue":null,"rcInfoList":[{"id":"9e09063610a7aaa748b1dc0ae14427dd","waybillId":"1b8dfe1108eeef0d6e484d18a3625709","waybillCode":"姚秋实666","cargoId":"0b574bd87d8e823d7f04a48876771488","cargoCn":"玫瑰花","number":123,"weight":123,"volume":123,"scooterId":"5dab37031dbf467a8765d362379d9c68","scooterType":1,"scooterCode":"12661","scooterWeight":575,"repPlaceId":null,"repName":null,"repPlaceNum":"系统分配","repPlaceStatus":null,"uldId":null,"uldCode":"","uldType":null,"uldWeight":null,"iata":null,"overWeight":null,"delFlag":0,"createUser":null,"createDate":1550842005554,"updateUser":null,"updateDate":null,"updateStatus":0,"inFlight":0}],"updateStatus":null,"scooterCode":"12661","scooterType":1,"uldCode":"","uldType":null,"iata":null,"scooterWeight":575,"uldWeight":null,"inFlight":1}]
     * withoutScootereRcInfos : [{"id":"9e09063610a7aaa748b1dc0ae14427dd","waybillId":"1b8dfe1108eeef0d6e484d18a3625709","waybillCode":"姚秋实666","cargoId":"0b574bd87d8e823d7f04a48876771488","cargoCn":"玫瑰花","number":123,"weight":123,"volume":123,"scooterId":"5dab37031dbf467a8765d362379d9c68","scooterType":1,"scooterCode":"12661","scooterWeight":575,"repPlaceId":null,"repName":null,"repPlaceNum":"系统分配","repPlaceStatus":null,"uldId":null,"uldCode":"","uldType":null,"uldWeight":null,"iata":null,"overWeight":null,"delFlag":0,"createUser":null,"createDate":1550842005554,"updateUser":null,"updateDate":null,"updateStatus":0,"inFlight":0}]
     */

    private FlightBean flight;
    private List<FtRuntimeFlightScooter> scooters;
    private List<FtGroupScooter> withoutScootereRcInfos;

    @Data
    public static class FlightBean {
        /**
         * id : b0bf4cb543ed4304a3bb4d3639eedac2
         * flightId : 11902827
         * airlineCode : CA
         * flightNo : CA4467
         * aircraftNo : B6479
         * scheduleTime : 1550828700000
         * estimateTime : 1550829540000
         * actualTime : 1550830320000
         * movement : D
         * flightType : W
         * flightIndicator : D
         * aircraftType : 319
         * terminal : T2
         * sta : null
         * eta : null
         * ata : null
         * std : 1550828700000
         * etd : 1550828700000
         * atd : 1550830320000
         * seat : 317L
         * flightStatus : NST
         * flightExtStatus : NST
         * flightExtRemark :
         * cancelTime : null
         * createDate : 1550758177452
         * succession : CA
         * successionFlightNo : CA4192
         * successionId : 11872586
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
         * aircraftOut : 1550828883000
         * preEtd : null
         * preAtd : null
         * nxtEta : null
         * nxtAta : null
         * keyMaintaince : null
         * boardingTime : 1550826000000
         * associateAirport : XIC
         * runway : 02L
         * runwayTime : null
         * delFlag : 0
         * delData : null
         * iacoAirlineCode : CCA
         * tobt : null
         * cobt : 1550828700000
         * ctot : 1550829540000
         * acdmSeat : null
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
        private String flightExtStatus;
        private String flightExtRemark;
        private Object cancelTime;
        private long createDate;
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
    }
}
