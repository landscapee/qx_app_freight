package qx.app.freight.qxappfreight.model;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.InventoryDetailEntity;
import qx.app.freight.qxappfreight.contract.AddInventoryDetailContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class AddInventoryDetailModel extends BaseModel implements AddInventoryDetailContract.addInventoryDetailModel {
    @Override
    public void addInventoryDetail(List<InventoryDetailEntity> list, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().addInventoryDetail(list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void uploads(List<MultipartBody.Part> files, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().upLoads(files)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void listWaybillCode(String code,String taskId, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().listWaybillCode(code,taskId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
