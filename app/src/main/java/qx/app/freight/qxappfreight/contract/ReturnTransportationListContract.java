package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ReturnBean;

public class ReturnTransportationListContract {

    public interface returnTransportationListModel {
        void returnTransportationList(BaseFilterEntity entity, IResultLisenter lisenter);
    }

    public interface returnTransportationListView extends IBaseView {
        void returnTransportationListResult(List<ReturnBean> addScooterBean);
    }
}
