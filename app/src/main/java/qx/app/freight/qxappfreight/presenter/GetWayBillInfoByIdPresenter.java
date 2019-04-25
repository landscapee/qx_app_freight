package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.DeclareWaybillBean;
import qx.app.freight.qxappfreight.contract.GetWayBillInfoByIdContract;
import qx.app.freight.qxappfreight.model.GetWayBillInfoByIdModel;

public class GetWayBillInfoByIdPresenter extends BasePresenter {
    public GetWayBillInfoByIdPresenter(GetWayBillInfoByIdContract.getWayBillInfoByIdView getWayBillInfoByIdView) {
        mRequestView = getWayBillInfoByIdView;
        mRequestModel = new GetWayBillInfoByIdModel();
    }

    public void getWayBillInfoById(String id) {
        mRequestView.showNetDialog();
        ((GetWayBillInfoByIdModel) mRequestModel).getWayBillInfoById(id, new IResultLisenter<DeclareWaybillBean>() {
            @Override
            public void onSuccess(DeclareWaybillBean result) {
                ((GetWayBillInfoByIdContract.getWayBillInfoByIdView) mRequestView).getWayBillInfoByIdResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }

        });
    }

    public void sendPrintMessage(String waybillId) {
        mRequestView.showNetDialog();
        ((GetWayBillInfoByIdModel) mRequestModel).sendPrintMessage(waybillId, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((GetWayBillInfoByIdContract.getWayBillInfoByIdView) mRequestView).sendPrintMessageResult(result);
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
