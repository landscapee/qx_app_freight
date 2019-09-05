package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.CargoReportHisBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.contract.BaggageSubHisContract;
import qx.app.freight.qxappfreight.model.BaggageSubHisModel;

public class BaggageSubHisPresenter extends BasePresenter {
    public BaggageSubHisPresenter(BaggageSubHisContract.baggageSubHisView addScooterView) {
        mRequestView = addScooterView;
        mRequestModel = new BaggageSubHisModel();
    }

    public void baggageSubHis(BaseFilterEntity operatorId) {
        mRequestView.showNetDialog();
        ((BaggageSubHisModel) mRequestModel).baggageSubHis(operatorId, new IResultLisenter<List<CargoReportHisBean>>() {
            @Override
            public void onSuccess(List<CargoReportHisBean> result) {
                ((BaggageSubHisContract.baggageSubHisView) mRequestView).baggageSubHisResult(result);
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
