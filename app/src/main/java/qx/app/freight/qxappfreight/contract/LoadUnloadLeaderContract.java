package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;

/**
 * 装卸员小组长任务列表Contract
 */
public class LoadUnloadLeaderContract {

    public interface LoadUnloadLeaderModel {
        void getLoadUnloadLeaderList(String taskId, IResultLisenter lisenter);
        void slideTask(PerformTaskStepsEntity baseFilterEntity, IResultLisenter lisenter);
        void refuseTask(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter);
    }

    public interface LoadUnloadLeaderView extends IBaseView {
        void getLoadUnloadLeaderListResult(List<LoadAndUnloadTodoBean> loadAndUnloadTodoBean);
        void slideTaskResult(String result);
        void refuseTaskResult(String result);
    }
}
