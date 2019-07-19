package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.BaseParamBean;
import qx.app.freight.qxappfreight.bean.response.ChangeStorageBean;
import qx.app.freight.qxappfreight.contract.BaseParamContract;
import qx.app.freight.qxappfreight.model.BaseParamModel;

public class BaseParamPresenter extends BasePresenter {

    public BaseParamPresenter(BaseParamContract.baseParamView baseParamView) {
        mRequestView = baseParamView;
        mRequestModel = new BaseParamModel();
    }

    public void baseParam(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((BaseParamModel) mRequestModel).baseParam(entity, new IResultLisenter<BaseParamBean>() {
            @Override
            public void onSuccess(BaseParamBean result) {
                ((BaseParamContract.baseParamView) mRequestView).baseParamResult(result);
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
