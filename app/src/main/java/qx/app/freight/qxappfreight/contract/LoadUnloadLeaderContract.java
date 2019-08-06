package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;

/**
 * 装卸员小组长接收新任务Contract
 */
public class LoadUnloadLeaderContract {

    public interface LoadUnloadLeaderModel {
        void slideTask(PerformTaskStepsEntity baseFilterEntity, IResultLisenter lisenter);

        void refuseTask(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter);
    }

    public interface LoadUnloadLeaderView extends IBaseView {
        void slideTaskResult(String result);

        void refuseTaskResult(String result);
    }
}
