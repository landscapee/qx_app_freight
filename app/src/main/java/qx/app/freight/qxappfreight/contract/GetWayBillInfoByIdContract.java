package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.DeclareWaybillBean;

public class GetWayBillInfoByIdContract {
    public interface getWayBillInfoByIdModel {
        void getWayBillInfoById(String id, IResultLisenter lisenter);
        void sendPrintMessage(String waybillId, IResultLisenter lisenter);
    }

    public interface getWayBillInfoByIdView extends IBaseView {
        void getWayBillInfoByIdResult(DeclareWaybillBean result);
        void sendPrintMessageResult(String result);
    }
}
