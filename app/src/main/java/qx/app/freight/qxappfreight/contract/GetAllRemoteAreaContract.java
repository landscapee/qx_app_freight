package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.GetAllRemoteAreaBean;

public class GetAllRemoteAreaContract {
    public interface getAllRemoteAreaModel {
        void getAllRemoteArea(IResultLisenter lisenter);
    }

    public interface getAllRemoteAreaView extends IBaseView {
        void getAllRemoteAreaResult(List<GetAllRemoteAreaBean> getAllRemoteAreaBean);
    }
}
