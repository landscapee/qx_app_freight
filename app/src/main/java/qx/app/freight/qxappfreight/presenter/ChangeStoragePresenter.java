package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.ChangeStorageBean;
import qx.app.freight.qxappfreight.contract.ChangeStorageContract;
import qx.app.freight.qxappfreight.model.ChangeStorageModel;

public class ChangeStoragePresenter extends BasePresenter {
    public ChangeStoragePresenter(ChangeStorageContract.changeStorageView changeStorageView) {
        mRequestView = changeStorageView;
        mRequestModel = new ChangeStorageModel();
    }

    public void changeStorage(ChangeStorageBean entity) {
        mRequestView.showNetDialog();
        ((ChangeStorageModel) mRequestModel).changeStorage(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((ChangeStorageContract.changeStorageView) mRequestView).changeStorageResult(result);
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
