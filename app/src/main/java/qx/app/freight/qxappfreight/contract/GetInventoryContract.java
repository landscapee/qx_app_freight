package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.SmInventorySummary;

/**
 * TODO : xxx
 * Created by pr
 */
public class GetInventoryContract {

    public interface getInventoryModel {
        void getInventory(BaseFilterEntity entity, IResultLisenter lisenter);
    }

    public interface getInventoryView extends IBaseView {
        void setInventoryResult(List <SmInventorySummary> result);
    }
}
