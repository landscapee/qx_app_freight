package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.IOManifestBean;
import qx.app.freight.qxappfreight.bean.response.SmInventoryEntryandexit;
import qx.app.freight.qxappfreight.contract.IOManiFestContract;
import qx.app.freight.qxappfreight.contract.SubmitIOManiFestContract;
import qx.app.freight.qxappfreight.model.IOManifestModel;
import qx.app.freight.qxappfreight.model.SubmitIOManifestModel;

/**
 * TODO : 提交 出库或者入库 单
 * Created by
 */
public class SubmitIOManifestPresenter extends BasePresenter {

    public SubmitIOManifestPresenter(SubmitIOManiFestContract.submitIOManiFestView ioManiFestView) {
        mRequestView = ioManiFestView;
        mRequestModel = new SubmitIOManifestModel();
    }

    public void submitIOManifestList(SmInventoryEntryandexit entity) {
        mRequestView.showNetDialog();
        ((SubmitIOManifestModel) mRequestModel).submitIOManiFest(entity, new IResultLisenter <String>() {
            @Override
            public void onSuccess(String result) {
                ((SubmitIOManiFestContract.submitIOManiFestView) mRequestView).setSubmitIOManiFestResult(result);
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
