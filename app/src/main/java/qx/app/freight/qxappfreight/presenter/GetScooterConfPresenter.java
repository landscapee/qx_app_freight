package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.ScooterConfBean;
import qx.app.freight.qxappfreight.contract.ScooterConfContract;
import qx.app.freight.qxappfreight.model.GetScooterConfModel;

/**
 * TODO : xxx
 * Created by
 */
public class GetScooterConfPresenter extends BasePresenter {

    public GetScooterConfPresenter(ScooterConfContract.scooterConfView scooterConfView) {
        mRequestView = scooterConfView;
        mRequestModel = new GetScooterConfModel();
    }

    public void getScooterConf(String type) {
        mRequestView.showNetDialog();
        ((GetScooterConfModel) mRequestModel).getScooterConf(type, new IResultLisenter <List <ScooterConfBean.ScooterConf>>() {
            @Override
            public void onSuccess(List <ScooterConfBean.ScooterConf> result) {
                ((ScooterConfContract.scooterConfView) mRequestView).getScooterConfResult(result);
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
