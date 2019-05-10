package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.contract.InternationalCargoReportContract;
import qx.app.freight.qxappfreight.model.InternationalCargoReportModel;

public class InternationalCargoReportPresenter extends BasePresenter {
    public InternationalCargoReportPresenter(InternationalCargoReportContract.internationalCargoReportView internationalCargoReportView) {
        mRequestView = internationalCargoReportView;
        mRequestModel = new InternationalCargoReportModel();
    }

    public void internationalCargoReport(String str) {
        mRequestView.showNetDialog();
        ((InternationalCargoReportModel) mRequestModel).internationalCargoReport(str, new IResultLisenter<String>() {
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
        ((InternationalCargoReportModel) mRequestModel).scooterInfoList(entity, new IResultLisenter<List<ScooterInfoListBean>>() {
            @Override
            public void onSuccess(List<ScooterInfoListBean> result) {
                ((InternationalCargoReportContract.internationalCargoReportView) mRequestView).scooterInfoListResult(result);
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
