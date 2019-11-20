package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.contract.ReportTaskHisContract;
import qx.app.freight.qxappfreight.model.ReportTaskHisModel;

public class ReportTaskHisPresenter extends BasePresenter {

    public ReportTaskHisPresenter(ReportTaskHisContract.eportTaskHisView addScooterView) {
        mRequestView = addScooterView;
        mRequestModel = new ReportTaskHisModel();
    }

    public void reportTaskHis(BaseFilterEntity operatorId) {
        mRequestView.showNetDialog();
        ((ReportTaskHisModel) mRequestModel).reportTaskHis(operatorId, new IResultLisenter<List<LoadAndUnloadTodoBean>>() {
            @Override
            public void onSuccess(List<LoadAndUnloadTodoBean> result) {
                ((ReportTaskHisContract.eportTaskHisView) mRequestView).reportTaskHisResult(result);
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
