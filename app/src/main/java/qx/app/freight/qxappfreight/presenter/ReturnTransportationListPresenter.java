package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ReturnBean;
import qx.app.freight.qxappfreight.contract.ReturnTransportationListContract;
import qx.app.freight.qxappfreight.model.ReturnTransportationListModel;

public class ReturnTransportationListPresenter extends BasePresenter {
    public ReturnTransportationListPresenter(ReturnTransportationListContract.returnTransportationListView returnTransportationListView) {
        mRequestView = returnTransportationListView;
        mRequestModel = new ReturnTransportationListModel();
    }

    public void returnTransportationList(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((ReturnTransportationListModel) mRequestModel).returnTransportationList(entity, new IResultLisenter<List<ReturnBean>>() {
            @Override
            public void onSuccess(List<ReturnBean> result) {
                ((ReturnTransportationListContract.returnTransportationListView) mRequestView).returnTransportationListResult(result);
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
