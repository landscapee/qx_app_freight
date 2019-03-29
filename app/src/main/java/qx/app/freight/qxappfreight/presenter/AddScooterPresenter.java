package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.AddScooterBean;
import qx.app.freight.qxappfreight.contract.AddScooterContract;
import qx.app.freight.qxappfreight.model.AddScooterModel;

public class AddScooterPresenter extends BasePresenter {
    public AddScooterPresenter(AddScooterContract.addScooterView addScooterView) {
        mRequestView = addScooterView;
        mRequestModel = new AddScooterModel();
    }

    public void addScooter() {
        mRequestView.showNetDialog();
        ((AddScooterModel) mRequestModel).addScooter(new IResultLisenter<AddScooterBean>() {
            @Override
            public void onSuccess(AddScooterBean result) {
                ((AddScooterContract.addScooterView) mRequestView).addScooterResult(result);
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
