package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.PhoneParametersEntity;
import qx.app.freight.qxappfreight.bean.response.FlightBean;

public class GetPhoneParametersContract {
    public interface getPhoneParametersModel {
        void getPhoneParameters(PhoneParametersEntity entity, IResultLisenter lisenter);
    }

    public interface getPhoneParametersView extends IBaseView {
        void getPhoneParametersResult(FlightBean result);
    }
}
