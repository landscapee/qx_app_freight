package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;

/**
 * TODO : xxx
 * Created by pr
 */
public class TransportListContract {
    public interface transportListContractModel {
        void transportListContract(BaseFilterEntity model, IResultLisenter lisenter);
    }

    public interface transportListContractView extends IBaseView {
        void transportListContractResult(TransportListBean transportListBeans);
    }
}
