package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.contract.EndInstallToDoContract;
import qx.app.freight.qxappfreight.model.EndInstallTodoModel;

public class EndInstallTodoPresenter extends BasePresenter {

    public EndInstallTodoPresenter(EndInstallToDoContract.IView iView) {
        mRequestView = iView;
        mRequestModel = new EndInstallTodoModel();
    }

    public void getEndInstallTodo(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((EndInstallTodoModel) mRequestModel).getEndInstallTodo(entity, new IResultLisenter<List<LoadAndUnloadTodoBean>>() {
            @Override
            public void onSuccess(List<LoadAndUnloadTodoBean> result) {
                ((EndInstallToDoContract.IView) mRequestView).getEndInstallTodoResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }
    public void slideTask(PerformTaskStepsEntity entity) {
        mRequestView.showNetDialog();
        ((EndInstallTodoModel) mRequestModel).slideTask(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((EndInstallToDoContract.IView) mRequestView).slideTaskResult(result);
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
