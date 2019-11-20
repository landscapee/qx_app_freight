package qx.app.freight.qxappfreight.model;

import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.contract.DeleteCollectionInfoContract;

public class DeleteCollectionInfoModel extends BaseModel implements DeleteCollectionInfoContract.deleteCollectionInfoModel {
//    @Override
//    public void deleteCollectionInfo(String id, IResultLisenter lisenter) {
//        Disposable subscription = UpdateRepository.getInstance().deleteCollectionInfo(id)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(lisenter::onSuccess, throwable -> {
//                    lisenter.onFail(throwable.getMessage());
//                });
//        mDisposableList.add(subscription);
//    }
}
