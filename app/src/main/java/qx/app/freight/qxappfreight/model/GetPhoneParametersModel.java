package qx.app.freight.qxappfreight.model;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.PhoneParametersEntity;
import qx.app.freight.qxappfreight.contract.GetPhoneParametersContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class GetPhoneParametersModel extends BaseModel implements GetPhoneParametersContract.getPhoneParametersModel {
    @Override
    public void getPhoneParameters(PhoneParametersEntity entity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getPhoneParameters(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
