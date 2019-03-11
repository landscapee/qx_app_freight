package qx.app.freight.qxappfreight.model;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;
import qx.app.freight.qxappfreight.contract.TransportBeginContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

/**
 * TODO : xxx
 * Created by pr
 */
public class TransportBeginModel extends BaseModel implements TransportBeginContract.transportBeginModel {
    @Override
    public void transportBegin(TransportEndEntity transportEndEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().transportBegin(transportEndEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void transportEnd(TransportEndEntity transportEndEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().transportEnd(transportEndEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
    @Override
    public void scanScooterDelete(TransportEndEntity transportEndEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().scanScooterDelete(transportEndEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
