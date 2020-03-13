package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.StorageCommitEntity;
import qx.app.freight.qxappfreight.contract.CommitReceiveListContract;
import qx.app.freight.qxappfreight.model.CommitReceiveListModel;

public class CommitReceiveListPresenter extends BasePresenter {
    public CommitReceiveListPresenter(CommitReceiveListContract.commitReceiveListrView commitReceiveListrView) {
        mRequestView = commitReceiveListrView;
        mRequestModel = new CommitReceiveListModel();
    }

    public void commitReceiveList(List<StorageCommitEntity> entity) {
        mRequestView.showNetDialog();
        ((CommitReceiveListModel) mRequestModel).commitReceiveListr(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((CommitReceiveListContract.commitReceiveListrView) mRequestView).commitReceiveListrResult(result);
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
