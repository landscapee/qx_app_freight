package qx.app.freight.qxappfreight.bean.response;

import android.util.Log;

import java.util.List;

import lombok.Data;


/**
 * create by unknow on unknow
 * @tital: 出港预配 理货->航班信息 航段
 *
 * &update by guohao on 2019/5/9 11:54
 * @description:  新增字段
 */

@Data
public class FlightCabinInfo {
    /**
     * aircraftTypes : [{"id":"d59cce99ede04a5faea4644950c28900","typeName":"波音737","typeLength":33,"typeWidth":35.8,"typeHeight":0,"useState":0,"displayUseState":"启用","no":1,"itat":"737","icat":"B737","model":"C"}]
     * flightInfo : {"id":"b745536f8f184100b0830fdafa62830a","flightId":12028183,"airlineCode":"8L","flightNo":"8L811","aircraftNo":"B1563","scheduleTime":1553705400000,"estimateTime":1553705400000,"actualTime":null,"movement":"D","flightType":"W","flightIndicator":"I","aircraftType":"737","terminal":"T1","sta":null,"eta":null,"ata":null,"std":1553705400000,"etd":1553705400000,"atd":null,"seat":"230","flightStatus":"RCFLOP","flightExtStatus":"RCFLOP","flightExtRemark":"","cancelTime":null,"createDate":1553613769410,"succession":"8L","successionFlightNo":"8L9646","successionId":12005001,"carrier":null,"masterFlightId":0,"delay":0,"estimateInSeat":null,"vipMark":null,"vipNo":0,"destChangeReason":null,"destChangeDirection":null,"destChangeAirport":null,"returnReason":null,"returnCode":null,"landAbortedReason":null,"landAbortedCode":null,"aircraftIn":null,"aircraftOut":null,"preEtd":null,"preAtd":null,"nxtEta":null,"nxtAta":null,"keyMaintaince":null,"boardingTime":null,"associateAirport":"BKK","runway":null,"runwayTime":null,"delFlag":0,"delData":null,"iacoAirlineCode":null,"tobt":null,"cobt":null,"ctot":null,"acdmSeat":null,"transportStatus":0,"taskId":null,"flightCourse":"[\"CTU\",\"BKK\"]","totalScooterNum":0,"arriveWarehouseNum":0,"scooters":null,"flightCourseByAndroid":["CTU","BKK"],"luggageScanningUser":null}
     * aircraftNoRS : {"cargos":[{"id":"4bc5ca2550394932945d26eb395860ce","aircraftNo":"B1939","iata":"ZH","cargoNo":"1","planType":"1","pos":"1H","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":3558,"arm":360.8,"pr":"1","ul":"L","zone":"","validType":"[\"\",\"\",\"\",\"\",\"\"]","idx":"1"},{"id":"565063e56ec84898b202ac13026579b0","aircraftNo":"B1939","iata":"ZH","cargoNo":"4","planType":"1","pos":"4H","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":4037,"arm":900.2,"pr":"1","ul":"L","zone":"","validType":"[\"\",\"\",\"\",\"\",\"\"]","idx":"2"}],"id":"7cab9d94c5b9418a953af61b10a56ae6","aircraftNo":"B1939","iata":"ZH","creator":null,"createTime":1553658026815,"updator":null,"updateTime":null,"hld1wgt":3558,"hld1arm":360.8,"hld1vol":19,"hld1maxWgt":3558,"hld1identification":"F","hld1FwdHldMaxWgt":3558,"hld2wgt":0,"hld2arm":0,"hld2vol":0,"hld2maxWgt":0,"hld2identification":"","hld2AftHldMaxWgt":4037,"hld3wgt":0,"hld3arm":0,"hld3vol":0,"hld3maxWgt":0,"hld3identification":"","hld3BagCntMinWgt":0,"hld4wgt":4037,"hld4arm":900.2,"hld4vol":24,"hld4maxWgt":4037,"hld4identification":"R","hld4ActTnkLmtWgt":0,"hld5wgt":0,"hld5arm":0,"hld5vol":0,"hld5maxWgt":0,"hld5identification":"","bulkHld":"","bagContType":"","bagDensity":5,"volumeOfBulkHolds":"[\"\",\"\",\"\",\"\",\"\"]","mailDensity":5,"weightUnit":"KG","lengthUnit":"IN","containerPlate":"[]","fwdZone":"[\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"]","fmaxWgt":"[\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"]","fcentroid":"[\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"]","rwdZone":"[\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"]","rmaxWgt":"[\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"]","rcentroid":"[\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"]","no":1,"flightMaxWgt":7595,"flightMaxVol":43}
     */

