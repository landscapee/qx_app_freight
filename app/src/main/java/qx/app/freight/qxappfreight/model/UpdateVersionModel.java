package qx.app.freight.qxappfreight.model;


import java.util.Map;

import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.contract.UpdateVersionContract;

public class UpdateVersionModel extends BaseModel implements UpdateVersionContract.updateModel {
    @Override
    public void updateVersion(Map<String,String> params,IResultLisenter lisenter) {
//        Disposable subscription = UpdateRepository.getInstance().updateVersion(params)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(lisenter::onSuccess, throwable -> {
//                    lisenter.onFail(throwable.getMessage());
//                });
//        mDisposableList.add(subscription);
    }
}
