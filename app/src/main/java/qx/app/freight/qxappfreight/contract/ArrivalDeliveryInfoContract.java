package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ArrivalDeliveryInfoBean;

public class ArrivalDeliveryInfoContract {
    public interface arrivalDeliveryInfoModel {
        void arrivalDeliveryInfo(BaseFilterEntity entity, IResultLisenter lisenter);

        void deliveryInWaybill(List <BaseFilterEntity> entity, IResultLisenter lisenter);

        void completDelivery(BaseFilterEntity entity, IResultLisenter lisenter);

    }

    public interface arrivalDeliveryInfoView extends IBaseView {
        void arrivalDeliveryInfoResult(ArrivalDeliveryInfoBean result);

        void deliveryInWaybillResult(String result);

        void completDeliveryResult(String result);
    }
}