    private FlightInfoBean flightInfo;
    private AircraftNoRSBean aircraftNoRS;
    private AircraftTypesBean aircraftTypes;

    @Data
    public static class FlightInfoBean {
        /**
         * id : b745536f8f184100b0830fdafa62830a
         * flightId : 12028183
         * airlineCode : 8L
         * flightNo : 8L811
         * aircraftNo : B1563
         * scheduleTime : 1553705400000
         * estimateTime : 1553705400000
         * actualTime : null
         * movement : D
         * flightType : W
         * flightIndicator : I
         * aircraftType : 737
         * terminal : T1
         * sta : null
         * eta : null
         * ata : null
         * std : 1553705400000
         * etd : 1553705400000
         * atd : null
         * seat : 230
         * flightStatus : RCFLOP
         * flightExtStatus : RCFLOP
         * flightExtRemark :
         * cancelTime : null
         * createDate : 1553613769410
         * succession : 8L
         * successionFlightNo : 8L9646
         * successionId : 12005001
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
         * aircraftOut : null
         * preEtd : null
         * preAtd : null
         * nxtEta : null
         * nxtAta : null
         * keyMaintaince : null
         * boardingTime : null
         * associateAirport : BKK
         * runway : null
         * runwayTime : null
         * delFlag : 0
         * delData : null
         * iacoAirlineCode : null
         * tobt : null
         * cobt : null
         * ctot : null
         * acdmSeat : null
         * transportStatus : 0
         * taskId : null
         * flightCourse : ["CTU","BKK"]
         * totalScooterNum : 0
         * arriveWarehouseNum : 0
         * scooters : null
         * flightCourseByAndroid : ["CTU","BKK"]
         * luggageScanningUser : null
         */

        private String id;
        private int flightId;
        private String airlineCode;
        private String flightNo;
        private String aircraftNo;
        private long scheduleTime;
        private long estimateTime;
        private Object actualTime;
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
        private Object atd;
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
        private Object aircraftOut;
        private Object preEtd;
        private Object preAtd;
        private Object nxtEta;
        private Object nxtAta;
        private Object keyMaintaince;
        private Object boardingTime;
        private String associateAirport;
        private Object runway;
        private Object runwayTime;
        private int delFlag;
        private Object delData;
        private Object iacoAirlineCode;
        private Object tobt;
        private Object cobt;
        private Object ctot;
        private Object acdmSeat;
        private int transportStatus;
        private Object taskId;
        private String flightCourse;
        private int totalScooterNum;
        private int arriveWarehouseNum;
        private Object scooters;
        private Object luggageScanningUser;
        private List <String> flightCourseByAndroid;
        private List<String> flightCourseCn;//所有的航段信息， 新增字段 by guohao
        private String associateAirportCn;
        private String flightBody;
        private List<Object> inWaybillRecords; //5.10 不知道 列表类型

    }
    @Data
    public static class AircraftNoRSBean {
        /**
         * cargos : [{"id":"4bc5ca2550394932945d26eb395860ce","aircraftNo":"B1939","iata":"ZH","cargoNo":"1","planType":"1","pos":"1H","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":3558,"arm":360.8,"pr":"1","ul":"L","zone":"","validType":"[\"\",\"\",\"\",\"\",\"\"]","idx":"1"},{"id":"565063e56ec84898b202ac13026579b0","aircraftNo":"B1939","iata":"ZH","cargoNo":"4","planType":"1","pos":"4H","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":4037,"arm":900.2,"pr":"1","ul":"L","zone":"","validType":"[\"\",\"\",\"\",\"\",\"\"]","idx":"2"}]
         * id : 7cab9d94c5b9418a953af61b10a56ae6
         * aircraftNo : B1939
         * iata : ZH
         * creator : null
         * createTime : 1553658026815
         * updator : null
         * updateTime : null
         * hld1wgt : 3558
         * hld1arm : 360.8
         * hld1vol : 19
         * hld1maxWgt : 3558
         * hld1identification : F
         * hld1FwdHldMaxWgt : 3558
         * hld2wgt : 0
         * hld2arm : 0
         * hld2vol : 0
         * hld2maxWgt : 0
         * hld2identification :
         * hld2AftHldMaxWgt : 4037
         * hld3wgt : 0
         * hld3arm : 0
         * hld3vol : 0
         * hld3maxWgt : 0
         * hld3identification :
         * hld3BagCntMinWgt : 0
         * hld4wgt : 4037
         * hld4arm : 900.2
         * hld4vol : 24
         * hld4maxWgt : 4037
         * hld4identification : R
         * hld4ActTnkLmtWgt : 0
         * hld5wgt : 0
         * hld5arm : 0
         * hld5vol : 0
         * hld5maxWgt : 0
         * hld5identification :
         * bulkHld :
         * bagContType :
         * bagDensity : 5
         * volumeOfBulkHolds : ["","","","",""]
         * mailDensity : 5
         * weightUnit : KG
         * lengthUnit : IN
         * containerPlate : []
         * fwdZone : ["","","","","","","","","",""]
         * fmaxWgt : ["","","","","","","","","",""]
         * fcentroid : ["","","","","","","","","",""]
         * rwdZone : ["","","","","","","","","",""]
         * rmaxWgt : ["","","","","","","","","",""]
         * rcentroid : ["","","","","","","","","",""]
         * no : 1
         * flightMaxWgt : 7595
         * flightMaxVol : 43
         */

