package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.PageListEntity;

public class NoReadCountContract {
    public interface noReadCountModel {
        void noReadCount(PageListEntity pageListEntity, IResultLisenter lisenter);
    }

    public interface noReadCountView extends IBaseView {
        void noReadCountResult(String result);
    }
}
