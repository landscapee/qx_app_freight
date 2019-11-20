package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.CargoReportHisBean;

public class BaggageSubHisContract {
    public interface baggageSubHisModel {
        void baggageSubHis(BaseFilterEntity operatorId, IResultLisenter lisenter);
    }

    public interface baggageSubHisView extends IBaseView {
        void baggageSubHisResult(List<CargoReportHisBean> cargoReportHisBeans);
    }
}
