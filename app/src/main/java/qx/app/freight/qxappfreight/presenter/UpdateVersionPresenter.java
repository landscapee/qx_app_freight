package qx.app.freight.qxappfreight.presenter;


import org.jetbrains.annotations.NotNull;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.UpdateVersionBean;
import qx.app.freight.qxappfreight.contract.UpdateVersionContract;
import qx.app.freight.qxappfreight.model.UpdateVersionModel;

public class UpdateVersionPresenter extends BasePresenter {
    public UpdateVersionPresenter(UpdateVersionContract.updateView mupdataView) {
        mRequestView = mupdataView;
        mRequestModel = new UpdateVersionModel();
    }

    public void updateVersion() {
        mRequestView.showNetDialog();
        ((UpdateVersionModel) mRequestModel).updateVersion(new IResultLisenter<UpdateVersionBean>() {
            @Override
            public void onSuccess(UpdateVersionBean updataBean) {
                ((UpdateVersionContract.updateView) mRequestView).updateVersionResult(updataBean);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(@NotNull String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }
}
