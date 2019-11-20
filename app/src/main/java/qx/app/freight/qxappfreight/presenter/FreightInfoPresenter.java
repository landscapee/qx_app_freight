package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.MarketCollectionRequireBean;
import qx.app.freight.qxappfreight.contract.FreightInfoContract;
import qx.app.freight.qxappfreight.model.FreightInfoModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class FreightInfoPresenter extends BasePresenter {

    public FreightInfoPresenter(FreightInfoContract.freightInfoView freightInfoView) {
        mRequestView = freightInfoView;
        mRequestModel = new FreightInfoModel();
    }

    public void freightInfo(String id) {
        mRequestView.showNetDialog();
        ((FreightInfoModel) mRequestModel).freightInfo(id, new IResultLisenter<List<MarketCollectionRequireBean>>() {
            @Override
            public void onSuccess(List<MarketCollectionRequireBean> beanList) {
                ((FreightInfoContract.freightInfoView) mRequestView).freightInfoResult(beanList);
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
