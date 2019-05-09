package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.InventoryQueryBean;
import qx.app.freight.qxappfreight.contract.InventoryQueryContract;
import qx.app.freight.qxappfreight.model.InventoryQueryModel;

public class InventoryQueryPresenter extends BasePresenter {
    public InventoryQueryPresenter(InventoryQueryContract.inventoryQueryView inventoryQueryView) {
        mRequestView = inventoryQueryView;
        mRequestModel = new InventoryQueryModel();
    }

    public void InventoryQuery() {
        mRequestView.showNetDialog();
        ((InventoryQueryModel) mRequestModel).inventoryQuery(new IResultLisenter<List<InventoryQueryBean>>() {
            @Override
            public void onSuccess(List<InventoryQueryBean> result) {
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
}
