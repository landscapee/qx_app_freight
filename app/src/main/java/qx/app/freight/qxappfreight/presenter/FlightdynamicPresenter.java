package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightBean;
import qx.app.freight.qxappfreight.contract.FlightdynamicContract;
import qx.app.freight.qxappfreight.model.FlightdynamicModel;

public class FlightdynamicPresenter extends BasePresenter {
    public FlightdynamicPresenter(FlightdynamicContract.flightdynamicView flightdynamicView) {
        mRequestView = flightdynamicView;
        mRequestModel = new FlightdynamicModel();
    }

    public void flightdynamic(BaseFilterEntity model) {
        mRequestView.showNetDialog();
        ((FlightdynamicModel) mRequestModel).flightdynamic(model, new IResultLisenter<FlightBean>() {
            @Override
            public void onSuccess(FlightBean result) {
                ((FlightdynamicContract.flightdynamicView) mRequestView).flightdynamicResult(result);
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
