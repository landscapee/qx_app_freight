package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
@Data
public class OutFieldFlightBean implements Serializable {

    private String id;

    private int flightId;

    private String airlineCode;

    private String iacoAirlineCode;

    private String flightNo;

    private String aircraftNo;

    private long scheduleTime; //进港 预计到港时间，出港 预计出港时间

    private long estimateTime;

    private long actualTime;

    private String movement;

    private String flightType;

    private String flightIndicator;

    private String aircraftType;

    private String terminal;

    private long sta;

    private long eta;

    private long ata;

    private long std;

    private long etd;

    private long atd;

    private String seat;

    private String flightStatus;

    private String flightExtStatus;

    private String flightExtRemark;

    private long cancelTime;

    private long createTime;

    private String succession;

    private String successionFlightNo;

    private int successionId;// 航班保障组 id

    private int masterFlightId;

    private int delay;

    private long estimateInSeat;

    private Object vipMark;

    private int vipNo;

    private Object destChangeReason;

    private Object destChangeDirection;

    private Object destChangeAirport;

    private Object returnReason;

    private Object returnCode;

    private Object landAbortedReason;

    private Object landAbortedCode;

    private long aircraftIn;

    private Object aircraftOut;

    private long preEtd;

    private long preAtd;

    private long nxtEta;

    private long nxtAta;

    private Object keyMaintaince;

    private long boardingTime;

    private String runway;

    private long runwayTime;

    private int deleted;

    private long delTime;

    private Object ctot;

    private Object cobt;

    private Object tobt;

    private String routers;

    private List<String> route;//航线信息 "[\"KMG\",\"CTU\"]"
}