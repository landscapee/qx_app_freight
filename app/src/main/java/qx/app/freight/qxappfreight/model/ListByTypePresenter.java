package qx.app.freight.qxappfreight.model;


import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.ListByTypeBean;
import qx.app.freight.qxappfreight.contract.ListByTypeContract;

public class ListByTypePresenter extends BasePresenter {

    public ListByTypePresenter(ListByTypeContract.listByTypeView likePageView) {
        mRequestView = likePageView;
        mRequestModel = new ListByTypeModel();
    }

    public void listByType(String name) {
        ((ListByTypeModel) mRequestModel).listByType(name, new IResultLisenter<List<ListByTypeBean>>() {
            @Override
            public void onSuccess(List<ListByTypeBean> listByTypeBeans) {
                ((ListByTypeContract.listByTypeView) mRequestView).listByTypeResult(listByTypeBeans);
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
            }
        });
    }
}
