package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.ExceptionReportEntity;
import qx.app.freight.qxappfreight.contract.PullGoodsReportContract;
import qx.app.freight.qxappfreight.model.PullGoodsReportModel;

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


}
