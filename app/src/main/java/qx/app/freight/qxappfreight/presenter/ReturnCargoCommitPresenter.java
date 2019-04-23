package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.TransportListCommitEntity;
import qx.app.freight.qxappfreight.contract.ReturnCargoCommitContract;
import qx.app.freight.qxappfreight.model.ReturnCargoCommitModel;

public class ReturnCargoCommitPresenter extends BasePresenter {
    public ReturnCargoCommitPresenter(ReturnCargoCommitContract.returnCargoCommitView returnCargoCommitView) {
        mRequestView = returnCargoCommitView;
        mRequestModel = new ReturnCargoCommitModel();
    }

    public void returnCargoCommit(TransportListCommitEntity transportListCommitEntity) {
        mRequestView.showNetDialog();
        ((ReturnCargoCommitModel) mRequestModel).returnCargoCommit(transportListCommitEntity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String testInfoListBeans) {
                ((ReturnCargoCommitContract.returnCargoCommitView) mRequestView).returnCargoCommitResult(testInfoListBeans);
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
