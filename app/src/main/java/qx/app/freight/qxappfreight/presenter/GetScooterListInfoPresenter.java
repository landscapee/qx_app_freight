package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.FightScooterSubmitEntity;
import qx.app.freight.qxappfreight.bean.request.GetScooterListInfoEntity;
import qx.app.freight.qxappfreight.bean.response.GetScooterListInfoBean;
import qx.app.freight.qxappfreight.contract.GetScooterListInfoContract;
import qx.app.freight.qxappfreight.model.GetScooterListInfoModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class GetScooterListInfoPresenter extends BasePresenter {

    public GetScooterListInfoPresenter(GetScooterListInfoContract.getScooterListInfoView getScooterListInfoView) {
        mRequestView = getScooterListInfoView;
        mRequestModel = new GetScooterListInfoModel();
    }

    public void getScooterListInfo(GetScooterListInfoEntity getScooterListInfoEntity) {
        mRequestView.showNetDialog();
        ((GetScooterListInfoModel) mRequestModel).getScooterListInfo(getScooterListInfoEntity, new IResultLisenter<GetScooterListInfoBean>() {
            @Override
            public void onSuccess(GetScooterListInfoBean getScooterListInfoBean) {
                ((GetScooterListInfoContract.getScooterListInfoView) mRequestView).getScooterListInfoResult(getScooterListInfoBean);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }
    public void freightInfo(FightScooterSubmitEntity entity) {
        mRequestView.showNetDialog();
        ((GetScooterListInfoModel) mRequestModel).fightScooterSubmit(entity, new IResultLisenter<String>() {
            @Override
            public void onSuccess(String string) {
                ((GetScooterListInfoContract.getScooterListInfoView) mRequestView).fightScooterSubmitResult(string);
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
