package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.contract.ReturnTransportationListContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class ReturnTransportationListModel extends BaseModel implements ReturnTransportationListContract.returnTransportationListModel {
    @Override
    public void returnTransportationList(BaseFilterEntity entity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().returnTransportationList(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
