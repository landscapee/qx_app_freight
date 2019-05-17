package qx.app.freight.qxappfreight.listener;

import qx.app.freight.qxappfreight.bean.response.TransportListBean;

public interface InportTallyInterface {
    void toDetail(TransportListBean.TransportDataBean item);
    void toFFM(TransportListBean.TransportDataBean item);
}