        private String id;
        private String aircraftNo;
        private String iata;
        private Object creator;
        private Long createTime;
        private Object updator;
        private Object updateTime;
        private Double hld1wgt;
        private Double hld1arm;
        private Double hld1vol;
        private Double hld1maxWgt;
        private String hld1identification;
        private Double hld1FwdHldMaxWgt;
        private Double hld2wgt;
        private Double hld2arm;
        private Double hld2vol;
        private Double hld2maxWgt;
        private String hld2identification;
        private Double hld2AftHldMaxWgt;
        private Double hld3wgt;
        private Double hld3arm;
        private Double hld3vol;
        private Double hld3maxWgt;
        private String hld3identification;
        private Double hld3BagCntMinWgt;
        private Double hld4wgt;
        private Double hld4arm;
        private Double hld4vol;
        private Double hld4maxWgt;
        private String hld4identification;
        private Double hld4ActTnkLmtWgt;
        private Double hld5wgt;
        private Double hld5arm;
        private Double hld5vol;
        private Double hld5maxWgt;
        private String hld5identification;
        private String bulkHld;
        private String bagContType;
        private int bagDensity;
        private String volumeOfBulkHolds;
        private int mailDensity;
        private String weightUnit;
        private String lengthUnit;
        private String containerPlate;
        private String fwdZone;
        private String fmaxWgt;
        private String fcentroid;
        private String rwdZone;
        private String rmaxWgt;
        private String rcentroid;
        private int no;
        private Double flightMaxWgt;
        private Double flightMaxVol;
        private List <CargosBean> cargos;


        @Data
        public static class CargosBean {
            /**
             * id : 4bc5ca2550394932945d26eb395860ce
             * aircraftNo : B1939
             * iata : ZH
             * cargoNo : 1
             * planType : 1
             * pos : 1H
             * overlays : ["","","","",""]
             * maxWgt : 3558
             * arm : 360.8
             * pr : 1
             * ul : L
             * zone :
             * validType : ["","","","",""]
             * idx : 1
             */
            private String id;
            private String aircraftNo;
            private String iata;
            private String cargoNo;
            private String planType;
            private String pos;
            private String overlays;
            private Double maxWgt;
            private Double arm;
            private String pr;
            private String ul;
            private String zone;
            private String validType;
            private String idx;

            private Double hldWgt;
            private Double hldArm;
            private Double hldVol;
            private Double hldMaxWgt;
            private String hldIdentification;
            private Double hldActTnkLmtWgt;

        }
    }
    @Data
    public static class AircraftTypesBean {
        /**
         * id : d59cce99ede04a5faea4644950c28900
         * typeName : 波音737
         * typeLength : 33
         * typeWidth : 35.8
         * typeHeight : 0
         * useState : 0
         * displayUseState : 启用
         * no : 1
         * itat : 737
         * icat : B737
         * model : C
         */

        private String id;
        private String typeName;
        private Double typeLength;
        private Double typeWidth;
        private int typeHeight;
        private int useState;
        private String displayUseState;
        private int no;
        private String itat;
        private String icat;
        private String model;

    }
}
