package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.contract.ScooterOperateContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

/**
 * Created by guohao on 2019/5/8 15:43 @COPYRIGHT 青霄科技
 *
 * @title：
 * @description：
 */
public class ScooterOperateModel extends BaseModel implements ScooterOperateContract.scooterOperateModel {

    /**
     * 回退到预配 -- guohao
     * @param entity
     * @param lisenter
     */
    @Override
    public void returnToPrematching(BaseFilterEntity entity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().returnPrematching(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
