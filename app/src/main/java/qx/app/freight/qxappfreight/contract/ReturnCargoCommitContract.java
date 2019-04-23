package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.TransportListCommitEntity;

public class ReturnCargoCommitContract {
    public interface returnCargoCommitModel {
        void returnCargoCommit(TransportListCommitEntity transportListCommitEntity, IResultLisenter lisenter);
    }

    public interface returnCargoCommitView extends IBaseView {
        void returnCargoCommitResult(String result);
    }
}
