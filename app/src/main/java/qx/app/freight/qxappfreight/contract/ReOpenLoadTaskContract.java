package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;

/**
 * 重新开启装机任务contract
 */
public class ReOpenLoadTaskContract {
    public interface ReOpenLoadTaskModel {
        void reOpenLoadTask(BaseFilterEntity entity, IResultLisenter lisenter);
    }

    public interface ReOpenLoadTaskView extends IBaseView {
        void reOpenLoadTaskResult(String result);
    }
}
