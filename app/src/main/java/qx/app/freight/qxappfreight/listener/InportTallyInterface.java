package qx.app.freight.qxappfreight.listener;

import qx.app.freight.qxappfreight.bean.InPortTallyEntity;

public interface InportTallyInterface {
    void toDetail(InPortTallyEntity item);
    void toFFM(InPortTallyEntity item);
}
