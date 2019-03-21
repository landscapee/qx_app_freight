package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.ExceptionReportEntity;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.contract.PullGoodsReportContract;
import qx.app.freight.qxappfreight.contract.ScanScooterContract;
import qx.app.freight.qxappfreight.model.PullGoodsReportModel;
import qx.app.freight.qxappfreight.model.ScanScooterModel;

public class PullGoodsReportPresenter extends BasePresenter {

    public PullGoodsReportPresenter(PullGoodsReportContract.pullGoodsView loginView) {
        mRequestView = loginView;
        mRequestModel = new PullGoodsReportModel();
    }

    public void pullGoodsReport(ExceptionReportEntity entity) {
        mRequestView.showNetDialog();
        ((PullGoodsReportModel) mRequestModel).pullGoodsReport(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((PullGoodsReportContract.pullGoodsView) mRequestView).pullGoodsReportResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }
    public void scanScooterDelete(TransportEndEntity endEntity) {
        mRequestView.showNetDialog();
        ((PullGoodsReportModel) mRequestModel).scanScooterDelete(endEntity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((PullGoodsReportContract.pullGoodsView) mRequestView).scanScooterDeleteResult(result);
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
