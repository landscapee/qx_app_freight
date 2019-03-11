package qx.app.freight.qxappfreight.presenter;

import java.util.List;
import java.util.Map;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.contract.TransportListContract;
import qx.app.freight.qxappfreight.model.TransportListModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class TransportListPresenter extends BasePresenter {

    public TransportListPresenter(TransportListContract.transportListContractView transportListContractView) {
        mRequestView = transportListContractView;
        mRequestModel = new TransportListModel();
    }

    public void transportListPresenter(BaseFilterEntity model) {
        mRequestView.showNetDialog();
        ((TransportListModel) mRequestModel).transportListContract(model, new IResultLisenter<List<TransportListBean>>() {
            @Override
            public void onSuccess(List<TransportListBean> transportListBeans) {
                ((TransportListContract.transportListContractView) mRequestView).transportListContractResult(transportListBeans);
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
