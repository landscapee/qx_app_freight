package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.contract.PrintRequestContract;
import qx.app.freight.qxappfreight.model.PrintRequestModel;

public class PrintRequestPresenter extends BasePresenter {
    public PrintRequestPresenter(PrintRequestContract.printRequestView addScooterView) {
        mRequestView = addScooterView;
        mRequestModel = new PrintRequestModel();
    }

    public void printRequest(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((PrintRequestModel) mRequestModel).printRequest(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((PrintRequestContract.printRequestView) mRequestView).printRequestResult(result);
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
