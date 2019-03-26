package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PageListEntity;
import qx.app.freight.qxappfreight.contract.MessageContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class MessageModel extends BaseModel implements MessageContract.messageModel {
    @Override
    public void pageList(BaseFilterEntity baseEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().pageList(baseEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void msMessageView(BaseFilterEntity baseEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().msMessageView(baseEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

}
