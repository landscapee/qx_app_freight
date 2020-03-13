package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.contract.GetFlightAllReportInfoContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

/**
 * 结载人员获取装机单model
 */
public class GetFlightAllReportInfoModel extends BaseModel implements GetFlightAllReportInfoContract.getFlightAllReportInfoModel {
    @Override
    public void getFlightAllReportInfo(BaseFilterEntity entity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getFlightAllReportInfo(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
