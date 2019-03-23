package qx.app.freight.qxappfreight.listener;

import qx.app.freight.qxappfreight.bean.response.TransportListBean;

public interface InportTallyInterface {
    void toDetail(TransportListBean item);
    void toFFM(TransportListBean item);
}
