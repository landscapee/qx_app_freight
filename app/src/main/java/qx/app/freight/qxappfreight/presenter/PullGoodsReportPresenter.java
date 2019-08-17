package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.PullGoodsInfoBean;
import qx.app.freight.qxappfreight.contract.PullGoodsReportContract;
import qx.app.freight.qxappfreight.model.PullGoodsReportModel;

public class PullGoodsReportPresenter extends BasePresenter {

    public PullGoodsReportPresenter(PullGoodsReportContract.pullGoodsView loginView) {
        mRequestView = loginView;
        mRequestModel = new PullGoodsReportModel();
    }

    public void getPullGoodsInfo(String flightInfoId) {
        mRequestView.showNetDialog();
        ((PullGoodsReportModel) mRequestModel).getPullGoodsInfo(flightInfoId, new IResultLisenter<PullGoodsInfoBean>() {
            @Override
            public void onSuccess(PullGoodsInfoBean result) {
                ((PullGoodsReportContract.pullGoodsView) mRequestView).getPullGoodsInfoResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void pullGoodsInfoCommit(PullGoodsInfoBean entity) {
        mRequestView.showNetDialog();
        ((PullGoodsReportModel) mRequestModel).pullGoodsInfoCommit(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((PullGoodsReportContract.pullGoodsView) mRequestView).pullGoodsInfoCommitResult(result);
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
