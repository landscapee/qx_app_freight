package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.PageListEntity;
import qx.app.freight.qxappfreight.contract.NoReadCountContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class NoReadCountModel extends BaseModel implements NoReadCountContract.noReadCountModel {
    @Override
    public void noReadCount(PageListEntity pageListEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().noReadCount(pageListEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });

        mDisposableList.add(subscription);
    }

    @Override
    public void noReadNoticeCount(String userId, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().noReadNoticeCount(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });

        mDisposableList.add(subscription);
    }
}
