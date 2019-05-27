package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.GroupBoardRequestEntity;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;

/**
 */
public class GroupBoardToDoContract {
    public interface GroupBoardToDoModel {
        void getGroupBoardToDo(GroupBoardRequestEntity model, IResultLisenter lisenter);
    }

    public interface GroupBoardToDoView extends IBaseView {
        void getGroupBoardToDoResult(List<TransportDataBase> transportListBeans);
    }
}
