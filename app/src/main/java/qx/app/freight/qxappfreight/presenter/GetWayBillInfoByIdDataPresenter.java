package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.GetWaybillInfoByIdDataBean;
import qx.app.freight.qxappfreight.contract.GetWayBillInfoByIdDataContract;
import qx.app.freight.qxappfreight.model.GetWayBillInfoByIdDataModel;

public class GetWayBillInfoByIdDataPresenter extends BasePresenter {
    public GetWayBillInfoByIdDataPresenter(GetWayBillInfoByIdDataContract.getWayBillInfoByIdDataView getWayBillInfoByIdDataView) {
        mRequestView = getWayBillInfoByIdDataView;
        mRequestModel = new GetWayBillInfoByIdDataModel();
    }

    public void getWayBillInfoByIdData(String waybillCode) {
        mRequestView.showNetDialog();
        ((GetWayBillInfoByIdDataModel) mRequestModel).getWayBillInfoByIdData(waybillCode, new IResultLisenter<GetWaybillInfoByIdDataBean>() {
            @Override
            public void onSuccess(GetWaybillInfoByIdDataBean result) {
                ((GetWayBillInfoByIdDataContract.getWayBillInfoByIdDataView) mRequestView).getWayBillInfoByIdDataResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }

        });
    }

    public void getWaybillInfo(String id) {
        mRequestView.showNetDialog();
        ((GetWayBillInfoByIdDataModel) mRequestModel).getWaybillInfo(id, new IResultLisenter<List<GetWaybillInfoByIdDataBean>>() {
            @Override
            public void onSuccess(List<GetWaybillInfoByIdDataBean> result) {
                ((GetWayBillInfoByIdDataContract.getWayBillInfoByIdDataView) mRequestView).getWaybillInfoResult(result);
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
