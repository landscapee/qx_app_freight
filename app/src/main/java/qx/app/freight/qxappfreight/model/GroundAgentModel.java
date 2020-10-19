package qx.app.freight.qxappfreight.model;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.ScooterTransitBean;
import qx.app.freight.qxappfreight.contract.GroundAgentContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class GroundAgentModel extends BaseModel implements GroundAgentContract.GroundAgentModel {

    @Override
    public void newScooter(ScooterTransitBean scooter, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().newScooter(scooter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void getAllAgent(IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getAllAgent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
