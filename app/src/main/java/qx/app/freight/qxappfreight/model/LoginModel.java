package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.LoginEntity;
import qx.app.freight.qxappfreight.contract.LoginContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class LoginModel extends BaseModel implements LoginContract.loginModel {
    @Override
    public void login(LoginEntity loginEntity, final IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().login(loginEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
