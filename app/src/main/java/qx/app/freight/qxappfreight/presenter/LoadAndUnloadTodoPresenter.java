package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.request.TaskClearEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.contract.LoadAndUnloadTodoContract;
import qx.app.freight.qxappfreight.model.LoadAndUnloadTodoModel;

public class LoadAndUnloadTodoPresenter extends BasePresenter {

    public LoadAndUnloadTodoPresenter(LoadAndUnloadTodoContract.loadAndUnloadTodoView loadAndUnloadTodoView) {
        mRequestView = loadAndUnloadTodoView;
        mRequestModel = new LoadAndUnloadTodoModel();
    }

    public void LoadAndUnloadTodo(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((LoadAndUnloadTodoModel) mRequestModel).loadAndUnloadTodo(entity, new IResultLisenter<List<LoadAndUnloadTodoBean>>() {
            @Override
            public void onSuccess(List<LoadAndUnloadTodoBean> result) {
                ((LoadAndUnloadTodoContract.loadAndUnloadTodoView) mRequestView).loadAndUnloadTodoResult(result);
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
        ((LoadAndUnloadTodoModel) mRequestModel).slideTask(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((LoadAndUnloadTodoContract.loadAndUnloadTodoView) mRequestView).slideTaskResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }
    public void startClearTask(TaskClearEntity entity) {
        mRequestView.showNetDialog();
        ((LoadAndUnloadTodoModel) mRequestModel).startClearTask(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((LoadAndUnloadTodoContract.loadAndUnloadTodoView) mRequestView).slideTaskResult(result);
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
