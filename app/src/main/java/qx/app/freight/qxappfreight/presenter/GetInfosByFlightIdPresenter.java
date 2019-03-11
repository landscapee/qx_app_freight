package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.contract.GetInfosByFlightIdContract;
import qx.app.freight.qxappfreight.model.GetInfosByFlightIdModel;

public class GetInfosByFlightIdPresenter extends BasePresenter {
    public GetInfosByFlightIdPresenter(GetInfosByFlightIdContract.getInfosByFlightIdView getInfosByFlightIdView) {
        mRequestView = getInfosByFlightIdView;
        mRequestModel = new GetInfosByFlightIdModel();
    }

    public void getInfosByFlightId(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((GetInfosByFlightIdModel) mRequestModel).getInfosByFlightId(entity, new IResultLisenter<List<GetInfosByFlightIdBean>>() {
            @Override
            public void onSuccess(List<GetInfosByFlightIdBean> getInfosByFlightIdBeans) {
                ((GetInfosByFlightIdContract.getInfosByFlightIdView) mRequestView).getInfosByFlightIdResult(getInfosByFlightIdBeans);
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
