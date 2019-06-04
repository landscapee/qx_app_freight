package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.SearchReservoirBean;
import qx.app.freight.qxappfreight.contract.SearchReservoirContract;
import qx.app.freight.qxappfreight.model.SearchReservoirModel;

public class SearchReservoirPresenter extends BasePresenter {
    public SearchReservoirPresenter(SearchReservoirContract.searchReservoirView searchReservoirView) {
        mRequestView = searchReservoirView;
        mRequestModel = new SearchReservoirModel();
    }

    public void searchReservoir(BaseFilterEntity entity) {
        mRequestView.showNetDialog();
        ((SearchReservoirModel) mRequestModel).searchReservoir(entity, new IResultLisenter<SearchReservoirBean>() {
            @Override
            public void onSuccess(SearchReservoirBean result) {
                ((SearchReservoirContract.searchReservoirView) mRequestView).searchReservoirResult(result);
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
