package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.InventoryBean;
import qx.app.freight.qxappfreight.contract.GetInventoryContract;
import qx.app.freight.qxappfreight.model.InventoryModel;

/**
 * TODO : 获取 出库或者入库 单
 * Created by
 */
public class GetInventoryPresenter extends BasePresenter {

    public GetInventoryPresenter(GetInventoryContract.getInventoryView inventoryView) {
        mRequestView = inventoryView;
        mRequestModel = new InventoryModel();
    }

    public void getInventoryList(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((InventoryModel) mRequestModel).getInventory(entity, new IResultLisenter <InventoryBean>() {
            @Override
            public void onSuccess(InventoryBean result) {
                ((GetInventoryContract.getInventoryView) mRequestView).setInventoryResult(result.getRecords());
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
