package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.ChangeWaybillEntity;
import qx.app.freight.qxappfreight.bean.request.DeclareWaybillEntity;
import qx.app.freight.qxappfreight.bean.response.DeclareWaybillBean;
import qx.app.freight.qxappfreight.contract.DeliveryVerifyContract;
import qx.app.freight.qxappfreight.model.DeliveryVerifyModel;

public class DeliveryVerifyPresenter extends BasePresenter {

    public DeliveryVerifyPresenter(DeliveryVerifyContract.deliveryVerifyView deliveryVerifyView){
        mRequestView = deliveryVerifyView;
        mRequestModel = new DeliveryVerifyModel();
    }

    public void getDeliveryVerify(DeclareWaybillEntity declareWaybillEntity){
        mRequestView.showNetDialog();
        ((DeliveryVerifyModel)mRequestModel).deliveryVerify(declareWaybillEntity, new IResultLisenter<DeclareWaybillBean>() {
            @Override
            public void onSuccess(DeclareWaybillBean result) {
                ((DeliveryVerifyContract.deliveryVerifyView)mRequestView).deliveryVerifyResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void changeSubmit(ChangeWaybillEntity changeWaybillEntity){
        mRequestView.showNetDialog();
        ((DeliveryVerifyModel)mRequestModel).changeSubmit(changeWaybillEntity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String o) {
                ((DeliveryVerifyContract.deliveryVerifyView)mRequestView).changeSubmitResult(o);
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
