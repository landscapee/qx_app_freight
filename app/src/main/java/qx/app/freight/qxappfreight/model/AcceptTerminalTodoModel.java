package qx.app.freight.qxappfreight.model;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.app.BaseModel;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.AcceptTerminalTodoBean;
import qx.app.freight.qxappfreight.contract.AcceptTerminalTodoContract;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.httpUtils.UpdateRepository;

public class AcceptTerminalTodoModel extends BaseModel implements AcceptTerminalTodoContract.acceptTerminalTodoModel {
    @Override
    public void acceptTerminalTodo(BaseFilterEntity model, IResultLisenter lisenter) {
        Disposable subscription = UpdateRepository.getInstance().acceptTerminalTodo(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lisenter::onSuccess, throwable -> {
                    lisenter.onFail(throwable.getMessage());
                });

        mDisposableList.add(subscription);
    }
}
