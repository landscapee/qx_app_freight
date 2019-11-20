package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.GroupBoardRequestEntity;
import qx.app.freight.qxappfreight.contract.GroupBoardToDoContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

/**
 * TODO : xxx
 * Created by pr
 */
public class GroupBoardToDoListModel extends BaseModel implements GroupBoardToDoContract.GroupBoardToDoModel {
    @Override
    public void getGroupBoardToDo(GroupBoardRequestEntity model, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getGroupBoardToDo(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void getScooterByScooterCode(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getScooterByScooterCode(baseFilterEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void searchWaybillByWaybillCode(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter) {

        Disposable subscription = UpdateRepository.getInstance().searchWaybillByWaybillCode(baseFilterEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
