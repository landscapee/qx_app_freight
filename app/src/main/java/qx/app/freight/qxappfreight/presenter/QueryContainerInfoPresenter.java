package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.QueryContainerInfoEntity;
import qx.app.freight.qxappfreight.bean.response.QueryContainerInfoBean;
import qx.app.freight.qxappfreight.contract.QueryContainerInfoContract;
import qx.app.freight.qxappfreight.model.QueryContainerInfoModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class QueryContainerInfoPresenter extends BasePresenter {

    public QueryContainerInfoPresenter(QueryContainerInfoContract.queryContainerInfoView queryContainerInfoView) {
        mRequestView = queryContainerInfoView;
        mRequestModel = new QueryContainerInfoModel();
    }

    public void queryContainerInfo(QueryContainerInfoEntity queryContainerInfoEntity) {
        mRequestView.showNetDialog();
        ((QueryContainerInfoModel) mRequestModel).queryContainerInfo(queryContainerInfoEntity, new IResultLisenter<List<QueryContainerInfoBean>>() {
            @Override
            public void onSuccess(List<QueryContainerInfoBean> queryContainerInfoBeans) {
                ((QueryContainerInfoContract.queryContainerInfoView) mRequestView).queryContainerInfoResult(queryContainerInfoBeans);
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
