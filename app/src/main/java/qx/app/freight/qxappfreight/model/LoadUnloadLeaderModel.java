package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.contract.LoadUnloadLeaderContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;
/**
 * 装卸员小组长接收新任务Model
 */
public class LoadUnloadLeaderModel extends BaseModel implements LoadUnloadLeaderContract.LoadUnloadLeaderModel {

    @Override
    public void slideTask(PerformTaskStepsEntity baseFilterEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().performTaskSteps(baseFilterEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void refuseTask(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().refuseTask(baseFilterEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
