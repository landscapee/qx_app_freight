package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.GroupBoardRequestEntity;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.contract.GroupBoardToDoContract;
import qx.app.freight.qxappfreight.model.GroupBoardToDoListModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class GroupBoardToDoPresenter extends BasePresenter {

    public GroupBoardToDoPresenter(GroupBoardToDoContract.GroupBoardToDoView transportListContractView) {
        mRequestView = transportListContractView;
        mRequestModel = new GroupBoardToDoListModel();
    }

    public void getGroupBoardToDo(GroupBoardRequestEntity model) {
        mRequestView.showNetDialog();
        ((GroupBoardToDoListModel) mRequestModel).getGroupBoardToDo(model, new IResultLisenter<List<TransportDataBase>>() {
            @Override
            public void onSuccess(List<TransportDataBase> transportListBeans) {
                ((GroupBoardToDoContract.GroupBoardToDoView) mRequestView).getGroupBoardToDoResult(transportListBeans);
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
