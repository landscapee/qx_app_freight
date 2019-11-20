package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.contract.ScooterInfoListContract;
import qx.app.freight.qxappfreight.exception.DefaultException;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

/**
 * TODO : xxx
 * Created by pr
 */
public class ScooterInfoListModel extends BaseModel implements ScooterInfoListContract.scooterInfoListModel {
    @Override
    public void scooterInfoList(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().scooterInfoList(baseFilterEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
    @Override
    public void scooterInfoListForReceive(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().scooterInfoListForReceive(baseFilterEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void exist(String scooterId, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().exist(scooterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    if (DefaultException.DATA_NULL_EXCEPTION.equals(throwable.getMessage())){
                        MyAgentListBean mExistBean = new MyAgentListBean();
                        mExistBean.setUldWeight(0);
                        lisenter.onSuccess(mExistBean);
                    }
//                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void addInfo(MyAgentListBean myAgentListBean, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().addInfo(myAgentListBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

}
