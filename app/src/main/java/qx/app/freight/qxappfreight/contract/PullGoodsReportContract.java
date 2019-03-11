package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.ExceptionReportEntity;
import qx.app.freight.qxappfreight.bean.request.StorageCommitEntity;

/**
 * TODO : xxx
 * Created by pr
 */
public class PullGoodsReportContract {
    public interface pullGoodsModel {
        void pullGoodsReport(ExceptionReportEntity storageCommitEntity, IResultLisenter lisenter);
    }

    public interface pullGoodsView extends IBaseView {
        void pullGoodsReportResult(String result);
    }

}
