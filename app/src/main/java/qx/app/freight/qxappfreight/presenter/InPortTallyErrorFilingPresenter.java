package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.ErrorFilingEntity;
import qx.app.freight.qxappfreight.contract.InPortTallyErrorFilingContract;
import qx.app.freight.qxappfreight.model.InPortTallyErrorFilingModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class InPortTallyErrorFilingPresenter extends BasePresenter {

    public InPortTallyErrorFilingPresenter(InPortTallyErrorFilingContract.InPortErrorFilingView view) {
        mRequestView = view;
        mRequestModel = new InPortTallyErrorFilingModel();
    }

    public void errorFiling(ErrorFilingEntity model) {
        mRequestView.showNetDialog();
        ((InPortTallyErrorFilingModel) mRequestModel).errorFiling(model, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((InPortTallyErrorFilingContract.InPortErrorFilingView) mRequestView).errorFilingResult(result);
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
