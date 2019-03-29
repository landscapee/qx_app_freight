package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.contract.AddScooterContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class AddScooterModel extends BaseModel implements AddScooterContract.addScooterModel {
    @Override
    public void addScooter(IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().addScooter()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
