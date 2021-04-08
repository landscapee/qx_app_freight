package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.GroupBoardRequestEntity;
import qx.app.freight.qxappfreight.bean.response.FilterTransportDateBase;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.bean.response.WaybillsListBean;
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

    public void getGroupBoardToDo(BaseFilterEntity model) {
        mRequestView.showNetDialog();
        ((GroupBoardToDoListModel) mRequestModel).getGroupBoardToDo(model, new IResultLisenter<FilterTransportDateBase>() {
            @Override
            public void onSuccess(FilterTransportDateBase transportListBeans) {
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
    public void getOverWeightToDo(BaseFilterEntity model) {
        mRequestView.showNetDialog();
        ((GroupBoardToDoListModel) mRequestModel).getOverWeightToDo(model, new IResultLisenter<FilterTransportDateBase>() {
            @Override
            public void onSuccess(FilterTransportDateBase transportListBeans) {
                ((GroupBoardToDoContract.GroupBoardToDoView) mRequestView).getOverWeightToDoResult(transportListBeans);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }

    public void getScooterByScooterCode(BaseFilterEntity airLineId) {
        mRequestView.showNetDialog();
        ((GroupBoardToDoListModel) mRequestModel).getScooterByScooterCode(airLineId, new IResultLisenter<List<GetInfosByFlightIdBean>>() {
            @Override
            public void onSuccess(List<GetInfosByFlightIdBean> queryAviationRequireBeans) {
                ((GroupBoardToDoContract.GroupBoardToDoView) mRequestView).getScooterByScooterCodeResult(queryAviationRequireBeans);
                mRequestView.dissMiss();
            }

            @Override
            public void onFail(String error) {
                mRequestView.toastView(error);
                mRequestView.dissMiss();
            }
        });
    }
    public void searchWaybillByWaybillCode(BaseFilterEntity airLineId) {
        mRequestView.showNetDialog();
        ((GroupBoardToDoListModel) mRequestModel).searchWaybillByWaybillCode(airLineId, new IResultLisenter<WaybillsListBean>() {
            @Override
            public void onSuccess(WaybillsListBean waybillsBeans) {
                ((GroupBoardToDoContract.GroupBoardToDoView) mRequestView).searchWaybillByWaybillCodeResult(waybillsBeans.getRecords());
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
