package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.LockScooterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightAllReportInfo;
import qx.app.freight.qxappfreight.contract.GetLastReportInfoContract;
import qx.app.freight.qxappfreight.model.GetLastReportInfoModel;

public class GetLastReportInfoPresenter extends BasePresenter {
    public GetLastReportInfoPresenter(GetLastReportInfoContract.getLastReportInfoView getLastReportInfoView) {
        mRequestView = getLastReportInfoView;
        mRequestModel = new GetLastReportInfoModel();
    }

    public void getLastReportInfo(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((GetLastReportInfoModel) mRequestModel).getLastReportInfo(entity, new IResultLisenter<List <FlightAllReportInfo>>() {
            @Override
            public void onSuccess(List<FlightAllReportInfo> result) {
                ((GetLastReportInfoContract.getLastReportInfoView) mRequestView).getLastReportInfoResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }

        });
    }
    public void lockOrUnlockScooter(LockScooterEntity entity) {
        mRequestView.showNetDialog();
        ((GetLastReportInfoModel) mRequestModel).lockOrUnlockScooter(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((GetLastReportInfoContract.getLastReportInfoView) mRequestView).lockOrUnlockScooterResult(result);
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
