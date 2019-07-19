package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.GetHistoryBean;

public class GetHistoryContract {
    public interface getHistoryModel {
        void getHistory(BaseFilterEntity entity,IResultLisenter lisenter);
    }

    public interface getHistoryView extends IBaseView {
        void getHistoryResult(GetHistoryBean getHistoryBean);
    }
}
