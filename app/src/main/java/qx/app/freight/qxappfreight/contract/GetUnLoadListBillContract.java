package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.UnLoadRequestEntity;
import qx.app.freight.qxappfreight.bean.response.UnLoadListBillBean;

public class GetUnLoadListBillContract {
    public interface IModel {
        void getUnLoadingList(UnLoadRequestEntity entity, IResultLisenter lisenter);
    }

    public interface IView extends IBaseView {
        void getUnLoadingListResult(UnLoadListBillBean result);
    }

}
