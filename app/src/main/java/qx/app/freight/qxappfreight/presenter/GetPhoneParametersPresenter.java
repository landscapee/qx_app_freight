package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.PhoneParametersEntity;
import qx.app.freight.qxappfreight.bean.response.FlightBean;
import qx.app.freight.qxappfreight.contract.GetPhoneParametersContract;
import qx.app.freight.qxappfreight.model.GetPhoneParametersModel;

public class GetPhoneParametersPresenter extends BasePresenter {
    public GetPhoneParametersPresenter(GetPhoneParametersContract.getPhoneParametersView getPhoneParametersView) {
        mRequestView = getPhoneParametersView;
        mRequestModel = new GetPhoneParametersModel();
    }

    public void getPhoneParameters(PhoneParametersEntity model) {
        mRequestView.showNetDialog();
        ((GetPhoneParametersModel) mRequestModel).getPhoneParameters(model, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String result) {
                ((GetPhoneParametersContract.getPhoneParametersView) mRequestView).getPhoneParametersResult(result);
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
