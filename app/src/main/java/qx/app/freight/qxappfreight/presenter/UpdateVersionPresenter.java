package qx.app.freight.qxappfreight.presenter;


import java.util.Map;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.contract.UpdateVersionContract;
import qx.app.freight.qxappfreight.model.UpdateVersionModel;

public class UpdateVersionPresenter extends BasePresenter {
    public UpdateVersionPresenter(UpdateVersionContract.updateView mupdataView) {
        mRequestView = mupdataView;
        mRequestModel = new UpdateVersionModel();
    }

    public void updateVersion(Map<String,String> params) {
//        mRequestView.showNetDialog();
//        ((UpdateVersionModel) mRequestModel).updateVersion(params,new IResultLisenter<UpdateVersionBean>() {
//            @Override
//            public void onSuccess(UpdateVersionBean updataBean) {
//                ((UpdateVersionContract.updateView) mRequestView).updateVersionResult(updataBean);
//                mRequestView.dissMiss();
//            }
//
//            @Override
//            public void onFail(@NotNull String error) {
//                mRequestView.toastView(error);
//                mRequestView.dissMiss();
//            }
//        });
    }
}
