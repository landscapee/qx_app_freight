package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.ExceptionReportEntity;
import qx.app.freight.qxappfreight.contract.ExceptionReportContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;


public class ExceptionReportModel extends BaseModel implements ExceptionReportContract.exceptionReportModel {
    @Override
    public void exceptionReport(ExceptionReportEntity exceptionReportEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().exceptionReport(exceptionReportEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
