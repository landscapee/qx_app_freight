package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;
import qx.app.freight.qxappfreight.contract.TransportBeginContract;
import qx.app.freight.qxappfreight.model.TransportBeginModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class TransportBeginPresenter extends BasePresenter {
    public TransportBeginPresenter(TransportBeginContract.transportBeginView transportBeginView) {
        mRequestView = transportBeginView;
        mRequestModel = new TransportBeginModel();
    }

    public void transportBegin(TransportEndEntity transportEndEntity) {
        mRequestView.showNetDialog();
        ((TransportBeginModel) mRequestModel).transportBegin(transportEndEntity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((TransportBeginContract.transportBeginView) mRequestView).transportBeginResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }
    public void transportEnd(TransportEndEntity transportEndEntity) {
        mRequestView.showNetDialog();
        ((TransportBeginModel) mRequestModel).transportEnd(transportEndEntity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((TransportBeginContract.transportBeginView) mRequestView).transportEndResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }
    public void scanScooterDelete(TransportEndEntity transportEndEntity) {
        mRequestView.showNetDialog();
        ((TransportBeginModel) mRequestModel).scanScooterDelete(transportEndEntity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((TransportBeginContract.transportBeginView) mRequestView).scanScooterDeleteResult(result);
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
