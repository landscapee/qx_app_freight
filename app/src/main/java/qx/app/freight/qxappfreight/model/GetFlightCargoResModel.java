package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.GetFlightCargoResBean;
import qx.app.freight.qxappfreight.contract.GetFlightCargoResContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class GetFlightCargoResModel extends BaseModel implements GetFlightCargoResContract.getFlightCargoResModel {
    @Override
    public void getFlightCargoRes(String flightid, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getFlightCargoRes(flightid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void flightDoneInstall(GetFlightCargoResBean entity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().flightDoneInstall(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
