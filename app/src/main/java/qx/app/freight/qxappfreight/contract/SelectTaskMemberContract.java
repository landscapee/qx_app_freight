package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.SelectTaskMemberEntity;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;

/**
 * 装卸员小组长选择任务人员Contract
 */
public class SelectTaskMemberContract {
    public interface SelectTaskMemberModel {
        void getLoadUnloadLeaderList(String taskId, IResultLisenter lisenter);

        void selectMember(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter);
    }

    public interface SelectTaskMemberView extends IBaseView {
        void getLoadUnloadLeaderListResult(List<SelectTaskMemberEntity> loadAndUnloadTodoBean);

        void selectMemberResult(String result);
    }
}
