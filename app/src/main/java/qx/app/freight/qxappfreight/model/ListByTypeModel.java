package qx.app.freight.qxappfreight.model;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.contract.ListByTypeContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class ListByTypeModel extends BaseModel implements ListByTypeContract.listByTypeModel {
    @Override
    public void listByType(BaseFilterEntity entity , IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().listByType(entity) // 1：集装箱类型
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
