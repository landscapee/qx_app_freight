package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.GpsInfoEntity;
import qx.app.freight.qxappfreight.contract.SaveGpsInfoContract;
import qx.app.freight.qxappfreight.model.SaveGpsInfoModel;

public class SaveGpsInfoPresenter extends BasePresenter {
    public SaveGpsInfoPresenter(SaveGpsInfoContract.saveGpsInfoView saveGpsInfoView) {
        mRequestView = saveGpsInfoView;
        mRequestModel = new SaveGpsInfoModel();
    }

    public void saveGpsInfo(GpsInfoEntity gpsInfoEntity) {
        mRequestView.showNetDialog();
        ((SaveGpsInfoModel) mRequestModel).saveGpsInfo(gpsInfoEntity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((SaveGpsInfoContract.saveGpsInfoView) mRequestView).saveGpsInfoResult(result);
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
