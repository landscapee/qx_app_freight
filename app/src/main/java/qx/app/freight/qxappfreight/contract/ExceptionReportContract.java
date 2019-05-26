package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.ExceptionReportEntity;

public class ExceptionReportContract {
    public interface exceptionReportModel {
        void exceptionReport(ExceptionReportEntity exceptionReportEntity, IResultLisenter lisenter);
        void exceptionTpEnd(ExceptionReportEntity exceptionReportEntity, IResultLisenter lisenter);
    }

    public interface exceptionReportView extends IBaseView {
        void exceptionReportResult(String result);
        void exceptionTpEndResult(String result);
    }
}
