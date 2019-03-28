package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

@Data
public class FlightBean {

    private int total;
    private int departure;
    private int arrive;
    private int reversal;
    private int preparation;
    private List<FlightsBean> flights;

    @Data
    public static class FlightsBean {
        /**
         * flightId : 12005903
         * flightNo : ZH9484
         * scheduleTime : 1553766600000
         * ata : 1553766300000
         * atd : null
         * movement : A
         * type : 0
         * flightStatus : ARR
         * routes : 泉州,怀化,成都
         */

        private int flightId;
        private String flightNo;
        private long scheduleTime;
        private long ata;
        private long atd;
        private String movement;
        private int type;
        private String flightStatus;
        private String routes;
    }
}
