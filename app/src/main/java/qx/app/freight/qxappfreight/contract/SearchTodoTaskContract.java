package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.AddScooterBean;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;

/**
 * Created by guohao on 2019/5/17 16:15 @COPYRIGHT 青霄科技
 *
 * @title：
 * @description：
 */
public class SearchTodoTaskContract {
    public interface searchTodoTaskModel {
        void searchTodoTask(BaseFilterEntity entity, IResultLisenter lisenter);
    }

    public interface searchTodoTaskView extends IBaseView {
        void searchTodoTaskResult(TransportListBean transportListBean);
    }
}
