package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.UnLoadRequestEntity;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.bean.response.UnLoadListBillBean;

public class GetUnLoadListBillContract {
    public interface IModel {
        void getUnLoadingList(UnLoadRequestEntity entity, IResultLisenter lisenter);
        void getUnLoadDoneScooter(String entity, IResultLisenter lisenter);
    }

    public interface IView extends IBaseView {
        void getUnLoadingListResult(UnLoadListBillBean result);
        void getUnLoadDoneScooterResult(List<TransportTodoListBean> result);
    }

}
