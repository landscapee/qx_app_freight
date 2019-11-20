package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.contract.AuditManifestContract;
import qx.app.freight.qxappfreight.model.AuditManifestModel;

public class AuditManifestPresenter extends BasePresenter {
    public AuditManifestPresenter(AuditManifestContract.auditManifestView addScooterView) {
        mRequestView = addScooterView;
        mRequestModel = new AuditManifestModel();
    }

    public void auditManifest(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((AuditManifestModel) mRequestModel).auditManifest(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((AuditManifestContract.auditManifestView) mRequestView).auditManifestResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }

        });
    }
    public void repartWriteLoading(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((AuditManifestModel) mRequestModel).repartWriteLoading(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((AuditManifestContract.auditManifestView) mRequestView).repartWriteLoadingResult(result);
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
