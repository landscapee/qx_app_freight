package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.GetFlightCargoResBean;

public class GetFlightCargoResContract {
    public interface getFlightCargoResModel {
        void getFlightCargoRes(String flightid, IResultLisenter lisenter);
        void flightDoneInstall(GetFlightCargoResBean entity,IResultLisenter lisenter);
    }

    public interface getFlightCargoResView extends IBaseView {
        void getFlightCargoResResult(List<GetFlightCargoResBean> getFlightCargoResBeanList);
        void flightDoneInstallResult(String result);
    }

}
