package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.contract.AuditManifestContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class AuditManifestModel extends BaseModel implements AuditManifestContract.auditManifestModel {
    @Override
    public void auditManifest(BaseFilterEntity entity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().auditManifest(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void repartWriteLoading(BaseFilterEntity entity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().repartWriteLoading(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
