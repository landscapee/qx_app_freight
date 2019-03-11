package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.AgentBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;

/**
 * TODO : xxx
 * Created by pr
 */
public class AgentTransportationListContract {
    public interface agentTransportationListModel {
        void agentTransportationList(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter);
    }

    public interface agentTransportationListView extends IBaseView {
        void agentTransportationListResult(AgentBean myAgentListBean);
    }
}
