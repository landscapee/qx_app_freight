package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.TransportListCommitEntity;
import qx.app.freight.qxappfreight.contract.ReturnCargoCommitContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class ReturnCargoCommitModel extends BaseModel implements ReturnCargoCommitContract.returnCargoCommitModel {
    @Override
    public void returnCargoCommit(TransportListCommitEntity transportListCommitEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().returnCargoCommit(transportListCommitEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
