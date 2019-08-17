package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.PageListEntity;
import qx.app.freight.qxappfreight.bean.request.UserBean;
import qx.app.freight.qxappfreight.bean.response.RespBean;
import qx.app.freight.qxappfreight.contract.NoReadCountContract;
import qx.app.freight.qxappfreight.model.NoReadCountModel;

public class NoReadCountPresenter extends BasePresenter {

    public NoReadCountPresenter(NoReadCountContract.noReadCountView noReadCountView) {
        mRequestView = noReadCountView;
        mRequestModel = new NoReadCountModel();
    }

    public void noReadCount(PageListEntity model) {
        mRequestView.showNetDialog();
        ((NoReadCountModel) mRequestModel).noReadCount(model, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((NoReadCountContract.noReadCountView) mRequestView).noReadCountResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void noReadNoticeCount(String userId) {
        mRequestView.showNetDialog();
        ((NoReadCountModel) mRequestModel).noReadNoticeCount(userId, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((NoReadCountContract.noReadCountView) mRequestView).noReadNoticeCountResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void loginOut(UserBean userBean) {
        mRequestView.showNetDialog();
        ((NoReadCountModel) mRequestModel).loginOut(userBean, new IResultLisenter<RespBean>() {
            @Override
            public void onSuccess(RespBean result) {
                ((NoReadCountContract.noReadCountView) mRequestView).loginOutResult(result);
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
