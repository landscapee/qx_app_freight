package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightLuggageBean;

public class LookLUggageScannigFlightContract {

    public interface lookLUggageScannigFlightModel {


        void getDepartureFlightByAndroid(BaseFilterEntity model, IResultLisenter lisenter);
    }

    public interface lookLUggageScannigFlightView extends IBaseView {


        void getDepartureFlightByAndroidResult(List<FlightLuggageBean> flightLuggageBeans);
    }
}
