package qx.app.freight.qxappfreight.bean.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 分拣- 运输板车到库数量变化
 */
@Getter
@Setter
public class ScooterArriveNumChangeEntity {
        private int arriveWarehouseNum; //已到库板车
        private int totalScooterNum;//总板车
        private String flightInfoId;

}
