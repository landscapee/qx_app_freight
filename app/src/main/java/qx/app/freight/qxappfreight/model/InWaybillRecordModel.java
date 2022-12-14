package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordGetEntity;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordSubmitNewEntity;
import qx.app.freight.qxappfreight.contract.InWaybillRecordContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

/**
 * 进港分拣
 * <p>
 * create by guohao - 2019/4/25
 */
public class InWaybillRecordModel extends BaseModel implements InWaybillRecordContract.inWaybillRecordModel {

    @Override
    public void getList(InWaybillRecordGetEntity entity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getInWaybillRecrodList(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void submit(InWaybillRecordSubmitNewEntity entity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().submitWillbillRecord(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void deleteById(String id, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().deleteInWayBillRecordById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void allGoodsArrived(InWaybillRecordSubmitNewEntity.SingleLineBean data, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().allGoodsArrived(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
