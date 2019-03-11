package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.AddInfoEntity;
import qx.app.freight.qxappfreight.contract.AddInfoContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

/**
 * TODO : xxx
 * Created by pr
 */
public class AddInfoModel extends BaseModel implements AddInfoContract.addInfoModel {
//    @Override
//    public void addInfo(AddInfoEntity addInfoEntity, IResultLisenter lisenter) {
//        Disposable subscription = UpdateRepository.getInstance().addInfo(addInfoEntity)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(lisenter::onSuccess, throwable -> {
//                    lisenter.onFail(throwable.getMessage());
//                });
//        mDisposableList.add(subscription);
//    }
}
