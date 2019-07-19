package qx.app.freight.qxappfreight.model;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.StorageCommitEntity;
import qx.app.freight.qxappfreight.contract.CommitReceiveListContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class CommitReceiveListModel extends BaseModel implements CommitReceiveListContract.commitReceiveListrModel {
    @Override
    public void commitReceiveListr(List<StorageCommitEntity> entity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().commitReceiveList(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
