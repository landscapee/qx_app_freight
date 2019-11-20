package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.LockScooterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightAllReportInfo;

public class GetLastReportInfoContract {
    public interface getLastReportInfoModel {
        void getLastReportInfo(BaseFilterEntity entity, IResultLisenter lisenter);
        void lockOrUnlockScooter(LockScooterEntity lockScooterEntity, IResultLisenter lisenter);
    }

    public interface getLastReportInfoView extends IBaseView {
        void getLastReportInfoResult(List<FlightAllReportInfo> lastReportInfoListBeans);
        void lockOrUnlockScooterResult(String result);
    }
}
