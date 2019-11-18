package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.FlightPhotoEntity;

public class UpLoadFlightPhotoContract {
    public interface uploadFlightPhotoModel {
        void uploadFlightPhoto(FlightPhotoEntity flightPhotoEntity, IResultLisenter lisenter);
    }

    public interface uploadFlightPhotoView extends IBaseView {
        void uploadFlightPhotoResult(String result);
    }
}
