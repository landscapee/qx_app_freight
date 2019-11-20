package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.IOManifestBean;
import qx.app.freight.qxappfreight.contract.IOManiFestContract;
import qx.app.freight.qxappfreight.model.IOManifestModel;

/**
 * TODO : 获取 出库或者入库 单
 * Created by
 */
public class IOManifestPresenter extends BasePresenter {

    public IOManifestPresenter(IOManiFestContract.ioManiFestView ioManiFestView) {
        mRequestView = ioManiFestView;
        mRequestModel = new IOManifestModel();
    }

    public void getIOManifestList(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((IOManifestModel) mRequestModel).getManifestInfo(entity, new IResultLisenter <IOManifestBean>() {
            @Override
            public void onSuccess(IOManifestBean result) {
                ((IOManiFestContract.ioManiFestView) mRequestView).setManifestResult(result.getRecords());
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
