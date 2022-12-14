package qx.app.freight.qxappfreight.model;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.GoodsIdEntity;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.contract.AirlineRequireContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

/**
 * TODO : xxx
 * Created by pr
 */
public class AirlineRequireModel extends BaseModel implements AirlineRequireContract.airlineRequireModel {

    @Override
    public void airlineRequire(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().airlineRequire(baseFilterEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void forwardInfo(String freightId, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().forwardInfo(freightId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void getgetCommdityById(GoodsIdEntity goodsNames, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getgetCommdityById(goodsNames)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
