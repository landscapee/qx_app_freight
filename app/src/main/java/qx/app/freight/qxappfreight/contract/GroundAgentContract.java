package qx.app.freight.qxappfreight.contract;



import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.ScooterTransitBean;
import qx.app.freight.qxappfreight.bean.response.GroundAgentBean;

public class GroundAgentContract {
    public interface GroundAgentModel {
        void newScooter(ScooterTransitBean scooter, IResultLisenter lisenter);
        void getAllAgent(IResultLisenter lisenter);
    }

    public interface GroundAgentView extends IBaseView {
        void newScooterResult();
        void getAllAgentResult(List <GroundAgentBean> groundAgentBeans);
    }
}
