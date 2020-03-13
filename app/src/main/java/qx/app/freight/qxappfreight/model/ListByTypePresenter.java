package qx.app.freight.qxappfreight.model;


import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ListByTypeBean;
import qx.app.freight.qxappfreight.contract.ListByTypeContract;

public class ListByTypePresenter extends BasePresenter {

    public ListByTypePresenter(ListByTypeContract.listByTypeView likePageView) {
        mRequestView = likePageView;
        mRequestModel = new ListByTypeModel();
    }

    public void listByType(BaseFilterEntity entity ) {
        ((ListByTypeModel) mRequestModel).listByType(entity, new IResultLisenter<ListByTypeBean>() {
            @Override
            public void onSuccess(ListByTypeBean listByTypeBeans) {
                ((ListByTypeContract.listByTypeView) mRequestView).listByTypeResult(listByTypeBeans);
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
            }
        });
    }
}
