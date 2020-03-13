package qx.app.freight.qxappfreight.model;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.PickGoodsRecordsBean;
import qx.app.freight.qxappfreight.contract.AddInfoContract;
import qx.app.freight.qxappfreight.contract.PickGoodsRecordsContract;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

/**
 * TODO : xxx
 * Created by zyy
 */
public class PickGoodsRecordsModel extends BaseModel implements PickGoodsRecordsContract.pickGoodsModel {
    @Override
    public void getOutboundList(String waybillId, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().getOutboundList(waybillId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }

    @Override
    public void revokeInboundDelevery(PickGoodsRecordsBean pickGoodsRecordsBean, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().revokeInboundDelevery(pickGoodsRecordsBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });
        mDisposableList.add(subscription);
    }
}
