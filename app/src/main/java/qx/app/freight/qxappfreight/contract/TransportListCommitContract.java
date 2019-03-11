package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.TransportListCommitEntity;

/**
 * TODO : xxx
 * Created by pr
 */
public class TransportListCommitContract {
    public interface transportListCommitModel {
        void transportListCommit(TransportListCommitEntity transportListCommitEntity, IResultLisenter lisenter);
        void deleteCollectionInfo(String id, IResultLisenter lisenter);
    }

    public interface transportListCommitView extends IBaseView {
        void transportListCommitResult(String result);
        void deleteCollectionInfoResult(String result);
    }
}
