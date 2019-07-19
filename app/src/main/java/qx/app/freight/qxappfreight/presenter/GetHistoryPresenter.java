package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.GetHistoryBean;
import qx.app.freight.qxappfreight.contract.GetHistoryContract;
import qx.app.freight.qxappfreight.model.GetHistoryModel;

public class GetHistoryPresenter extends BasePresenter {

    public GetHistoryPresenter(GetHistoryContract.getHistoryView getHistoryView) {
        mRequestView = getHistoryView;
        mRequestModel = new GetHistoryModel();
    }

    public void getHistory(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((GetHistoryModel) mRequestModel).getHistory(entity,new IResultLisenter<GetHistoryBean>() {
            @Override
            public void onSuccess(GetHistoryBean result) {
                ((GetHistoryContract.getHistoryView) mRequestView).getHistoryResult(result);
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
