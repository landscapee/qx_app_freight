package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.contract.LoadUnloadLeaderToDoContract;
import qx.app.freight.qxappfreight.model.LoadUnloadLeaderToDoModel;

/**
 * 装卸员小组长选择任务人员Presenter
 */
public class LoadUnloadToDoLeaderPresenter extends BasePresenter {

    public LoadUnloadToDoLeaderPresenter(LoadUnloadLeaderToDoContract.LoadUnloadLeaderToDoView loadAndUnloadTodoView) {
        mRequestView = loadAndUnloadTodoView;
        mRequestModel = new LoadUnloadLeaderToDoModel();
    }

    public void getLoadUnloadLeaderToDo(BaseFilterEntity baseFilterEntity) {
        mRequestView.showNetDialog();
        ((LoadUnloadLeaderToDoModel) mRequestModel).getLoadUnloadLeaderToDo(baseFilterEntity, new IResultLisenter<List<LoadAndUnloadTodoBean>>() {
            @Override
            public void onSuccess(List<LoadAndUnloadTodoBean> result) {
                ((LoadUnloadLeaderToDoContract.LoadUnloadLeaderToDoView) mRequestView).getLoadUnloadLeaderToDoResult(result);
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
        ((LoadUnloadLeaderToDoModel) mRequestModel).slideTask(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((LoadUnloadLeaderToDoContract.LoadUnloadLeaderToDoView) mRequestView).slideTaskResult(result);
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
