package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import okhttp3.MultipartBody;
import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.InventoryDetailEntity;
import qx.app.freight.qxappfreight.contract.AddInventoryDetailContract;
import qx.app.freight.qxappfreight.model.AddInventoryDetailModel;

public class AddInventoryDetailPresenter extends BasePresenter {

    public AddInventoryDetailPresenter(AddInventoryDetailContract.addInventoryDetailView addInventoryDetailView) {
        mRequestView = addInventoryDetailView;
        mRequestModel = new AddInventoryDetailModel();
    }

    public void addInventoryDetail(List<InventoryDetailEntity> list) {
        mRequestView.showNetDialog();
        ((AddInventoryDetailModel) mRequestModel).addInventoryDetail(list, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((AddInventoryDetailContract.addInventoryDetailView) mRequestView).addInventoryDetailResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void uploads(List<MultipartBody.Part> files) {
        mRequestView.showNetDialog();
        ((AddInventoryDetailModel) mRequestModel).uploads(files, new IResultLisenter<Object>() {
            @Override
            public void onSuccess(Object result) {
                ((AddInventoryDetailContract.addInventoryDetailView) mRequestView).uploadsResult(result);
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
