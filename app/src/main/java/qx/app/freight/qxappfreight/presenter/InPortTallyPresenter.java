package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.InportTallyBean;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.InPortTallyCommitEntity;
import qx.app.freight.qxappfreight.bean.response.InPortResponseBean;
import qx.app.freight.qxappfreight.contract.InPortTallyContract;
import qx.app.freight.qxappfreight.model.InPortTallyModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class InPortTallyPresenter extends BasePresenter {

    public InPortTallyPresenter(InPortTallyContract.InPortTallyListView view) {
        mRequestView = view;
        mRequestModel = new InPortTallyModel();
    }

    public void getInPortTallyList(BaseFilterEntity model) {
        mRequestView.showNetDialog();
        ((InPortTallyModel) mRequestModel).getInPortTallyList(model, new IResultLisenter<InPortResponseBean>() {
            @Override
            public void onSuccess(InPortResponseBean result) {
                ((InPortTallyContract.InPortTallyListView) mRequestView).getInPortTallyListResult(result.getList());
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void inPortTallyCommit(InPortTallyCommitEntity model) {
        mRequestView.showNetDialog();
        ((InPortTallyModel) mRequestModel).inPortTallyCommit(model, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((InPortTallyContract.InPortTallyListView) mRequestView).inPortTallyCommitResult(result);
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
