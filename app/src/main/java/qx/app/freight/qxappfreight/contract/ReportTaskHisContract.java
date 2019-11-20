package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;

public class ReportTaskHisContract {
    public interface reportTaskHisModel {
        void reportTaskHis(BaseFilterEntity operatorId, IResultLisenter lisenter);
    }

    public interface eportTaskHisView extends IBaseView {
        void reportTaskHisResult(List<LoadAndUnloadTodoBean> loadAndUnloadTodoBeans);
    }
}
