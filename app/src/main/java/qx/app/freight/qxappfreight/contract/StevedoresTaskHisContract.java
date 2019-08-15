package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;

public class StevedoresTaskHisContract  {
    public interface stevedoresTaskHisModel {
        void stevedoresTaskHis(String operatorId, IResultLisenter lisenter);
    }

    public interface stevedoresTaskHisView extends IBaseView {
        void stevedoresTaskHisResult(List<LoadAndUnloadTodoBean> loadAndUnloadTodoBeans);
    }
}
