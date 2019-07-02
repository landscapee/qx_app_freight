package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.TodoScootersEntity;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.bean.response.GetTodoScootersBean;

public class TodoScootersContract {
    public interface todoScootersModel {
        void todoScooters(TodoScootersEntity todoScootersEntity, IResultLisenter lisenter);
    }

    public interface todoScootersView extends IBaseView {
        void todoScootersResult(List<GetInfosByFlightIdBean> result);
    }
}
