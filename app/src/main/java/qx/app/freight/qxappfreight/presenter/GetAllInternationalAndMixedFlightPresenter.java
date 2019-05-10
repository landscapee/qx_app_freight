package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightLuggageBean;
import qx.app.freight.qxappfreight.contract.GetAllInternationalAndMixedFlightContract;
import qx.app.freight.qxappfreight.model.GetAllInternationalAndMixedFlightModel;

public class GetAllInternationalAndMixedFlightPresenter extends BasePresenter {
    public GetAllInternationalAndMixedFlightPresenter(GetAllInternationalAndMixedFlightContract.getAllInternationalAndMixedFlightView getAllInternationalAndMixedFlightView) {
        mRequestView = getAllInternationalAndMixedFlightView;
        mRequestModel = new GetAllInternationalAndMixedFlightModel();
    }

    public void addScooter(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((GetAllInternationalAndMixedFlightModel) mRequestModel).getAllInternationalAndMixedFlight(entity, new IResultLisenter<List<FlightLuggageBean>>() {
            @Override
            public void onSuccess(List<FlightLuggageBean> result) {
                ((GetAllInternationalAndMixedFlightContract.getAllInternationalAndMixedFlightView) mRequestView).getAllInternationalAndMixedFlightResult(result);
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
