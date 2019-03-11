package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.StorageCommitEntity;
import qx.app.freight.qxappfreight.bean.request.SubmissionEntity;
import qx.app.freight.qxappfreight.contract.SubmissionContract;
import qx.app.freight.qxappfreight.model.SubmissionModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class SubmissionPresenter extends BasePresenter {
    public SubmissionPresenter(SubmissionContract.submissionView submissionView) {
        mRequestView = submissionView;
        mRequestModel = new SubmissionModel();
    }

    public void submission(StorageCommitEntity storageCommitEntity) {
        mRequestView.showNetDialog();
        ((SubmissionModel) mRequestModel).submission(storageCommitEntity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((SubmissionContract.submissionView) mRequestView).submissionResult(result);
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
