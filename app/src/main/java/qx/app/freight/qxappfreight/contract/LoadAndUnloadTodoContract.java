package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.request.TaskClearEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;

public class LoadAndUnloadTodoContract {

    public interface loadAndUnloadTodoModel {
        void loadAndUnloadTodo(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter);

        void slideTask(PerformTaskStepsEntity baseFilterEntity, IResultLisenter lisenter);

        void startClearTask(TaskClearEntity taskClearEntity,IResultLisenter lisenter);
    }

    public interface loadAndUnloadTodoView extends IBaseView {
        void loadAndUnloadTodoResult(List<LoadAndUnloadTodoBean> loadAndUnloadTodoBean);

        void slideTaskResult(String result);

        void startClearTaskResult(String result);
    }
}
