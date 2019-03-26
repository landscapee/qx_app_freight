package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PageListEntity;
import qx.app.freight.qxappfreight.bean.response.MsMessageViewBean;
import qx.app.freight.qxappfreight.bean.response.PageListBean;
import qx.app.freight.qxappfreight.contract.MessageContract;
import qx.app.freight.qxappfreight.model.MessageModel;

public class MessagePresenter extends BasePresenter {

    public MessagePresenter(MessageContract.messageView messageView) {
        mRequestView = messageView;
        mRequestModel = new MessageModel();
    }

    public void pageList(BaseFilterEntity model) {
        mRequestView.showNetDialog();
        ((MessageModel) mRequestModel).pageList(model, new IResultLisenter<PageListBean>() {
            @Override
            public void onSuccess(PageListBean pageListBean) {
                ((MessageContract.messageView) mRequestView).pageListResult(pageListBean);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void msMessageView(BaseFilterEntity model) {
        mRequestView.showNetDialog();
        ((MessageModel) mRequestModel).msMessageView(model, new IResultLisenter<MsMessageViewBean>() {
            @Override
            public void onSuccess(MsMessageViewBean msMessageViewBean) {
                ((MessageContract.messageView) mRequestView).msMessageViewResult(msMessageViewBean);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void noReadCount(PageListEntity model) {
        mRequestView.showNetDialog();
        ((MessageModel) mRequestModel).noReadCount(model, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((MessageContract.messageView) mRequestView).noReadCountResult(result);
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
