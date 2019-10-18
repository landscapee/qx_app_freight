package qx.app.freight.qxappfreight.bean.loadinglist;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InstallNotifyEventBusEntity {

    private String flightNo;
    private int type;//1 通知录入 2 装机单确认 3 写入状态更新
}
