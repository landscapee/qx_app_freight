package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.contract.GetAllInternationalAndMixedFlightContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class GetAllInternationalAndMixedFlightModel extends BaseModel implements GetAllInternationalAndMixedFlightContract.getAllInternationalAndMixedFlightModel {
    @Override
    public void getAllInternationalAndMixedFlight(BaseFilterEntity entity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getAllInternationalAndMixedFlight(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
