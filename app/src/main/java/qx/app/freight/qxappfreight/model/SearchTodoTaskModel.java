package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.contract.SearchTodoTaskContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

/**
 * Created by pr on 2019/5/17 16:17 @COPYRIGHT 青霄科技
 *
 * @title：
 * @description：
 */
public class SearchTodoTaskModel extends BaseModel implements SearchTodoTaskContract.searchTodoTaskModel {
    @Override
    public void searchTodoTask(BaseFilterEntity entity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().searchTodoTask(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
