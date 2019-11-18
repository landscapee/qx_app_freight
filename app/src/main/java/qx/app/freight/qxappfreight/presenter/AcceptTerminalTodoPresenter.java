package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.AcceptTerminalTodoBean;
import qx.app.freight.qxappfreight.contract.AcceptTerminalTodoContract;
import qx.app.freight.qxappfreight.model.AcceptTerminalTodoModel;

public class AcceptTerminalTodoPresenter extends BasePresenter {

    public AcceptTerminalTodoPresenter(AcceptTerminalTodoContract.acceptTerminalTodoView acceptTerminalTodoView) {
        mRequestView = acceptTerminalTodoView;
        mRequestModel = new AcceptTerminalTodoModel();
    }

    public void acceptTerminalTodo(BaseFilterEntity model) {
        mRequestView.showNetDialog();
        ((AcceptTerminalTodoModel) mRequestModel).acceptTerminalTodo(model,new IResultLisenter<List<AcceptTerminalTodoBean>>() {
            @Override
            public void onSuccess(List<AcceptTerminalTodoBean> result) {
                ((AcceptTerminalTodoContract.acceptTerminalTodoView) mRequestView).acceptTerminalTodoResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }

        });
    }
}
