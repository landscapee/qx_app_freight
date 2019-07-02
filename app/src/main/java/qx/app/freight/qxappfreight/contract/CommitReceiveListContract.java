package qx.app.freight.qxappfreight.contract;

import java.util.List;
import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.StorageCommitEntity;

public class CommitReceiveListContract {

    public interface commitReceiveListrModel {
        void commitReceiveListr(List<StorageCommitEntity> entity, IResultLisenter lisenter);
    }

    public interface commitReceiveListrView extends IBaseView {
        void commitReceiveListrResult(String result);
    }
}
