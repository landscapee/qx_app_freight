package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.DeclareApplyForRecords;
import qx.app.freight.qxappfreight.contract.ChangeStorageListContract;
import qx.app.freight.qxappfreight.model.ChangeStorageListModel;

public class ChangeStorageListPresenter extends BasePresenter {
    public ChangeStorageListPresenter(ChangeStorageListContract.changeStorageListView changeStorageView) {
        mRequestView = changeStorageView;
        mRequestModel = new ChangeStorageListModel();
    }

    public void changeStorageList(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((ChangeStorageListModel) mRequestModel).changeStorageList(entity, new IResultLisenter<DeclareApplyForRecords>() {
            @Override
            public void onSuccess(DeclareApplyForRecords result) {
                ((ChangeStorageListContract.changeStorageListView) mRequestView).changeStorageListResult(result);
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
