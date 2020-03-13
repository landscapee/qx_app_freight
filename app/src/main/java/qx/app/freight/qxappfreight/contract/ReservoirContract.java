package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ReservoirBean;

public class ReservoirContract {
    public interface reservoirModel {
        void reservoir(BaseFilterEntity model, IResultLisenter lisenter);
        void getAirWaybillPrefix(String iata, IResultLisenter lisenter);
    }

    public interface reservoirView extends IBaseView {
        void reservoirResult(ReservoirBean acceptTerminalTodoBeanList);
        void getAirWaybillPrefixResult(String getAirWaybillPrefixBean);
    }
}
