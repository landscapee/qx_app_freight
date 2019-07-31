package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.LastReportInfoListBean;
import qx.app.freight.qxappfreight.contract.GetLastReportInfoContract;
import qx.app.freight.qxappfreight.model.GetLastReportInfoModel;

public class GetLastReportInfoPresenter extends BasePresenter {
    public GetLastReportInfoPresenter(GetLastReportInfoContract.getLastReportInfoView getLastReportInfoView) {
        mRequestView = getLastReportInfoView;
        mRequestModel = new GetLastReportInfoModel();
    }

    public void getLastReportInfo(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((GetLastReportInfoModel) mRequestModel).getLastReportInfo(entity, new IResultLisenter<LastReportInfoListBean>() {
            @Override
            public void onSuccess(LastReportInfoListBean result) {
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
}
