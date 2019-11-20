package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.contract.DeleteCollectionInfoContract;
import qx.app.freight.qxappfreight.model.DeleteCollectionInfoModel;

public class DeleteCollectionInfoPresenter extends BasePresenter {
    public DeleteCollectionInfoPresenter(DeleteCollectionInfoContract.deleteCollectionInfoView deleteCollectionInfoView) {
        mRequestView = deleteCollectionInfoView;
        mRequestModel = new DeleteCollectionInfoModel();
    }

//    public void deleteCollectionInfo(String id) {
//        mRequestView.showNetDialog();
//        ((DeleteCollectionInfoModel) mRequestModel).deleteCollectionInfo(id, new IResultLisenter<String>() {
//            @Override
//            public void onSuccess(String result) {
//                ((DeleteCollectionInfoContract.deleteCollectionInfoView) mRequestView).deleteCollectionInfoResult(result);
//                mRequestView.dissMiss();
//            }
//
//            @Override
//            public void onFail(String error) {
//                mRequestView.toastView(error);
//                mRequestView.dissMiss();
//            }
//        });
//    }
}
