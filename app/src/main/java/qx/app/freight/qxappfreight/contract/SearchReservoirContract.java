package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.SearchReservoirBean;

public class SearchReservoirContract {
    public interface searchReservoirModel {
        void searchReservoir(BaseFilterEntity entity,IResultLisenter lisenter);
    }

    public interface searchReservoirView extends IBaseView {
        void searchReservoirResult(SearchReservoirBean searchReservoirBeanList);
    }
}
