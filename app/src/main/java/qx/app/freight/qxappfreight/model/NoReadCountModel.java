package qx.app.freight.qxappfreight.model;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.PageListEntity;
import qx.app.freight.qxappfreight.bean.request.UserBean;
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

    @Override
    public void loginOut(UserBean userBean, IResultLisenter lisenter) {
        Gson gson = new Gson();
        Map <String, String> map = new HashMap <>();
        String str = gson.toJson(userBean);
        map.put("param", str);
        Disposable subscription = UpdateRepository.getInstance().loginOutQxAi(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });

        mDisposableList.add(subscription);
    }
}
