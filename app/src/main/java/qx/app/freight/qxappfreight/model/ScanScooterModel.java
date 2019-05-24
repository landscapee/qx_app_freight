package qx.app.freight.qxappfreight.model;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.contract.ScanScooterContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

/**
 * TODO : xxx
 * Created by pr
 */
public class ScanScooterModel extends BaseModel implements ScanScooterContract.scanScooterModel {
    @Override
    public void scanScooter(TransportTodoListBean transportEndEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().scanScooter(transportEndEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void scanLockScooter(List<TransportTodoListBean> transportEndEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().scanLockScooter(transportEndEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void scooterWithUser(String user, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().scooterWithUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
