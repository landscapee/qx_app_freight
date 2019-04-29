package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.LoadingListOverBean;
import qx.app.freight.qxappfreight.bean.response.GetFlightCargoResBean;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;
import qx.app.freight.qxappfreight.contract.GetFlightCargoResContract;
import qx.app.freight.qxappfreight.model.GetFlightCargoResModel;

public class GetFlightCargoResPresenter extends BasePresenter {

    public GetFlightCargoResPresenter(GetFlightCargoResContract.getFlightCargoResView getFlightCargoResView) {
        mRequestView = getFlightCargoResView;
        mRequestModel = new GetFlightCargoResModel();
    }

    public void getFlightCargoRes(String id) {
        mRequestView.showNetDialog();
        ((GetFlightCargoResModel) mRequestModel).getFlightCargoRes(id, new IResultLisenter<List<LoadingListBean>>() {
            @Override
            public void onSuccess(List<LoadingListBean> getFlightCargoResBeans) {
                ((GetFlightCargoResContract.getFlightCargoResView) mRequestView).getFlightCargoResResult(getFlightCargoResBeans);
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
    public void overLoad(LoadingListOverBean entity) {
        mRequestView.showNetDialog();
        ((GetFlightCargoResModel) mRequestModel).overLoad(entity, new IResultLisenter<String>() {
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
}
