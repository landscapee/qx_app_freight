package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.InventoryDetailEntity;

public class ListInventoryDetailContract {
    public interface listInventoryDetailModel {
        void listInventoryDetail(BaseFilterEntity entity, IResultLisenter lisenter);
    }

    public interface listInventoryDetailView extends IBaseView {
        void listInventoryDetailResult(List<InventoryDetailEntity> entityList);
    }
}
