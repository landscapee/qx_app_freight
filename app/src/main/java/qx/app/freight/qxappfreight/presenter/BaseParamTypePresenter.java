package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.BaseParamBean;
import qx.app.freight.qxappfreight.contract.BaseParamTypeContract;
import qx.app.freight.qxappfreight.model.BaseParamTypeModel;

public class BaseParamTypePresenter extends BasePresenter {

    public BaseParamTypePresenter(BaseParamTypeContract.baseParamTypeView baseParamView) {
        mRequestView = baseParamView;
        mRequestModel = new BaseParamTypeModel();
    }

    public void baseParamType(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((BaseParamTypeModel) mRequestModel).baseParamType(entity, new IResultLisenter <BaseParamBean>() {
            @Override
            public void onSuccess(BaseParamBean result) {
                ((BaseParamTypeContract.baseParamTypeView) mRequestView).baseParamTypeResult(result);
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
