package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.QueryAviationRequireBean;
import qx.app.freight.qxappfreight.contract.GetQueryAviationRequireContract;
import qx.app.freight.qxappfreight.model.GetQueryAviationRequireModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class GetQueryAviationRequirePresenter extends BasePresenter {

    public GetQueryAviationRequirePresenter(GetQueryAviationRequireContract.getQueryAviationRequireView getQualificationsView) {
        mRequestView = getQualificationsView;
        mRequestModel = new GetQueryAviationRequireModel();
    }

    public void getQueryAviationRequire(String airLineId) {
        mRequestView.showNetDialog();
        ((GetQueryAviationRequireModel) mRequestModel).getQueryAviationRequire(airLineId, new IResultLisenter<List<QueryAviationRequireBean>>() {
            @Override
            public void onSuccess(List<QueryAviationRequireBean> queryAviationRequireBeans) {
                ((GetQueryAviationRequireContract.getQueryAviationRequireView) mRequestView).getQueryAviationRequireResult(queryAviationRequireBeans);
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
