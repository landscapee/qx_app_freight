package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.GpsInfoEntity;
import qx.app.freight.qxappfreight.contract.SaveGpsInfoContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;


public class SaveGpsInfoModel extends BaseModel implements SaveGpsInfoContract.saveGpsInfoModel {
    @Override
    public void saveGpsInfo(GpsInfoEntity gpsInfoEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().saveGpsInfo(gpsInfoEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
