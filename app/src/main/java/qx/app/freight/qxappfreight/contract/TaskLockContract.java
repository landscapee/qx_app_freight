package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.TaskLockEntity;

/**
 * Created by guohao On 2019/6/21 10:54
 *
 * @description 待办任务锁定
 */
public class TaskLockContract  {

    public interface taskLockModel{
        void taskLock(TaskLockEntity entity, IResultLisenter lisenter);
    }

    public interface taskLockView extends IBaseView {
        void taskLockResult(String result);
    }
}
