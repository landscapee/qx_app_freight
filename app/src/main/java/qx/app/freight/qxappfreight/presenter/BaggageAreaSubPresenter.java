package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.contract.BaggageAreaSubContract;
import qx.app.freight.qxappfreight.model.BaggageAreaSubModel;

public class BaggageAreaSubPresenter extends BasePresenter {
    public BaggageAreaSubPresenter(BaggageAreaSubContract.baggageAreaSubView baggageAreaSubView) {
        mRequestView = baggageAreaSubView;
        mRequestModel = new BaggageAreaSubModel();
    }

    public void baggageAreaSub(BaseFilterEntity model) {
        mRequestView.showNetDialog();
        ((BaggageAreaSubModel) mRequestModel).baggageAreaSub(model, new IResultLisenter<String>() {
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
}
