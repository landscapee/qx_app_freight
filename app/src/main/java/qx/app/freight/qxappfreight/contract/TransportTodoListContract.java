package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;

/**
 * TODO : xxx
 * Created by pr
 */
public class TransportTodoListContract {
    public interface transportTodoListModel {
        void transportTodoList(IResultLisenter lisenter);
    }

    public interface transportTodoListView extends IBaseView {
        void transportTodoListResult(List<TransportTodoListBean> transportTodoListBeans);
    }
}
