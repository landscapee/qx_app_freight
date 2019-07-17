package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.UpdatePwdEntity;
import qx.app.freight.qxappfreight.contract.UpdatePWDContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class UpdatePWDModel extends BaseModel implements UpdatePWDContract.updatePWDModel {
    @Override
    public void updatePWD(UpdatePwdEntity entity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().updatePWD(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
