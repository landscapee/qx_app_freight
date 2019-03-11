package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;
import qx.app.freight.qxappfreight.contract.ArrivalDataSaveContract;
import qx.app.freight.qxappfreight.model.ArrivalDataSaveModel;

public class ArrivalDataSavePresenter extends BasePresenter {
    public ArrivalDataSavePresenter(ArrivalDataSaveContract.arrivalDataSaveView arrivalDataSaveView) {
        mRequestView = arrivalDataSaveView;
        mRequestModel = new ArrivalDataSaveModel();
    }

    public void arrivalDataSave(TransportEndEntity model) {
        mRequestView.showNetDialog();
        ((ArrivalDataSaveModel) mRequestModel).arrivalDataSave(model, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((ArrivalDataSaveContract.arrivalDataSaveView) mRequestView).arrivalDataSaveResult(result);
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
