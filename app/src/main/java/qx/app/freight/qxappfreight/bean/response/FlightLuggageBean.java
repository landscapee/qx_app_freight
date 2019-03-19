package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

@Data
public class FlightLuggageBean {
    private String airlineCode;  // 航班号
    private String aircraftNo;  // 飞机号
    private long scheduleTime;  //预计到达时间
    private List<String> flightCourseByAndroid;  //航线列表
    private String luggageScanningUser;  //锁定人
    private String seat;  //机位号

}
