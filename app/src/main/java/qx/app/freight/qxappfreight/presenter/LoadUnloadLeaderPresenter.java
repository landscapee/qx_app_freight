package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.contract.LoadUnloadLeaderContract;
import qx.app.freight.qxappfreight.model.LoadUnloadLeaderModel;

/**
 * 装卸员小组长接收新任务Presenter
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
