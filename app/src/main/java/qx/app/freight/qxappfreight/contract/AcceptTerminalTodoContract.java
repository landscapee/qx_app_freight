package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.AcceptTerminalTodoBean;

public class AcceptTerminalTodoContract {
    public interface acceptTerminalTodoModel {
        void acceptTerminalTodo(BaseFilterEntity model, IResultLisenter lisenter);
    }

    public interface acceptTerminalTodoView extends IBaseView {
        void acceptTerminalTodoResult(List<AcceptTerminalTodoBean> acceptTerminalTodoBeanList);
    }
}
