package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import okhttp3.MultipartBody;
import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.AddInfoEntity;
import qx.app.freight.qxappfreight.contract.AddInfoContract;
import qx.app.freight.qxappfreight.contract.UploadsContract;
import qx.app.freight.qxappfreight.model.AddInfoModel;
import qx.app.freight.qxappfreight.model.UploadsModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class AddInfoPresenter extends BasePresenter {

    public AddInfoPresenter(AddInfoContract.addInfoView addInfoView) {
        mRequestView = addInfoView;
        mRequestModel = new AddInfoModel();
    }

//    public void addInfo(AddInfoEntity addInfoEntity) {
//        mRequestView.showNetDialog();
//        ((AddInfoModel) mRequestModel).addInfo(addInfoEntity, new IResultLisenter<String>() {
//            @Override
//            public void onSuccess(String result) {
//                ((AddInfoContract.addInfoView) mRequestView).addInfoResult(result);
//                mRequestView.dissMiss();
//            }
//
//            @Override
//            public void onFail(String error) {
//                mRequestView.toastView(error);
//                mRequestView.dissMiss();
//            }
//        });
//    }
}
