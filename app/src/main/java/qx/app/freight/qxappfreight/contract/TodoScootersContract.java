package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.TodoScootersEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.FlightInfoAndScootersBean;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.model.ManifestBillModel;

public class TodoScootersContract {
    public interface todoScootersModel {
        void todoScooters(TodoScootersEntity todoScootersEntity, IResultLisenter lisenter);
        void getManifest(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter);
        void returnGroupScooterTask(GetInfosByFlightIdBean scooter,IResultLisenter lisenter);
    }

    public interface todoScootersView extends IBaseView {
        void todoScootersResult(FlightInfoAndScootersBean result);
        void getManifestResult(List<ManifestBillModel> result);
        void returnGroupScooterTaskResult(BaseEntity <Object> scooter);
    }
}
