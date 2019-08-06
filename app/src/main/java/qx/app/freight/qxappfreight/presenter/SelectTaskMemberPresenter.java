package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.SelectTaskMemberEntity;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.contract.LoadUnloadLeaderContract;
import qx.app.freight.qxappfreight.contract.SelectTaskMemberContract;
import qx.app.freight.qxappfreight.model.LoadUnloadLeaderModel;
import qx.app.freight.qxappfreight.model.SelectTaskMemberModel;

/**
 * 装卸员小组长选择任务人员Presenter
 */
public class SelectTaskMemberPresenter extends BasePresenter {

    public SelectTaskMemberPresenter(SelectTaskMemberContract.SelectTaskMemberView loadAndUnloadTodoView) {
        mRequestView = loadAndUnloadTodoView;
        mRequestModel = new SelectTaskMemberModel();
    }

    public void getLoadUnloadLeaderList(String taskId) {
        mRequestView.showNetDialog();
        ((SelectTaskMemberModel) mRequestModel).getLoadUnloadLeaderList(taskId, new IResultLisenter<List<SelectTaskMemberEntity>>() {
            @Override
            public void onSuccess(List<SelectTaskMemberEntity> result) {
                ((SelectTaskMemberContract.SelectTaskMemberView) mRequestView).getLoadUnloadLeaderListResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }
    public void selectMember(BaseFilterEntity baseFilterEntity) {
        mRequestView.showNetDialog();
        ((SelectTaskMemberModel) mRequestModel).selectMember(baseFilterEntity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((SelectTaskMemberContract.SelectTaskMemberView) mRequestView).selectMemberResult(result);
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
