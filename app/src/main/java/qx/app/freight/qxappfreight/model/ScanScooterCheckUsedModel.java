package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.contract.ScanScooterCheckUsedContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

/**
 * 检查板车二维码是否在数据库中处于被占用的状态
 */
public class ScanScooterCheckUsedModel extends BaseModel implements ScanScooterCheckUsedContract.ScanScooterCheckModel {

    @Override
    public void checkScooterCode(String scooterCode,String flightId,String scSubCategory, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().checkScooterCode(scooterCode,flightId,scSubCategory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
