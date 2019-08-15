package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.contract.TransportTaskHisContract;
import qx.app.freight.qxappfreight.model.TransportTaskHisModel;

public class TransportTaskHisPresenter extends BasePresenter {

    public TransportTaskHisPresenter(TransportTaskHisContract.transportTaskHisView addScooterView) {
        mRequestView = addScooterView;
        mRequestModel = new TransportTaskHisModel();
    }

    public void transportTaskHis(String operatorId) {
        mRequestView.showNetDialog();
        ((TransportTaskHisModel) mRequestModel).transportTaskHis(operatorId, new IResultLisenter<List<TransportTodoListBean>>() {
            @Override
            public void onSuccess(List<TransportTodoListBean> result) {
                ((TransportTaskHisContract.transportTaskHisView) mRequestView).transportTaskHisResult(result);
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
