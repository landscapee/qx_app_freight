package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightAllReportInfo;

public class GetFlightAllReportInfoContract {
    public interface getFlightAllReportInfoModel {
        void getFlightAllReportInfo(BaseFilterEntity entity, IResultLisenter lisenter);
        void reOpenLoadTask(BaseFilterEntity entity, IResultLisenter lisenter);
    }

    public interface getFlightAllReportInfoView extends IBaseView {
        void getFlightAllReportInfoResult(List<FlightAllReportInfo> flightAllReportInfos);
        void reOpenLoadTaskResult(String result);
    }
}
