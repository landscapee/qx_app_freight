package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ArrivalDeliveryInfoBean;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;
import qx.app.freight.qxappfreight.contract.ArrivalDeliveryInfoContract;
import qx.app.freight.qxappfreight.model.ArrivalDeliveryInfoModel;

public class ArrivalDeliveryInfoPresenter extends BasePresenter {
    public ArrivalDeliveryInfoPresenter(ArrivalDeliveryInfoContract.arrivalDeliveryInfoView arrivalDeliveryInfoView) {
        mRequestView = arrivalDeliveryInfoView;
        mRequestModel = new ArrivalDeliveryInfoModel();
    }

    public void arrivalDataSave(BaseFilterEntity model) {
        mRequestView.showNetDialog();
        ((ArrivalDeliveryInfoModel) mRequestModel).arrivalDeliveryInfo(model, new IResultLisenter<ArrivalDeliveryInfoBean>() {
            @Override
            public void onSuccess(ArrivalDeliveryInfoBean result) {
                ((ArrivalDeliveryInfoContract.arrivalDeliveryInfoView) mRequestView).arrivalDeliveryInfoResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void deliveryInWaybill(BaseFilterEntity model) {
        mRequestView.showNetDialog();
        ((ArrivalDeliveryInfoModel) mRequestModel).deliveryInWaybill(model, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((ArrivalDeliveryInfoContract.arrivalDeliveryInfoView) mRequestView).deliveryInWaybillResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void completDelivery(BaseFilterEntity model) {
        mRequestView.showNetDialog();
        ((ArrivalDeliveryInfoModel) mRequestModel).completDelivery(model, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((ArrivalDeliveryInfoContract.arrivalDeliveryInfoView) mRequestView).completDeliveryResult(result);
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
