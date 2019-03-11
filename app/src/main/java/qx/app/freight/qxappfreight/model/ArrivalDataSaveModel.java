package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;
import qx.app.freight.qxappfreight.contract.ArrivalDataSaveContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class ArrivalDataSaveModel extends BaseModel implements ArrivalDataSaveContract.arrivalDataSaveModel {
    @Override
    public void arrivalDataSave(TransportEndEntity transportEndEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().arrivalDataSave(transportEndEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
