package qx.app.freight.qxappfreight.presenter;


import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.SaveOrUpdateEntity;
import qx.app.freight.qxappfreight.contract.SaveOrUpdateContract;
import qx.app.freight.qxappfreight.model.SaveOrUpdateModel;

public class SaveOrUpdatePresenter extends BasePresenter {

    public SaveOrUpdatePresenter(SaveOrUpdateContract.saveOrUpdateView saveOrUpdateView) {
        mRequestView = saveOrUpdateView;
        mRequestModel = new SaveOrUpdateModel();
    }

    public void saveOrUpdate(SaveOrUpdateEntity entity) {
        mRequestView.showNetDialog();
        ((SaveOrUpdateModel) mRequestModel).saveOrUpdate(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((SaveOrUpdateContract.saveOrUpdateView) mRequestView).saveOrUpdateResult(result);
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
