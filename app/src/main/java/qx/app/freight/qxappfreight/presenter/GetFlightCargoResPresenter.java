package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.LoadingListRequestEntity;
import qx.app.freight.qxappfreight.bean.request.LoadingListSendEntity;
import qx.app.freight.qxappfreight.bean.response.GetFlightCargoResBean;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;
import qx.app.freight.qxappfreight.contract.GetFlightCargoResContract;
import qx.app.freight.qxappfreight.model.GetFlightCargoResModel;

public class GetFlightCargoResPresenter extends BasePresenter {

    public GetFlightCargoResPresenter(GetFlightCargoResContract.getFlightCargoResView getFlightCargoResView) {
        mRequestView = getFlightCargoResView;
        mRequestModel = new GetFlightCargoResModel();
    }

    public void getLoadingList(LoadingListRequestEntity entity) {
        mRequestView.showNetDialog();
        ((GetFlightCargoResModel) mRequestModel).getLoadingList(entity, new IResultLisenter<LoadingListBean>() {
            @Override
            public void onSuccess(LoadingListBean result) {
                ((GetFlightCargoResContract.getFlightCargoResView) mRequestView).getLoadingListResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void flightDoneInstall(GetFlightCargoResBean entity) {
        mRequestView.showNetDialog();
        ((GetFlightCargoResModel) mRequestModel).flightDoneInstall(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String string) {
                ((GetFlightCargoResContract.getFlightCargoResView) mRequestView).flightDoneInstallResult(string);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void overLoad(LoadingListSendEntity entity) {
        mRequestView.showNetDialog();
        ((GetFlightCargoResModel) mRequestModel).overLoad(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String string) {
                ((GetFlightCargoResContract.getFlightCargoResView) mRequestView).overLoadResult(string);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void confirmLoadPlan(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((GetFlightCargoResModel) mRequestModel).confirmLoadPlan(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((GetFlightCargoResContract.getFlightCargoResView) mRequestView).confirmLoadPlanResult(result);
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
