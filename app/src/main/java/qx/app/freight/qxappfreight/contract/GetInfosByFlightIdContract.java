package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;

public class GetInfosByFlightIdContract {
    public interface getInfosByFlightIdModel {
        void getInfosByFlightId(BaseFilterEntity entity, IResultLisenter lisenter);
    }

    public interface getInfosByFlightIdView extends IBaseView {
        void getInfosByFlightIdResult(List<GetInfosByFlightIdBean> getInfosByFlightIdBeans);
    }
}
