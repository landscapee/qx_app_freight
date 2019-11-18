package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;

public class LoadUnloadTaskHisContract {
    public interface loadUnloadTaskHisModel {
        void loadUnloadTaskHis(BaseFilterEntity operatorId, IResultLisenter lisenter);
    }

    public interface loadUnloadTaskHisView extends IBaseView {
        void loadUnloadTaskHisResult(List<LoadAndUnloadTodoBean> transportTodoListBeans);
    }
}
