package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;

/**
 * 装卸员小组长任务代办列表Contract
 */
public class LoadUnloadLeaderToDoContract {

    public interface LoadUnloadLeaderToDoModel {
        void getLoadUnloadLeaderToDo(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter);

        void slideTask(PerformTaskStepsEntity baseFilterEntity, IResultLisenter lisenter);
    }

    public interface LoadUnloadLeaderToDoView extends IBaseView {
        void getLoadUnloadLeaderToDoResult(List<LoadAndUnloadTodoBean> result);

        void slideTaskResult(String result);
    }
}
