package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.GetAllRemoteAreaBean;
import qx.app.freight.qxappfreight.contract.GetAllRemoteAreaContract;
import qx.app.freight.qxappfreight.model.GetAllRemoteAreaModel;

public class GetAllRemoteAreaPresenter extends BasePresenter {
    public GetAllRemoteAreaPresenter(GetAllRemoteAreaContract.getAllRemoteAreaView getAllRemoteAreaView) {
        mRequestView = getAllRemoteAreaView;
        mRequestModel = new GetAllRemoteAreaModel();
    }

    public void getAllRemoteArea() {
        mRequestView.showNetDialog();
        ((GetAllRemoteAreaModel) mRequestModel).getAllRemoteArea(new IResultLisenter<List<GetAllRemoteAreaBean>>() {
            @Override
            public void onSuccess(List<GetAllRemoteAreaBean> result) {
                ((GetAllRemoteAreaContract.getAllRemoteAreaView) mRequestView).getAllRemoteAreaResult(result);
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
