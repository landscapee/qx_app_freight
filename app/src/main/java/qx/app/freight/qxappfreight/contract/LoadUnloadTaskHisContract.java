package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;

public class LoadUnloadTaskHisContract {
    public interface loadUnloadTaskHisModel {
        void loadUnloadTaskHis(String operatorId, IResultLisenter lisenter);
    }

    public interface loadUnloadTaskHisView extends IBaseView {
        void loadUnloadTaskHisResult(List<LoadAndUnloadTodoBean> transportTodoListBeans);
    }
}
