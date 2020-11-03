package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordSubmitNewEntity;
import qx.app.freight.qxappfreight.bean.request.QueryWaybillInfoEntity;
import qx.app.freight.qxappfreight.bean.response.ArrivalCargoInfoBean;
import qx.app.freight.qxappfreight.contract.ReservoirContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class ReservoirModel extends BaseModel implements ReservoirContract.reservoirModel {
    @Override
    public void reservoir(BaseFilterEntity model, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().reservoir(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });

        mDisposableList.add(subscription);
    }

    @Override
    public void getAirWaybillPrefix(String iata, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getAirWaybillPrefix(iata)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });

        mDisposableList.add(subscription);
    }

    @Override
    public void getWaybillInfoByCode(QueryWaybillInfoEntity entity, IResultLisenter<InWaybillRecordSubmitNewEntity.SingleLineBean> lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getWaybillInfoByCode(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });

        mDisposableList.add(subscription);
    }
}
