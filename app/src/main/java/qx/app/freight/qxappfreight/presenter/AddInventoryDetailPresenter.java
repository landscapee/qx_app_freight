package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import okhttp3.MultipartBody;
import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.InventoryDetailEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.ListWaybillCodeBean;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;
import qx.app.freight.qxappfreight.contract.AddInventoryDetailContract;
import qx.app.freight.qxappfreight.model.AddInventoryDetailModel;

public class AddInventoryDetailPresenter extends BasePresenter {

    public AddInventoryDetailPresenter(AddInventoryDetailContract.addInventoryDetailView addInventoryDetailView) {
        mRequestView = addInventoryDetailView;
        mRequestModel = new AddInventoryDetailModel();
    }

    public void addInventoryDetail(List <InventoryDetailEntity> list) {
        mRequestView.showNetDialog();
        ((AddInventoryDetailModel) mRequestModel).addInventoryDetail(list, new IResultLisenter <BaseEntity>() {
            @Override
            public void onSuccess(BaseEntity result) {
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

    public void uploads(List <MultipartBody.Part> files) {
        mRequestView.showNetDialog();
        ((AddInventoryDetailModel) mRequestModel).uploads(files, new IResultLisenter <Object>() {
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

    public void listWaybillCode(String code, String taskId) {
        mRequestView.showNetDialog();
        ((AddInventoryDetailModel) mRequestModel).listWaybillCode(code, taskId, new IResultLisenter <ListWaybillCodeBean>() {
            @Override
            public void onSuccess(ListWaybillCodeBean result) {
                ((AddInventoryDetailContract.addInventoryDetailView) mRequestView).listWaybillCodeResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void getWaybillCode() {
        mRequestView.showNetDialog();
        ((AddInventoryDetailModel) mRequestModel).getWaybillCode(new IResultLisenter <String>() {
            @Override
            public void onSuccess(String result) {
                ((AddInventoryDetailContract.addInventoryDetailView) mRequestView).getWaybillCodeResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void getWaybillInfoByWaybillCode(String waybillCode) {
        mRequestView.showNetDialog();
        ((AddInventoryDetailModel) mRequestModel).getWaybillInfoByWaybillCode(waybillCode, new IResultLisenter <WaybillsBean>() {
            @Override
            public void onSuccess(WaybillsBean result) {
                ((AddInventoryDetailContract.addInventoryDetailView) mRequestView).getWaybillInfoByWaybillCodeResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                ((AddInventoryDetailContract.addInventoryDetailView) mRequestView).getWaybillInfoByWaybillCodeResultFail();
                mRequestView.dissMiss();
            }
        });
    }
}
