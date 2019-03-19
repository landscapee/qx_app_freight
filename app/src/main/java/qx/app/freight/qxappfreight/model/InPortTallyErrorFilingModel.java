package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.ErrorFilingEntity;
import qx.app.freight.qxappfreight.contract.InPortTallyErrorFilingContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class InPortTallyErrorFilingModel extends BaseModel implements InPortTallyErrorFilingContract.InPortErrorFilingModel {

    @Override
    public void errorFiling(ErrorFilingEntity entity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().errorFiling(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
