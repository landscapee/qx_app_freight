package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.OutFieldTaskBean;

public class TransportTaskHisContract {
    public interface transportTaskHisModel {
        void transportTaskHis(BaseFilterEntity operatorId, IResultLisenter lisenter);
    }

    public interface transportTaskHisView extends IBaseView {
        void transportTaskHisResult(List<OutFieldTaskBean> transportTodoListBeans);
    }
}
