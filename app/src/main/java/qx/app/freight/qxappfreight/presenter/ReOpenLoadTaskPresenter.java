package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.contract.ReOpenLoadTaskContract;
import qx.app.freight.qxappfreight.model.ReOpenLoadTaskModel;

/**
 * 重新开启装机任务presenter
 */
public class ReOpenLoadTaskPresenter extends BasePresenter {
    public ReOpenLoadTaskPresenter(ReOpenLoadTaskContract.ReOpenLoadTaskView getFlightAllReportInfoView) {
        mRequestView = getFlightAllReportInfoView;
        mRequestModel = new ReOpenLoadTaskModel();
    }

    public void reOpenLoadTask(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((ReOpenLoadTaskModel) mRequestModel).reOpenLoadTask(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((ReOpenLoadTaskContract.ReOpenLoadTaskView) mRequestView).reOpenLoadTaskResult(result);
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
