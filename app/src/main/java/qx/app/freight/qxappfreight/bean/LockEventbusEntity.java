package qx.app.freight.qxappfreight.bean;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.WebSocketResultBean;
import qx.app.freight.qxappfreight.utils.CommonJson4List;

@Getter
@Setter
public class LockEventbusEntity implements Serializable {
    private int flag;
    private WebSocketResultBean webSocketResultBean;
    private CommonJson4List commonJson4List;
}
