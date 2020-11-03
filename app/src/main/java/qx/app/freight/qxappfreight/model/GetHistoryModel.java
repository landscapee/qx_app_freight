package qx.app.freight.qxappfreight.model;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.SearchFilghtEntity;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.SearchFlightInfoBean;
import qx.app.freight.qxappfreight.contract.GetHistoryContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class GetHistoryModel extends BaseModel implements GetHistoryContract.getHistoryModel {
    @Override
    public void getHistory(BaseFilterEntity entity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getHistoryScootersPage(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void searchFlightsByKey(SearchFilghtEntity entity, IResultLisenter<List<SearchFlightInfoBean>> lisenter) {
        Disposable subscription = UpdateRepository.getInstance().searchFlightsByKey(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
