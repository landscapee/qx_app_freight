package qx.app.freight.qxappfreight.presenter;

import java.util.List;

import qx.app.freight.qxappfreight.app.BasePresenter;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.AgentBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.contract.AgentTransportationListContract;
import qx.app.freight.qxappfreight.model.AgentTransportationListModel;

/**
 * TODO : xxx
 * Created by pr
 */
public class AgentTransportationListPresent extends BasePresenter {

    public AgentTransportationListPresent(AgentTransportationListContract.agentTransportationListView agentTransportationListView) {
        mRequestView = agentTransportationListView;
        mRequestModel = new AgentTransportationListModel();
    }

    public void agentTransportationList(BaseFilterEntity param) {
        mRequestView.showNetDialog();
        ((AgentTransportationListModel) mRequestModel).agentTransportationList(param, new IResultLisenter<AgentBean>() {
            @Override
            public void onSuccess(AgentBean myAgentListBean) {
                ((AgentTransportationListContract.agentTransportationListView) mRequestView).agentTransportationListResult(myAgentListBean);
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
