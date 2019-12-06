package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.GoodsIdEntity;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.AirlineRequireBean;
import qx.app.freight.qxappfreight.bean.response.DocumentsBean;
import qx.app.freight.qxappfreight.bean.response.ForwardInfoBean;
import qx.app.freight.qxappfreight.contract.AirlineRequireContract;
import qx.app.freight.qxappfreight.model.AirlineRequireModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class AirlineRequirePresenter extends BasePresenter {
    public AirlineRequirePresenter(AirlineRequireContract.airlineRequireView airlineRequireView) {
        mRequestView = airlineRequireView;
        mRequestModel = new AirlineRequireModel();
    }

    public void freightInfo(BaseFilterEntity model) {
        mRequestView.showNetDialog();
        ((AirlineRequireModel) mRequestModel).airlineRequire(model, new IResultLisenter<List<AirlineRequireBean>>() {
            @Override
            public void onSuccess(List<AirlineRequireBean> airlineRequireBeans) {
                ((AirlineRequireContract.airlineRequireView) mRequestView).airlineRequireResult(airlineRequireBeans);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void forwardInfo(String freightId) {
        mRequestView.showNetDialog();
        ((AirlineRequireModel) mRequestModel).forwardInfo(freightId, new IResultLisenter<ForwardInfoBean>() {
            @Override
            public void onSuccess(ForwardInfoBean forwardInfoBean) {
                ((AirlineRequireContract.airlineRequireView) mRequestView).forwardInfoResult(forwardInfoBean);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }
    public void getgetCommdityById(GoodsIdEntity goodsNames) {
        mRequestView.showNetDialog();
        ((AirlineRequireModel) mRequestModel).getgetCommdityById(goodsNames, new IResultLisenter<List<DocumentsBean>>() {
            @Override
            public void onSuccess(List<DocumentsBean> forwardInfoBean) {
                ((AirlineRequireContract.airlineRequireView) mRequestView).getgetCommdityByIdResult(forwardInfoBean);
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
