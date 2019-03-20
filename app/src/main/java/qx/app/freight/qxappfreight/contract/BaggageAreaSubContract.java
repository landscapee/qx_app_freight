package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;

public class BaggageAreaSubContract {
    public interface baggageAreaSubModel {
        void baggageAreaSub(String model, IResultLisenter lisenter);
        void scooterInfoList(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter);
        void lookLUggageScannigFlight(BaseFilterEntity model, IResultLisenter lisenter);
    }

    public interface baggageAreaSubView extends IBaseView {
        void scooterInfoListResult(List<ScooterInfoListBean> scooterInfoListBeans);
        void baggageAreaSubResult(String result);
        void lookLUggageScannigFlightResult(String result);
    }
}
