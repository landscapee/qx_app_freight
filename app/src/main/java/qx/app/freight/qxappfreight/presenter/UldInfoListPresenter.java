package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.UldInfoListBean;
import qx.app.freight.qxappfreight.contract.UldInfoListContract;
import qx.app.freight.qxappfreight.model.UldInfoListModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class UldInfoListPresenter extends BasePresenter {
    public UldInfoListPresenter(UldInfoListContract.uldInfoListView uldInfoListView) {
        mRequestView = uldInfoListView;
        mRequestModel = new UldInfoListModel();
    }

    public void UldInfoList(BaseFilterEntity model) {
        mRequestView.showNetDialog();
        ((UldInfoListModel) mRequestModel).uldInfoList(model, new IResultLisenter<List<UldInfoListBean>>() {
            @Override
            public void onSuccess(List<UldInfoListBean> scooterInfoListBeans) {
                ((UldInfoListContract.uldInfoListView) mRequestView).uldInfoListResult(scooterInfoListBeans);
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
