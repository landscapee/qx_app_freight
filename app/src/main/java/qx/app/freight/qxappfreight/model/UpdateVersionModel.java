package qx.app.freight.qxappfreight.model;


import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.contract.UpdateVersionContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class UpdateVersionModel extends BaseModel implements UpdateVersionContract.updateModel {
    @Override
    public void updateVersion(IResultLisenter lisenter) {
        Map<String, String> map = new HashMap<>();
        map.put("param", "");
        Disposable subscription = UpdateRepository.getInstance().updateVersion(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
