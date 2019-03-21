package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.ExceptionReportEntity;
import qx.app.freight.qxappfreight.bean.request.StorageCommitEntity;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;

/**
 * TODO : xxx
 * Created by pr
 */
public class PullGoodsReportContract {
    public interface pullGoodsModel {
        void pullGoodsReport(ExceptionReportEntity storageCommitEntity, IResultLisenter lisenter);
        void scanScooterDelete(TransportEndEntity transportEndEntity, IResultLisenter lisenter);
    }

    public interface pullGoodsView extends IBaseView {
        void pullGoodsReportResult(String result);
        void scanScooterDeleteResult(String result);
    }

}
