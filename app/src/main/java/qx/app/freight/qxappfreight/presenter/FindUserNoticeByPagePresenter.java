package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.NoticeBean;
import qx.app.freight.qxappfreight.bean.response.NoticeViewBean;
import qx.app.freight.qxappfreight.contract.FindUserNoticeByPageContract;
import qx.app.freight.qxappfreight.model.FindUserNoticeByPageModel;

public class FindUserNoticeByPagePresenter extends BasePresenter {
    public FindUserNoticeByPagePresenter(FindUserNoticeByPageContract.findUserNoticeByPageView findUserNoticeByPageView) {
        mRequestView = findUserNoticeByPageView;
        mRequestModel = new FindUserNoticeByPageModel();
    }

    public void findUserNoticeByPage(BaseFilterEntity param) {
        mRequestView.showNetDialog();
        ((FindUserNoticeByPageModel) mRequestModel).notice(param, new IResultLisenter<NoticeBean>() {
            @Override
            public void onSuccess(NoticeBean noticeBean) {
                ((FindUserNoticeByPageContract.findUserNoticeByPageView) mRequestView).noticeResult(noticeBean);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void noticeView(BaseFilterEntity param) {
        mRequestView.showNetDialog();
        ((FindUserNoticeByPageModel) mRequestModel).noticeView(param, new IResultLisenter<NoticeViewBean>() {
            @Override
            public void onSuccess(NoticeViewBean noticeViewBean) {
                ((FindUserNoticeByPageContract.findUserNoticeByPageView) mRequestView).noticeViewResult(noticeViewBean);
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
