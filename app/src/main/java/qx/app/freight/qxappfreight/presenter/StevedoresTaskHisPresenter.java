package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.contract.StevedoresTaskHisContract;
import qx.app.freight.qxappfreight.model.StevedoresTaskHisModel;

public class StevedoresTaskHisPresenter extends BasePresenter {


    public StevedoresTaskHisPresenter(StevedoresTaskHisContract.stevedoresTaskHisView addScooterView) {
        mRequestView = addScooterView;
        mRequestModel = new StevedoresTaskHisModel();
    }

    public void stevedoresTaskHis(String operatorId) {
        mRequestView.showNetDialog();
        ((StevedoresTaskHisModel) mRequestModel).stevedoresTaskHis(operatorId, new IResultLisenter<List<LoadAndUnloadTodoBean>>() {
            @Override
            public void onSuccess(List<LoadAndUnloadTodoBean> result) {
                ((StevedoresTaskHisContract.stevedoresTaskHisView) mRequestView).stevedoresTaskHisResult(result);
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
