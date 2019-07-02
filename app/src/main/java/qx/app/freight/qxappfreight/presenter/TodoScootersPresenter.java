package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.TodoScootersEntity;
import qx.app.freight.qxappfreight.bean.response.GetTodoScootersBean;
import qx.app.freight.qxappfreight.contract.TodoScootersContract;
import qx.app.freight.qxappfreight.model.TodoScootersModel;

public class TodoScootersPresenter extends BasePresenter {

    public TodoScootersPresenter(TodoScootersContract.todoScootersView todoScootersView) {
        mRequestView = todoScootersView;
        mRequestModel = new TodoScootersModel();
    }

    public void todoScooters(TodoScootersEntity model) {
        mRequestView.showNetDialog();
        ((TodoScootersModel) mRequestModel).todoScooters(model, new IResultLisenter<List<GetTodoScootersBean>>() {
            @Override
            public void onSuccess(List<GetTodoScootersBean> airlineRequireBeans) {
                ((TodoScootersContract.todoScootersView) mRequestView).todoScootersResult(airlineRequireBeans);
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
