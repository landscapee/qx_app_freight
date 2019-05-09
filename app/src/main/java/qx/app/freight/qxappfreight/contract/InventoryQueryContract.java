package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.InventoryQueryBean;

public class InventoryQueryContract {
    public interface inventoryQueryModel {
        void inventoryQuery(IResultLisenter lisenter);
    }

    public interface inventoryQueryView extends IBaseView {
        void inventoryQueryResult(List<InventoryQueryBean> inventoryQueryBean);
    }
}
