package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ReservoirBean;
import qx.app.freight.qxappfreight.contract.ReservoirContract;
import qx.app.freight.qxappfreight.model.ReservoirModel;

public class ReservoirPresenter extends BasePresenter {
    public ReservoirPresenter(ReservoirContract.reservoirView reservoirView) {
        mRequestView = reservoirView;
        mRequestModel = new ReservoirModel();
    }

    public void reservoir(BaseFilterEntity model) {
        mRequestView.showNetDialog();
        ((ReservoirModel) mRequestModel).reservoir(model, new IResultLisenter<ReservoirBean>() {
            @Override
            public void onSuccess(ReservoirBean result) {
                ((ReservoirContract.reservoirView) mRequestView).reservoirResult(result);
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
