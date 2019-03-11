package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.ExceptionReportEntity;
import qx.app.freight.qxappfreight.contract.ExceptionReportContract;
import qx.app.freight.qxappfreight.model.ExceptionReportModel;

public class ExceptionReportPresenter extends BasePresenter {
    public ExceptionReportPresenter(ExceptionReportContract.exceptionReportView exceptionReportView) {
        mRequestView = exceptionReportView;
        mRequestModel = new ExceptionReportModel();
    }

    public void exceptionReport(ExceptionReportEntity model) {
        mRequestView.showNetDialog();
        ((ExceptionReportModel) mRequestModel).exceptionReport(model, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((ExceptionReportContract.exceptionReportView) mRequestView).exceptionReportResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }
}
