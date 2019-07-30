package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.LastReportInfoListBean;

public class GetLastReportInfoContract {
    public interface getLastReportInfoModel {
        void getLastReportInfo(BaseFilterEntity entity, IResultLisenter lisenter);
    }

    public interface getLastReportInfoView extends IBaseView {
        void getLastReportInfoResult(LastReportInfoListBean lastReportInfoListBeans);
    }
}
