package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.AddInfoEntity;
import qx.app.freight.qxappfreight.bean.response.ForkliftWorkingCostBean;
import qx.app.freight.qxappfreight.contract.AddInfoContract;
import qx.app.freight.qxappfreight.contract.ForkliftCostContract;
import qx.app.freight.qxappfreight.model.AddInfoModel;
import qx.app.freight.qxappfreight.model.ForkliftModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class ForkLiftPresenter extends BasePresenter {

    public ForkLiftPresenter(ForkliftCostContract.forkliftView forkliftView) {
        mRequestView = forkliftView;
        mRequestModel = new ForkliftModel();
    }

    public void getForklifts(String waybillId) {
        mRequestView.showNetDialog();
        ((ForkliftModel) mRequestModel).getForklifts(waybillId, new IResultLisenter <List<ForkliftWorkingCostBean>>() {
            @Override
            public void onSuccess(List<ForkliftWorkingCostBean> result) {
                ((ForkliftCostContract.forkliftView) mRequestView).getForkliftsResult(result);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void addForklift( List<ForkliftWorkingCostBean> entity) {
        mRequestView.showNetDialog();
        ((ForkliftModel) mRequestModel).addForklift(entity, new IResultLisenter <String>() {
            @Override
            public void onSuccess(String result) {
                ((ForkliftCostContract.forkliftView) mRequestView).addForkliftResult(result);
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
