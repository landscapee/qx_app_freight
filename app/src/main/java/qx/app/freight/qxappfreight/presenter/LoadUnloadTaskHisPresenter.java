package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.contract.LoadUnloadTaskHisContract;
import qx.app.freight.qxappfreight.model.LoadUnloadTaskHisModel;

public class LoadUnloadTaskHisPresenter extends BasePresenter {

    public LoadUnloadTaskHisPresenter(LoadUnloadTaskHisContract.loadUnloadTaskHisView addScooterView) {
        mRequestView = addScooterView;
        mRequestModel = new LoadUnloadTaskHisModel();
    }

    public void loadUnloadTaskHis(BaseFilterEntity operatorId) {
        mRequestView.showNetDialog();
        ((LoadUnloadTaskHisModel) mRequestModel).loadUnloadTaskHis(operatorId, new IResultLisenter<List<LoadAndUnloadTodoBean>>() {
            @Override
            public void onSuccess(List<LoadAndUnloadTodoBean> result) {
                ((LoadUnloadTaskHisContract.loadUnloadTaskHisView) mRequestView).loadUnloadTaskHisResult(result);
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
