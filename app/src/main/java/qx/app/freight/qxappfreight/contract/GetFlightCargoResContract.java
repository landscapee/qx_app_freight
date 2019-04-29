package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.LoadingListOverBean;
import qx.app.freight.qxappfreight.bean.response.GetFlightCargoResBean;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;

public class GetFlightCargoResContract {
    public interface getFlightCargoResModel {
        void getFlightCargoRes(String flightid, IResultLisenter lisenter);
        void flightDoneInstall(GetFlightCargoResBean entity,IResultLisenter lisenter);
        void overLoad(LoadingListOverBean entity, IResultLisenter lisenter);
    }

    public interface getFlightCargoResView extends IBaseView {
        void getFlightCargoResResult(List<LoadingListBean> getFlightCargoResBeanList);
        void flightDoneInstallResult(String result);
        void overLoadResult(String result);
    }

}
