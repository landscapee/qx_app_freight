package qx.app.freight.qxappfreight.model;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.ForkliftWorkingCostBean;
import qx.app.freight.qxappfreight.contract.AddInfoContract;
import qx.app.freight.qxappfreight.contract.ForkliftCostContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

/**
 * TODO : xxx
 * Created by pr
 */
public class ForkliftModel extends BaseModel implements ForkliftCostContract.forkliftModel {
    @Override
    public void addForklift(List <ForkliftWorkingCostBean> forkliftWorkingCostBeans, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().addWaybillForklift(forkliftWorkingCostBeans)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void getForklifts(String waybillId, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getWaybillForklift(waybillId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
