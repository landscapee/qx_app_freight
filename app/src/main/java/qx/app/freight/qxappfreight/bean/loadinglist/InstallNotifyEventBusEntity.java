package qx.app.freight.qxappfreight.bean.loadinglist;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InstallNotifyEventBusEntity {

    private String flightNo;
    private int version;
    private int type;//1 预装机单 2确认按此装机 3 写入状态更新 4 确认最终装机单
}
