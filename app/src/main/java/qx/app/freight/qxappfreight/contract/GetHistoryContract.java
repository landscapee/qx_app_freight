package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.SearchFilghtEntity;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.GetHistoryBean;
import qx.app.freight.qxappfreight.bean.response.SearchFlightInfoBean;

public class GetHistoryContract {
    public interface getHistoryModel {
        void getHistory(BaseFilterEntity entity, IResultLisenter lisenter);

        void searchFlightsByKey(SearchFilghtEntity key, IResultLisenter<List<SearchFlightInfoBean>> lisenter);
    }

    public interface getHistoryView extends IBaseView {
        void getHistoryResult(GetHistoryBean getHistoryBean);

        void searchFlightsByKeyResult(List<SearchFlightInfoBean> result);
    }
}
