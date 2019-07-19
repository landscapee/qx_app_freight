package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.TaskLockEntity;
import qx.app.freight.qxappfreight.contract.TaskLockContract;
import qx.app.freight.qxappfreight.model.TaskLockModel;

/**
 * Created by guohao On 2019/6/21 11:18
 *
 * @description 待办锁定
 */
public class TaskLockPresenter extends BasePresenter {

    public TaskLockPresenter(TaskLockContract.taskLockView taskLockView){
        mRequestView = taskLockView;
        mRequestModel = new TaskLockModel();
    }

    public void taskLock(TaskLockEntity entity){

        mRequestView.showNetDialog();

        ((TaskLockModel) mRequestModel).taskLock(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                mRequestView.dissMiss();
                ((TaskLockContract.taskLockView) mRequestView).taskLockResult(result);
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });

    }

}
