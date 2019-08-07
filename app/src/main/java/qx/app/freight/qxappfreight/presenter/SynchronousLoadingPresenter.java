package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.contract.SynchronousLoadingContract;
import qx.app.freight.qxappfreight.model.SynchronousLoadingModel;

public class SynchronousLoadingPresenter extends BasePresenter {
    public SynchronousLoadingPresenter(SynchronousLoadingContract.synchronousLoadingView addScooterView) {
        mRequestView = addScooterView;
        mRequestModel = new SynchronousLoadingModel();
    }

    public void synchronousLoading(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((SynchronousLoadingModel) mRequestModel).synchronousLoading(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((SynchronousLoadingContract.synchronousLoadingView) mRequestView).synchronousLoadingResult(result);
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
