package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightInfoBean;
import qx.app.freight.qxappfreight.bean.response.FlightServiceBean;

public class FlightInfoContract {
    public interface flightInfoModel {
        void flightInfo(BaseFilterEntity model, IResultLisenter lisenter);

        void getMilepostData(BaseFilterEntity model, IResultLisenter lisenter);
    }

    public interface flightInfoView extends IBaseView {
        void flightInfoResult(FlightInfoBean flightInfoBean);

        void getMilepostDataResult(FlightServiceBean flightServiceBean);
    }

}
