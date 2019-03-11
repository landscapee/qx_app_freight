package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.ScooterSubmitEntity;
import qx.app.freight.qxappfreight.contract.ScooterSubmitContract;
import qx.app.freight.qxappfreight.model.ScooterSubmitModel;

public class ScooterSubmitPresenter extends BasePresenter {
    public ScooterSubmitPresenter(ScooterSubmitContract.scooterSubmitView scooterSubmitView) {
        mRequestView = scooterSubmitView;
        mRequestModel = new ScooterSubmitModel();
    }

    public void scooterSubmit(ScooterSubmitEntity model) {
        mRequestView.showNetDialog();
        ((ScooterSubmitModel) mRequestModel).scooterSubmit(model, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((ScooterSubmitContract.scooterSubmitView) mRequestView).scooterSubmitResult(result);
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
