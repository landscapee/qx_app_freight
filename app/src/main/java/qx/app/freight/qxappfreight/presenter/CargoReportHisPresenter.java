package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.CargoReportHisBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.contract.CargoReportHisContract;
import qx.app.freight.qxappfreight.model.CargoReportHisModel;

public class CargoReportHisPresenter extends BasePresenter {

    public CargoReportHisPresenter(CargoReportHisContract.cargoReportHisView addScooterView) {
        mRequestView = addScooterView;
        mRequestModel = new CargoReportHisModel();
    }

    public void cargoReportHis(String operatorId) {
        mRequestView.showNetDialog();
        ((CargoReportHisModel) mRequestModel).cargoReportHis(operatorId, new IResultLisenter<List<CargoReportHisBean>>() {
            @Override
            public void onSuccess(List<CargoReportHisBean> result) {
                ((CargoReportHisContract.cargoReportHisView) mRequestView).cargoReportHisResult(result);
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
