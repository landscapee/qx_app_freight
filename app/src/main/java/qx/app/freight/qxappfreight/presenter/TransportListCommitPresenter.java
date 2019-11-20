package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.TransportListCommitEntity;
import qx.app.freight.qxappfreight.contract.TransportListCommitContract;
import qx.app.freight.qxappfreight.model.TransportListCommitModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class TransportListCommitPresenter extends BasePresenter {
    public TransportListCommitPresenter(TransportListCommitContract.transportListCommitView testInfoView) {
        mRequestView = testInfoView;
        mRequestModel = new TransportListCommitModel();
    }

    public void transportListCommit(TransportListCommitEntity transportListCommitEntity) {
        mRequestView.showNetDialog();
        ((TransportListCommitModel) mRequestModel).transportListCommit(transportListCommitEntity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String testInfoListBeans) {
                ((TransportListCommitContract.transportListCommitView) mRequestView).transportListCommitResult(testInfoListBeans);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }
    public void deleteCollectionInfo(String id) {
        mRequestView.showNetDialog();
        ((TransportListCommitModel) mRequestModel).deleteCollectionInfo(id, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((TransportListCommitContract.transportListCommitView) mRequestView).deleteCollectionInfoResult(result);
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
