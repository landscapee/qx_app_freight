package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.OnlineStutasEntity;
import qx.app.freight.qxappfreight.bean.response.LoginResponseBean;
import qx.app.freight.qxappfreight.contract.OnlineStatusContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;


public class OnlineStatusModel extends BaseModel implements OnlineStatusContract.OnlineStatusModel {

    @Override
    public void onlineStatus(OnlineStutasEntity userInfo, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().onlineStatus(userInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
