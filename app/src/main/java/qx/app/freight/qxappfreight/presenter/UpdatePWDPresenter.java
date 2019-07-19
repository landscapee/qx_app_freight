package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.UpdatePwdEntity;
import qx.app.freight.qxappfreight.contract.UpdatePWDContract;
import qx.app.freight.qxappfreight.model.UpdatePWDModel;

public class UpdatePWDPresenter extends BasePresenter {

    public UpdatePWDPresenter(UpdatePWDContract.updatePWDView updatePWDView) {
        mRequestView = updatePWDView;
        mRequestModel = new UpdatePWDModel();
    }

    public void updatePWD(UpdatePwdEntity entity) {
        mRequestView.showNetDialog();
        ((UpdatePWDModel) mRequestModel).updatePWD(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((UpdatePWDContract.updatePWDView) mRequestView).updatePWDResult(result);
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
