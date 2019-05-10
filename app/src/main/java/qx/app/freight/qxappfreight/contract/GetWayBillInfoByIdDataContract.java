package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.GetWaybillInfoByIdDataBean;

public class GetWayBillInfoByIdDataContract {
    public interface getWayBillInfoByIdDataModel {
        void getWayBillInfoByCode(String waybillCode, IResultLisenter lisenter);
//        void getWaybillInfo(String id, IResultLisenter lisenter);

    }

    public interface getWayBillInfoByIdDataView extends IBaseView {
        void getWayBillInfoByCodeResult(GetWaybillInfoByIdDataBean addScooterBean);
//        void getWaybillInfoResult(List<GetWaybillInfoByIdDataBean> addScooterBean);
    }
}
