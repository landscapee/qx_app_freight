package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.contract.LoadAndUnloadTodoContract;
import qx.app.freight.qxappfreight.contract.LoadUnloadLeaderContract;
import qx.app.freight.qxappfreight.model.LoadAndUnloadTodoModel;
import qx.app.freight.qxappfreight.model.LoadUnloadLeaderModel;
/**
 * 装卸员小组长任务列表Presenter
 */
public class LoadUnloadLeaderPresenter extends BasePresenter {

    public LoadUnloadLeaderPresenter(LoadUnloadLeaderContract.LoadUnloadLeaderView loadAndUnloadTodoView) {
        mRequestView = loadAndUnloadTodoView;
        mRequestModel = new LoadUnloadLeaderModel();
    }

    public void refuseTask(BaseFilterEntity baseFilterEntity) {
        mRequestView.showNetDialog();
        ((LoadUnloadLeaderModel) mRequestModel).refuseTask(baseFilterEntity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((LoadUnloadLeaderContract.LoadUnloadLeaderView) mRequestView).refuseTaskResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }
    public void getLoadUnloadLeaderList(String taskId) {
        mRequestView.showNetDialog();
        ((LoadUnloadLeaderModel) mRequestModel).getLoadUnloadLeaderList(taskId, new IResultLisenter<List<LoadAndUnloadTodoBean>>() {
            @Override
            public void onSuccess(List<LoadAndUnloadTodoBean> result) {
                ((LoadUnloadLeaderContract.LoadUnloadLeaderView) mRequestView).getLoadUnloadLeaderListResult(result);
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
        ((LoadUnloadLeaderModel) mRequestModel).slideTask(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((LoadUnloadLeaderContract.LoadUnloadLeaderView) mRequestView).slideTaskResult(result);
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
