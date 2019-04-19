package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.contract.GetWayBillInfoByIdContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class GetWayBillInfoByIdModel extends BaseModel implements GetWayBillInfoByIdContract.getWayBillInfoByIdModel {
    @Override
    public void getWayBillInfoById(String id, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getWayBillInfoById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
