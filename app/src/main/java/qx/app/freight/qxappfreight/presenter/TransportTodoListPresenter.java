package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.contract.TransportTodoListContract;
import qx.app.freight.qxappfreight.model.TransportTodoListModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class TransportTodoListPresenter extends BasePresenter {
    public TransportTodoListPresenter(TransportTodoListContract.transportTodoListView transportTodoListView) {
        mRequestView = transportTodoListView;
        mRequestModel = new TransportTodoListModel();
    }

    public void transportTodoList() {
        mRequestView.showNetDialog();
        ((TransportTodoListModel) mRequestModel).transportTodoList(new IResultLisenter<List<TransportTodoListBean>>() {
            @Override
            public void onSuccess(List<TransportTodoListBean> transportTodoListBeans) {
                ((TransportTodoListContract.transportTodoListView) mRequestView).transportTodoListResult(transportTodoListBeans);
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
