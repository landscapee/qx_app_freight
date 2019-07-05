package qx.app.freight.qxappfreight.model;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.SaveOrUpdateEntity;
import qx.app.freight.qxappfreight.contract.SaveOrUpdateContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class SaveOrUpdateModel extends BaseModel implements SaveOrUpdateContract.saveOrUpdateModel {
    @Override
    public void saveOrUpdate(SaveOrUpdateEntity entity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().saveOrUpdate(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
