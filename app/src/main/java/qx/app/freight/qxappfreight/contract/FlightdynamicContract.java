package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightBean;

public class FlightdynamicContract {
    public interface flightdynamicModel {
        void flightdynamic(BaseFilterEntity entity, IResultLisenter lisenter);
    }

    public interface flightdynamicView extends IBaseView {
        void flightdynamicResult(FlightBean result);
    }
}
