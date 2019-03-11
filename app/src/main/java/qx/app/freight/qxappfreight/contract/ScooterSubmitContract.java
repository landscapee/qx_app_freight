package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.ScooterSubmitEntity;

public class ScooterSubmitContract {
    public interface scooterSubmitModel {
        void scooterSubmit(ScooterSubmitEntity scooterSubmitEntity, IResultLisenter lisenter);
    }

    public interface scooterSubmitView extends IBaseView {
        void scooterSubmitResult(String result);
    }
}
