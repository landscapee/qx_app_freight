package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.InventoryQueryBean;
import qx.app.freight.qxappfreight.contract.InventoryQueryContract;
import qx.app.freight.qxappfreight.model.InventoryQueryModel;

public class InventoryQueryPresenter extends BasePresenter {
    public InventoryQueryPresenter(InventoryQueryContract.inventoryQueryView inventoryQueryView) {
        mRequestView = inventoryQueryView;
        mRequestModel = new InventoryQueryModel();
    }

    public void InventoryQuery(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((InventoryQueryModel) mRequestModel).inventoryQuery(entity,new IResultLisenter<InventoryQueryBean>() {
            @Override
            public void onSuccess(InventoryQueryBean result) {
                ((InventoryQueryContract.inventoryQueryView) mRequestView).inventoryQueryResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }

        });
    }
    public void InventoryQueryHistory(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((InventoryQueryModel) mRequestModel).inventoryHistoryQuery(entity,new IResultLisenter<InventoryQueryBean>() {
            @Override
            public void onSuccess(InventoryQueryBean result) {
                ((InventoryQueryContract.inventoryQueryView) mRequestView).inventoryQueryHistoryResult(result);
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
