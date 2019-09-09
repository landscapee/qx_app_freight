package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.contract.ExceptionContentContract;
import qx.app.freight.qxappfreight.model.ExceptionContentModel;

public class ExceptionContentPresenter extends BasePresenter {
    public ExceptionContentPresenter(ExceptionContentContract.exceptionContentView addScooterView) {
        mRequestView = addScooterView;
        mRequestModel = new ExceptionContentModel();
    }

    public void exceptionContent(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((ExceptionContentModel) mRequestModel).exceptionContent(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((ExceptionContentContract.exceptionContentView) mRequestView).exceptionContentResult(result);
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
