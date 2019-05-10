package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightLuggageBean;

public class GetAllInternationalAndMixedFlightContract {
    public interface getAllInternationalAndMixedFlightModel {
        void getAllInternationalAndMixedFlight(BaseFilterEntity entity, IResultLisenter lisenter);
    }

    public interface getAllInternationalAndMixedFlightView extends IBaseView {
        void getAllInternationalAndMixedFlightResult(List<FlightLuggageBean> flightLuggageBeans);
    }
}
