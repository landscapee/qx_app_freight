package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.contract.ScanScooterContract;
import qx.app.freight.qxappfreight.model.ScanScooterModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class ScanScooterPresenter extends BasePresenter {
    public ScanScooterPresenter(ScanScooterContract.scanScooterView scanScooterView) {
        mRequestView = scanScooterView;
        mRequestModel = new ScanScooterModel();
    }

    public void scanScooter(TransportTodoListBean transportEndEntity) {
        mRequestView.showNetDialog();
        ((ScanScooterModel) mRequestModel).scanScooter(transportEndEntity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((ScanScooterContract.scanScooterView) mRequestView).scanScooterResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }
    public void scanLockScooter(TransportEndEntity transportEndEntity) {
        mRequestView.showNetDialog();
        ((ScanScooterModel) mRequestModel).scanLockScooter(transportEndEntity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((ScanScooterContract.scanScooterView) mRequestView).scanLockScooterResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void scooterWithUser(String user) {
        mRequestView.showNetDialog();
        ((ScanScooterModel) mRequestModel).scooterWithUser(user, new IResultLisenter<List<TransportTodoListBean>>() {
            @Override
            public void onSuccess(List<TransportTodoListBean> result) {
                ((ScanScooterContract.scanScooterView) mRequestView).scooterWithUserResult(result);
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
