package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.GetWaybillInfoByIdDataBean;

public class GetWayBillInfoByIdDataContract {
    public interface getWayBillInfoByIdDataModel {
        void getWayBillInfoByIdData(String waybillCode, IResultLisenter lisenter);
        void getWaybillInfo(String id, IResultLisenter lisenter);

    }

    public interface getWayBillInfoByIdDataView extends IBaseView {
        void getWayBillInfoByIdDataResult(GetWaybillInfoByIdDataBean addScooterBean);
        void getWaybillInfoResult(List<GetWaybillInfoByIdDataBean> addScooterBean);
    }
}
