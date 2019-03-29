package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

@Data
public class FlightInfoBean {

    /**
     * flightId : 11702523
     * flightNo : 3U5096
     * correlationFlightId : 0
     * correlationFlightNo : null
     * flightType : 连班
     * preAtd : null
     * eta : 1546535700000
     * sta : 1546535700000
     * std : null
     * etd : null
     * atd : null
     * ata : null
     * cobt : null
     * ctot : null
     * seat : null
     * gate : null
     * aircrafType : 320
     * aircrafNumber : B1607
     * aircrafModel : null
     * carousel : null
     * seatingTime : null
     * flightIndicator : D
     * flightTypeDate : null
     * aircraftFlightType : C
     * returnFliht : false
     * alternate : false
     * alternateAirport : null
     * delay : false
     * vip : false
     * flightStatus : 正常
     * flightStatusCode : 6
     * routes : ["合肥","成都"]
     * airlines : 中国国际货运航空有限公司
     * weather : {"id":"143d6df0-8d60-49f7-8385-bae91497","aptIata":"合肥","type":" 霾","temperature":"06","windSpeed":"2 m/s","windPower":"2级","windDirection":"100","visib":"3300","cloudHight":"云高良好","airPressure":"1030","dewPoint":"-01","rvr":"","reportTime":"2019-01-03 14:00:00","status":null,"createTime":1546497301000}
     * movement : A
     * projectCode : null
     * operationCode : null
     */

    private int flightId;
    private String flightNo;
    private int correlationFlightId;
    private Object correlationFlightNo;
    private String flightType;
    private Object preAtd;
    private long eta;
    private long sta;
    private long std;
    private long etd;
    private long atd;
    private long ata;
    private long cobt;
    private long ctot;
    private String seat;
    private String gate;
    private String aircrafType;
    private String aircrafNumber;
    private Object aircrafModel;
    private String carousel;
    private long seatingTime;
    private String flightIndicator;
    private Object flightTypeDate;
    private String aircraftFlightType;
    private boolean returnFliht;
    private boolean alternate;
    private Object alternateAirport;
    private boolean delay;
    private boolean vip;
    private String flightStatus;
    private int flightStatusCode;
    private String airlines;
    private WeatherBean weather;
    private String movement;
    private Object projectCode;
    private Object operationCode;
    private List<String> routes;
    private boolean hasReceived;

    @Data
    public static class WeatherBean {
        /**
         * id : 143d6df0-8d60-49f7-8385-bae91497
         * aptIata : 合肥
         * type :  霾
         * temperature : 06
         * windSpeed : 2 m/s
         * windPower : 2级
         * windDirection : 100
         * visib : 3300
         * cloudHight : 云高良好
         * airPressure : 1030
         * dewPoint : -01
         * rvr :
         * reportTime : 2019-01-03 14:00:00
         * status : null
         * createTime : 1546497301000
         */

        private String id;
        private String aptIata;
        private String type;
        private String temperature;
        private String windSpeed;
        private String windPower;
        private String windDirection;
        private String visib;
        private String cloudHight;
        private String airPressure;
        private String dewPoint;
        private String rvr;
        private String reportTime;
        private Object status;
        private long createTime;


    }
}
