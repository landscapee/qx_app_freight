package qx.app.freight.qxappfreight.presenter;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.contract.ScanScooterCheckUsedContract;
import qx.app.freight.qxappfreight.model.ScanScooterCheckUsedModel;

/**
 * 检查板车二维码是否在数据库中处于被占用的状态
 */
public class ScanScooterCheckUsedPresenter extends BasePresenter {

    public ScanScooterCheckUsedPresenter(ScanScooterCheckUsedContract.ScanScooterCheckView view) {
        mRequestView = view;
        mRequestModel = new ScanScooterCheckUsedModel();
    }

    public void checkScooterCode(String scooterCode,String flightId,int scSubCategory) {
        mRequestView.showNetDialog();
        ((ScanScooterCheckUsedModel) mRequestModel).checkScooterCode(scooterCode, flightId, scSubCategory+"", new IResultLisenter<BaseEntity<Object>>() {
            @Override
            public void onSuccess(BaseEntity<Object> result) {
                ((ScanScooterCheckUsedContract.ScanScooterCheckView) mRequestView).checkScooterCodeResult(result);
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
