package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.AddInfoEntity;
import qx.app.freight.qxappfreight.contract.AddInfoContract;
import qx.app.freight.qxappfreight.contract.GetWeightContract;
import qx.app.freight.qxappfreight.model.AddInfoModel;
import qx.app.freight.qxappfreight.model.GetWeightModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class GetWeightPresenter extends BasePresenter {

    public GetWeightPresenter(GetWeightContract.getWeightView getWeightView) {
        mRequestView = getWeightView;
        mRequestModel = new GetWeightModel();
    }

    public void getWeight(String pbName) {
        mRequestView.showNetDialog();
        ((GetWeightModel) mRequestModel).getWeight(pbName, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((GetWeightContract.getWeightView) mRequestView).getWeightResult(result);
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
