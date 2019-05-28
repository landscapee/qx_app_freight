package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;

public class EndInstallToDoContract {
    public interface IModel {
        void getEndInstallTodo(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter);
        void slideTask(PerformTaskStepsEntity baseFilterEntity, IResultLisenter lisenter);
    }

    public interface IView extends IBaseView {
        void getEndInstallTodoResult(List<LoadAndUnloadTodoBean> loadAndUnloadTodoBean);
        void slideTaskResult(String result);
    }
}
