package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightInfoBean;
import qx.app.freight.qxappfreight.bean.response.FlightServiceBean;
import qx.app.freight.qxappfreight.contract.FlightInfoContract;
import qx.app.freight.qxappfreight.model.FlightInfoModel;

public class FlightInfoPresenter extends BasePresenter {
    public FlightInfoPresenter(FlightInfoContract.flightInfoView flightInfoView) {
        mRequestView = flightInfoView;
        mRequestModel = new FlightInfoModel();
    }

    public void flightInfo(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((FlightInfoModel) mRequestModel).flightInfo(entity, new IResultLisenter<FlightInfoBean>() {
            @Override
            public void onSuccess(FlightInfoBean result) {
                ((FlightInfoContract.flightInfoView) mRequestView).flightInfoResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }

        });
    }

    public void getMilepostData(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((FlightInfoModel) mRequestModel).getMilepostData(entity, new IResultLisenter<FlightServiceBean>() {
            @Override
            public void onSuccess(FlightServiceBean result) {
                ((FlightInfoContract.flightInfoView) mRequestView).getMilepostDataResult(result);
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
