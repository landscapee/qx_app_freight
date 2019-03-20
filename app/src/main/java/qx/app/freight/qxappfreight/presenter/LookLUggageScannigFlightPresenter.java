package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightLuggageBean;
import qx.app.freight.qxappfreight.contract.LookLUggageScannigFlightContract;
import qx.app.freight.qxappfreight.model.LookLUggageScannigFlightModel;

public class LookLUggageScannigFlightPresenter extends BasePresenter {

    public LookLUggageScannigFlightPresenter(LookLUggageScannigFlightContract.lookLUggageScannigFlightView lookLUggageScannigFlightView) {
        mRequestView = lookLUggageScannigFlightView;
        mRequestModel = new LookLUggageScannigFlightModel();
    }



    public void getDepartureFlightByAndroid(BaseFilterEntity model) {
        mRequestView.showNetDialog();
        ((LookLUggageScannigFlightModel) mRequestModel).getDepartureFlightByAndroid(model, new IResultLisenter<List<FlightLuggageBean>>() {
            @Override
            public void onSuccess(List<FlightLuggageBean> result) {
                ((LookLUggageScannigFlightContract.lookLUggageScannigFlightView) mRequestView).getDepartureFlightByAndroidResult(result);
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
