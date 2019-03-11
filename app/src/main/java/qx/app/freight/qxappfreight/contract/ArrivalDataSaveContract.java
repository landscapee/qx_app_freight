package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;

public class ArrivalDataSaveContract {
    public interface arrivalDataSaveModel {
        void arrivalDataSave(TransportEndEntity transportEndEntity, IResultLisenter lisenter);
    }

    public interface arrivalDataSaveView extends IBaseView {
        void arrivalDataSaveResult(String result);
    }
}
