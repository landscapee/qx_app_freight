package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.TaskLockEntity;
import qx.app.freight.qxappfreight.contract.TaskLockContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

/**
 * Created by guohao On 2019/6/21 10:57
 *
 * @description 待办锁定
 */
public class TaskLockModel extends BaseModel implements TaskLockContract.taskLockModel {


    @Override
    public void taskLock(TaskLockEntity entity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().taskLock(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
