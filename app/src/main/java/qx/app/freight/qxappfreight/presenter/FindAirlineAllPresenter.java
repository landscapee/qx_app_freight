package qx.app.freight.qxappfreight.presenter;


import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.FindAirlineAllBean;
import qx.app.freight.qxappfreight.contract.FindAirlineAllContract;
import qx.app.freight.qxappfreight.model.FindAirlineAllModel;

public class FindAirlineAllPresenter extends BasePresenter {
    public FindAirlineAllPresenter(FindAirlineAllContract.findAirlineAllView findAirlineAllView) {
        mRequestView = findAirlineAllView;
        mRequestModel = new FindAirlineAllModel();
    }

    public void findAirlineAll() {
        mRequestView.showNetDialog();
        ((FindAirlineAllModel) mRequestModel).findAirlineAll(new IResultLisenter<List<FindAirlineAllBean>>() {
            @Override
            public void onSuccess(List<FindAirlineAllBean> findAirlineAllBeans) {
                ((FindAirlineAllContract.findAirlineAllView) mRequestView).findAirlineAllResult(findAirlineAllBeans);
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
