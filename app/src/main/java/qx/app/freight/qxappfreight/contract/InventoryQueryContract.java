package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.InventoryQueryBean;

public class InventoryQueryContract {
    public interface inventoryQueryModel {
        void inventoryQuery(BaseFilterEntity entity,IResultLisenter lisenter);
        void inventoryHistoryQuery(BaseFilterEntity entity,IResultLisenter lisenter);
    }

    public interface inventoryQueryView extends IBaseView {
        void inventoryQueryResult(InventoryQueryBean inventoryQueryBean);
        void inventoryQueryHistoryResult(InventoryQueryBean inventoryQueryBean);
    }
}
