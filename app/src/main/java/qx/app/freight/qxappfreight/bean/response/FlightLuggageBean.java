package qx.app.freight.qxappfreight.bean.response;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class FlightLuggageBean implements MultiItemEntity, Serializable {
//    private String airlineCode;  // 航si
    private String flightNo;  // 航班号
    private String aircraftNo;  // 飞机号
    private long scheduleTime;  //预计到达时间
    private List<String> flightCourseByAndroid;  //航线列表
    private String luggageScanningUser;  //锁定人
    private String seat;  //机位号
    private String id; //业务id
    private String flightId; //优利系统id
    private String successionId; //关联航班id
    private String movement; //航班类型 A – Arrival/到达 D – Departure/离港
    private String flightIndicator; //航班类型 D – Domestic/国内 I – International/国际 M – Mixed/混合 R – Regional/地区
    private int tpFlightType;

    public int getTpFlightType(){
        if ("A".equals(movement)){
            if ("D".equals(flightIndicator)){
                return 1;
            }else if ("I".equals(flightIndicator)){
                return 3;
            }else {
                return 0;
            }
        }else if ("D".equals(movement)){
            if ("D".equals(flightIndicator)){
                return 2;
            }else if ("I".equals(flightIndicator)){
                return 4;
            }else {
                return 0;
            }
        }else {
            return 0;
        }

    }
    @Override
    public int getItemType() {
        if (flightCourseByAndroid !=null) {
            return flightCourseByAndroid.size();
        } else {
            return 2;
        }
    }

}
