package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;

/**
 * Created by guohao on 2019/5/8 15:38 @COPYRIGHT 青霄科技
 *
 * @title：运单操作，回退到预配
 * @description：
 */
public class ScooterOperateContract {

    public interface scooterOperateModel {
        void returnToPrematching(BaseFilterEntity entity, IResultLisenter lisenter);
    }

    public interface scooterOperateView extends IBaseView {
        void returnToPrematching(Object result);
    }
}
