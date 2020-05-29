package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.GroupBoardRequestEntity;
import qx.app.freight.qxappfreight.bean.response.FilterTransportDateBase;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;

/**
 */
public class GroupBoardToDoContract {
    public interface GroupBoardToDoModel {
        void getGroupBoardToDo(BaseFilterEntity model, IResultLisenter lisenter);
        void getScooterByScooterCode(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter);
        void searchWaybillByWaybillCode(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter);
        void getOverWeightToDo(BaseFilterEntity model, IResultLisenter lisenter);

    }

    public interface GroupBoardToDoView extends IBaseView {
        void getGroupBoardToDoResult(FilterTransportDateBase transportListBeans);
        void getOverWeightToDoResult(FilterTransportDateBase transportListBeans);
        void getScooterByScooterCodeResult(List<GetInfosByFlightIdBean> getInfosByFlightIdBean);
        void searchWaybillByWaybillCodeResult(List<WaybillsBean> waybillsBeans);

    }
}
