package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.TodoScootersEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.FlightInfoAndScootersBean;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.contract.TodoScootersContract;
import qx.app.freight.qxappfreight.model.ManifestBillModel;
import qx.app.freight.qxappfreight.model.TodoScootersModel;

public class TodoScootersPresenter extends BasePresenter {

    public TodoScootersPresenter(TodoScootersContract.todoScootersView todoScootersView) {
        mRequestView = todoScootersView;
        mRequestModel = new TodoScootersModel();
    }

    public void todoScooters(TodoScootersEntity model) {
        mRequestView.showNetDialog();
        ((TodoScootersModel) mRequestModel).todoScooters(model, new IResultLisenter <FlightInfoAndScootersBean>() {
            @Override
            public void onSuccess(FlightInfoAndScootersBean airlineRequireBeans) {
                ((TodoScootersContract.todoScootersView) mRequestView).todoScootersResult(airlineRequireBeans);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void getManifest(BaseFilterEntity model) {
        mRequestView.showNetDialog();
        ((TodoScootersModel) mRequestModel).getManifest(model, new IResultLisenter <List <ManifestBillModel>>() {
            @Override
            public void onSuccess(List <ManifestBillModel> airlineRequireBeans) {
                ((TodoScootersContract.todoScootersView) mRequestView).getManifestResult(airlineRequireBeans);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    /**
     * 退回组板
     *
     * @param scooter
     */
    public void returnGroupScooterTask(GetInfosByFlightIdBean scooter) {
        mRequestView.showNetDialog();
        ((TodoScootersModel) mRequestModel).returnGroupScooterTask(scooter, new IResultLisenter <BaseEntity>() {
            @Override
            public void onSuccess(BaseEntity airlineRequireBeans) {
                ((TodoScootersContract.todoScootersView) mRequestView).returnGroupScooterTaskResult(airlineRequireBeans);
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
