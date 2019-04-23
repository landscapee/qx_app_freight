package qx.app.freight.qxappfreight.model;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.ChangeWaybillEntity;
import qx.app.freight.qxappfreight.bean.request.DeclareWaybillEntity;
import qx.app.freight.qxappfreight.bean.response.DeclareWaybillBean;
import qx.app.freight.qxappfreight.contract.DeliveryVerifyContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

/**
 * 换单审核 model
 * create by guohao -2019/4/22
 */
public class DeliveryVerifyModel extends BaseModel implements DeliveryVerifyContract.deliveryVerifyModel {
    @Override
    public void deliveryVerify(DeclareWaybillEntity declareWaybillEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getChangeWaybill(declareWaybillEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void changeSubmit(ChangeWaybillEntity changeWaybillEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().changeSubmit(changeWaybillEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                   lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
