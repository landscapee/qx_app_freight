package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.InventoryDetailEntity;
import qx.app.freight.qxappfreight.contract.ListInventoryDetailContract;
import qx.app.freight.qxappfreight.model.ListInventoryDetailModel;

public class ListInventoryDetailPresenter extends BasePresenter {
    public ListInventoryDetailPresenter(ListInventoryDetailContract.listInventoryDetailView listInventoryDetailView) {
        mRequestView = listInventoryDetailView;
        mRequestModel = new ListInventoryDetailModel();
    }

    public void listInventoryDetail(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((ListInventoryDetailModel) mRequestModel).listInventoryDetail(entity, new IResultLisenter<List<InventoryDetailEntity>>() {
            @Override
            public void onSuccess(List<InventoryDetailEntity> result) {
                ((ListInventoryDetailContract.listInventoryDetailView) mRequestView).listInventoryDetailResult(result);
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
