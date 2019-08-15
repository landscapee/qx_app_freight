package qx.app.freight.qxappfreight.bean.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SeatChangeEntity {
    private String workerId;
    private String remark;
    private String flightNo;
    private boolean loadUnloadStatusChg;

}
