package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.StorageCommitEntity;

/**
 * TODO : xxx
 * Created by pr
 */
public class SubmissionContract {
    public interface submissionModel {
        void submission(StorageCommitEntity storageCommitEntity, IResultLisenter lisenter);
    }

    public interface submissionView extends IBaseView {
        void submissionResult(String result);
    }

}
