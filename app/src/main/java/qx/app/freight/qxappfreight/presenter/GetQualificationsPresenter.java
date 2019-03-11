package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.GetQualificationsBean;
import qx.app.freight.qxappfreight.contract.GetQualificationsContract;
import qx.app.freight.qxappfreight.model.GetQualificationsModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class GetQualificationsPresenter extends BasePresenter {

    public GetQualificationsPresenter(GetQualificationsContract.getQualificationsView getQualificationsView) {
        mRequestView = getQualificationsView;
        mRequestModel = new GetQualificationsModel();
    }

    public void getQualifications(String freightCode) {
        mRequestView.showNetDialog();
        ((GetQualificationsModel) mRequestModel).getQualifications(freightCode, new IResultLisenter<GetQualificationsBean>() {
            @Override
            public void onSuccess(GetQualificationsBean getQualificationsBean) {
                ((GetQualificationsContract.getQualificationsView) mRequestView).getQualificationsResult(getQualificationsBean);
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
