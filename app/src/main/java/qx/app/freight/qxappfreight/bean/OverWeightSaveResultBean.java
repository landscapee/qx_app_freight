package qx.app.freight.qxappfreight.bean;

import lombok.Getter;
import lombok.Setter;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;

@Setter
@Getter
public class OverWeightSaveResultBean {

    private GetInfosByFlightIdBean scooter;
    private double threshold;//机型 上限阈值
    private double reDifferenceSum;//差值总和
    private boolean finish;// 复重是否完成
    private boolean  hasGroupScooterTask;// 表示是否还有组板任务的存在.

}
