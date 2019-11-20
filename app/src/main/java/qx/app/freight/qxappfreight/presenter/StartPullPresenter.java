package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.PullGoodsEntity;
import qx.app.freight.qxappfreight.contract.StartPullContract;
import qx.app.freight.qxappfreight.model.StartPullModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class StartPullPresenter extends BasePresenter {

    public StartPullPresenter(StartPullContract.startPullView startPullView) {
        mRequestView = startPullView;
        mRequestModel = new StartPullModel();
    }

    public void startPull(PullGoodsEntity pullGoodsEntity) {
        mRequestView.showNetDialog();
        ((StartPullModel) mRequestModel).startPull(pullGoodsEntity, new IResultLisenter <String>() {
            @Override
            public void onSuccess(String result) {
                ((StartPullContract.startPullView) mRequestView).startPullResult(result);
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
