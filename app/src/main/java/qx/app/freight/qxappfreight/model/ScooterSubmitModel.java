package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.ScooterSubmitEntity;
import qx.app.freight.qxappfreight.contract.ScooterSubmitContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class ScooterSubmitModel extends BaseModel implements ScooterSubmitContract.scooterSubmitModel {
    @Override
    public void scooterSubmit(ScooterSubmitEntity scooterSubmitEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().scooterSubmit(scooterSubmitEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
