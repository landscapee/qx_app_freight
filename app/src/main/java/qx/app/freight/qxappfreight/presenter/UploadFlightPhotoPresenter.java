package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.AddInfoEntity;
import qx.app.freight.qxappfreight.bean.request.FlightPhotoEntity;
import qx.app.freight.qxappfreight.contract.AddInfoContract;
import qx.app.freight.qxappfreight.contract.UpLoadFlightPhotoContract;
import qx.app.freight.qxappfreight.model.AddInfoModel;
import qx.app.freight.qxappfreight.model.UploadFlightPhotoModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class UploadFlightPhotoPresenter extends BasePresenter {

    public UploadFlightPhotoPresenter(UpLoadFlightPhotoContract.uploadFlightPhotoView uploadFlightPhotoView) {
        mRequestView = uploadFlightPhotoView;
        mRequestModel = new UploadFlightPhotoModel();
    }

    public void uploadFlightPhoto(FlightPhotoEntity flightPhotoEntity) {
        mRequestView.showNetDialog();
        ((UploadFlightPhotoModel) mRequestModel).uploadFlightPhoto(flightPhotoEntity, new IResultLisenter <String>() {
            @Override
            public void onSuccess(String result) {
                ((UpLoadFlightPhotoContract.uploadFlightPhotoView) mRequestView).uploadFlightPhotoResult(result);
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
