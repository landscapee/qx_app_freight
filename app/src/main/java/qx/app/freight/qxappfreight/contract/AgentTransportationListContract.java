package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.AgentBean;
import qx.app.freight.qxappfreight.bean.response.AutoReservoirBean;

/**
 * TODO : xxx
 * Created by pr
 */
public class AgentTransportationListContract {
    public interface agentTransportationListModel {
        void autoReservoirv(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter);
        void agentTransportationList(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter);
    }

    public interface agentTransportationListView extends IBaseView {
        void agentTransportationListResult(AgentBean myAgentListBean);
        void autoReservoirvResult(AutoReservoirBean myAgentListBean);
    }
}
