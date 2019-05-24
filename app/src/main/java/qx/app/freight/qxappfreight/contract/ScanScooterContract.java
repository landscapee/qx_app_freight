package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;

/**
 * TODO : 添加  和 查询
 * Created by pr
 */
public class ScanScooterContract {
    public interface scanScooterModel {
        void scanScooter(TransportTodoListBean transportEndEntity, IResultLisenter lisenter);
        void scanLockScooter(List<TransportTodoListBean> transportEndEntity, IResultLisenter lisenter);
        void scooterWithUser(String user,IResultLisenter lisenter);

    }

    public interface scanScooterView extends IBaseView {
        void scanScooterResult(String result);
        void scanLockScooterResult(String result);
        void scooterWithUserResult(List<TransportTodoListBean> result);

    }
}
