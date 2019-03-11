package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import okhttp3.MultipartBody;
import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.contract.UploadsContract;
import qx.app.freight.qxappfreight.model.UploadsModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class UploadsPresenter extends BasePresenter {

    public UploadsPresenter(UploadsContract.uploadsView uploadsView) {
        mRequestView = uploadsView;
        mRequestModel = new UploadsModel();
    }

    public void uploads(List<MultipartBody.Part> files) {
        mRequestView.showNetDialog();
        ((UploadsModel) mRequestModel).uploads(files, new IResultLisenter<Object>() {
            @Override
            public void onSuccess(Object result) {
                ((UploadsContract.uploadsView) mRequestView).uploadsResult(result);
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
