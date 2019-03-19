package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.InportTallyBean;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.InPortTallyCommitEntity;

public class InPortTallyContract {
    public interface InPortTallyListModel {
        void getInPortTallyList(BaseFilterEntity entity, IResultLisenter lisenter);

        void inPortTallyCommit(InPortTallyCommitEntity entity, IResultLisenter lisenter);
    }

    public interface InPortTallyListView extends IBaseView {
        void getInPortTallyListResult(List<InportTallyBean> result);

        void inPortTallyCommitResult(String result);
    }
}
