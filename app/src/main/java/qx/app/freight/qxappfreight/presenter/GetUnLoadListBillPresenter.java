package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.UnLoadRequestEntity;
import qx.app.freight.qxappfreight.bean.response.UnLoadListBillBean;
import qx.app.freight.qxappfreight.contract.GetUnLoadListBillContract;
import qx.app.freight.qxappfreight.model.GetUnLoadListBillModel;

public class GetUnLoadListBillPresenter extends BasePresenter {

    public GetUnLoadListBillPresenter(GetUnLoadListBillContract.IView getFlightCargoResView) {
        mRequestView = getFlightCargoResView;
        mRequestModel = new GetUnLoadListBillModel();
    }

    public void getUnLoadingList(UnLoadRequestEntity entity) {
        mRequestView.showNetDialog();
        ((GetUnLoadListBillModel) mRequestModel).getUnLoadingList(entity, new IResultLisenter<UnLoadListBillBean>() {
            @Override
            public void onSuccess(UnLoadListBillBean result) {
                ((GetUnLoadListBillContract.IView) mRequestView).getUnLoadingListResult(result);
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
