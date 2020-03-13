package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.LockScooterEntity;
import qx.app.freight.qxappfreight.contract.GetLastReportInfoContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class GetLastReportInfoModel extends BaseModel implements GetLastReportInfoContract.getLastReportInfoModel {
    @Override
    public void getLastReportInfo(BaseFilterEntity entity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getLastReportInfo(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void lockOrUnlockScooter(LockScooterEntity lockScooterEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().lockOrUnlockScooter(lockScooterEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
