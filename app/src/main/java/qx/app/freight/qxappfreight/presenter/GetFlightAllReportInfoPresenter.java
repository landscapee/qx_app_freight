package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightAllReportInfo;
import qx.app.freight.qxappfreight.contract.GetFlightAllReportInfoContract;
import qx.app.freight.qxappfreight.model.GetFlightAllReportInfoModel;

/**
 * 结载人员获取装机单presenter
 */
public class GetFlightAllReportInfoPresenter extends BasePresenter {
    public GetFlightAllReportInfoPresenter(GetFlightAllReportInfoContract.getFlightAllReportInfoView getFlightAllReportInfoView) {
        mRequestView = getFlightAllReportInfoView;
        mRequestModel = new GetFlightAllReportInfoModel();
    }

    public void getFlightAllReportInfoView(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((GetFlightAllReportInfoModel) mRequestModel).getFlightAllReportInfo(entity, new IResultLisenter<List<FlightAllReportInfo>>() {
            @Override
            public void onSuccess(List<FlightAllReportInfo> result) {
                ((GetFlightAllReportInfoContract.getFlightAllReportInfoView) mRequestView).getFlightAllReportInfoResult(result);
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
