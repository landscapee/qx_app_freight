package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
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
        ((BaggageAreaSubModel) mRequestModel).scooterInfoList(model, new IResultLisenter<List<ScooterInfoListBean>>() {
            @Override
            public void onSuccess(List<ScooterInfoListBean> scooterInfoListBeans) {
                ((BaggageAreaSubContract.baggageAreaSubView) mRequestView).scooterInfoListResult(scooterInfoListBeans);
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
