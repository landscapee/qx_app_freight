package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.CargoUploadBean;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListDataBean;
import qx.app.freight.qxappfreight.contract.InternationalCargoReportContract;
import qx.app.freight.qxappfreight.model.InternationalCargoReportModel;

public class InternationalCargoReportPresenter extends BasePresenter {
    public InternationalCargoReportPresenter(InternationalCargoReportContract.internationalCargoReportView internationalCargoReportView) {
        mRequestView = internationalCargoReportView;
        mRequestModel = new InternationalCargoReportModel();
    }

    public void internationalCargoReport(CargoUploadBean entity) {
        mRequestView.showNetDialog();
        ((InternationalCargoReportModel) mRequestModel).internationalCargoReport(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((InternationalCargoReportContract.internationalCargoReportView) mRequestView).internationalCargoReportResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }

        });
    }

    public void scooterInfoList(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((InternationalCargoReportModel) mRequestModel).scooterInfoList(entity, new IResultLisenter<ScooterInfoListDataBean>() {
            @Override
            public void onSuccess(ScooterInfoListDataBean result) {
                ((InternationalCargoReportContract.internationalCargoReportView) mRequestView).scooterInfoListResult(result.getRecords());
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
