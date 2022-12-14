package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.FlightIdBean;
import qx.app.freight.qxappfreight.bean.request.LoadingListRequestEntity;
import qx.app.freight.qxappfreight.bean.request.LoadingListSendEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.CargoCabinData;
import qx.app.freight.qxappfreight.bean.response.GetFlightCargoResBean;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;

public class GetFlightCargoResContract {
    public interface getFlightCargoResModel {
        void getLoadingList(LoadingListRequestEntity entity, IResultLisenter lisenter);

        void getFlightSpace(FlightIdBean entity, IResultLisenter lisenter);

        void flightDoneInstall(GetFlightCargoResBean entity, IResultLisenter lisenter);

        void overLoad(LoadingListSendEntity entity, IResultLisenter lisenter);

        void confirmLoadPlan(BaseFilterEntity taskClearEntity, IResultLisenter lisenter);

        void getPullStatus(BaseFilterEntity taskClearEntity, IResultLisenter lisenter);
    }

    public interface getFlightCargoResView extends IBaseView {
        void getLoadingListResult(LoadingListBean result);

        void setFlightSpace(CargoCabinData result);

        void flightDoneInstallResult(String result);

        void overLoadResult(String result);

        void confirmLoadPlanResult(String result);

        void getPullStatusResult(BaseEntity<String> result);
    }
}
