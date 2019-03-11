package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.GpsInfoEntity;

public class SaveGpsInfoContract {
    public interface saveGpsInfoModel {
        void saveGpsInfo(GpsInfoEntity gpsInfoEntity, IResultLisenter lisenter);
    }

    public interface saveGpsInfoView extends IBaseView {
        void saveGpsInfoResult(String result);
    }
}
