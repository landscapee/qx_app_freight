package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.contract.LookLUggageScannigFlightContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class LookLUggageScannigFlightModel extends BaseModel implements LookLUggageScannigFlightContract.lookLUggageScannigFlightModel {
    @Override
    public void lookLUggageScannigFlight(BaseFilterEntity model, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().lookLUggageScannigFlight(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });

        mDisposableList.add(subscription);
    }

    @Override
    public void getDepartureFlightByAndroid(BaseFilterEntity model, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getDepartureFlightByAndroid(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });

        mDisposableList.add(subscription);
    }
}
