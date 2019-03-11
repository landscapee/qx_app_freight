package qx.app.freight.qxappfreight.model;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.FightScooterSubmitEntity;
import qx.app.freight.qxappfreight.bean.request.GetScooterListInfoEntity;
import qx.app.freight.qxappfreight.contract.GetScooterListInfoContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

/**
 * TODO : xxx
 * Created by pr
 */
public class GetScooterListInfoModel extends BaseModel implements GetScooterListInfoContract.getScooterListInfoModel {
    @Override
    public void getScooterListInfo(GetScooterListInfoEntity scooterListInfoEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getScooterListInfo(scooterListInfoEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(lisenter::onSuccess, throwable -> {
                lisenter.onFail(throwable.getMessage());
            });
        mDisposableList.add(subscription);
}

    @Override
    public void fightScooterSubmit(FightScooterSubmitEntity fightScooterSubmitEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().fightScooterSubmit(fightScooterSubmitEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
