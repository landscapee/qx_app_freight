package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListDataBean;
import qx.app.freight.qxappfreight.contract.BaggageAreaSubContract;
import qx.app.freight.qxappfreight.model.BaggageAreaSubModel;

public class BaggageAreaSubPresenter extends BasePresenter {
    public BaggageAreaSubPresenter(BaggageAreaSubContract.baggageAreaSubView baggageAreaSubView) {
        mRequestView = baggageAreaSubView;
        mRequestModel = new BaggageAreaSubModel();
    }

    public void baggageAreaSub(String list) {
        mRequestView.showNetDialog();
        ((BaggageAreaSubModel) mRequestModel).baggageAreaSub(list, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((BaggageAreaSubContract.baggageAreaSubView) mRequestView).baggageAreaSubResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void ScooterInfoList(BaseFilterEntity model) {
        mRequestView.showNetDialog();
        ((BaggageAreaSubModel) mRequestModel).scooterInfoList(model, new IResultLisenter<ScooterInfoListDataBean>() {
            @Override
            public void onSuccess(ScooterInfoListDataBean scooterInfoListBeans) {
                ((BaggageAreaSubContract.baggageAreaSubView) mRequestView).scooterInfoListResult(scooterInfoListBeans.getRecords());
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }
    public void lookLUggageScannigFlight(BaseFilterEntity model) {
        mRequestView.showNetDialog();
        ((BaggageAreaSubModel) mRequestModel).lookLUggageScannigFlight(model, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((BaggageAreaSubContract.baggageAreaSubView) mRequestView).lookLUggageScannigFlightResult(result);
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
