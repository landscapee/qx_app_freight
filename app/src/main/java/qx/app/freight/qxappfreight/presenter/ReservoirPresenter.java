package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.QueryWaybillInfoEntity;
import qx.app.freight.qxappfreight.bean.response.ArrivalCargoInfoBean;
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

    public void getAirWaybillPrefix(String str) {
        mRequestView.showNetDialog();
        ((ReservoirModel) mRequestModel).getAirWaybillPrefix(str, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((ReservoirContract.reservoirView) mRequestView).getAirWaybillPrefixResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void getWaybillInfoByCode(QueryWaybillInfoEntity entity) {
        mRequestView.showNetDialog();
        ((ReservoirModel) mRequestModel).getWaybillInfoByCode(entity, new IResultLisenter<ArrivalCargoInfoBean>() {
            @Override
            public void onSuccess(ArrivalCargoInfoBean result) {
                ((ReservoirContract.reservoirView) mRequestView).getWaybillInfoByCodeResult(result);
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
