package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.contract.GetWayBillInfoByIdDataContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class GetWayBillInfoByIdDataModel extends BaseModel implements GetWayBillInfoByIdDataContract.getWayBillInfoByIdDataModel {
    @Override
    public void getWayBillInfoByIdData(String waybillCode, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getWayBillInfoByIdData(waybillCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void getWaybillInfo(String id, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getWaybillInfo(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
