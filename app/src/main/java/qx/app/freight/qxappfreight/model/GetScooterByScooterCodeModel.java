package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.ReturnWeighingEntity;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.contract.GetScooterByScooterCodeContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class GetScooterByScooterCodeModel extends BaseModel implements GetScooterByScooterCodeContract.getScooterByScooterCodeModel {

    @Override
    public void getInfosByFlightId(BaseFilterEntity entity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getInfosByFlightId(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void getScooterByScooterCode(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getScooterByScooterCode(baseFilterEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void saveScooter(GetInfosByFlightIdBean getInfosByFlightIdBean, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().saveScooter(getInfosByFlightIdBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void returnWeighing(ReturnWeighingEntity getInfosByFlightIdBean, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().returnWeighing(getInfosByFlightIdBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void getWeight(String pbName, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getWeight(pbName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void returnGroupScooterTask(GetInfosByFlightIdBean scooter, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().returnGroupScooterTask(scooter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
