package qx.app.freight.qxappfreight.listener;

import qx.app.freight.qxappfreight.bean.response.TransportDataBase;

public interface InportTallyInterface {
    void toDetail(TransportDataBase item);
    void toFFM(TransportDataBase item);
}
